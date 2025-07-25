package com.tokobackend.toko.service;

import com.tokobackend.toko.model.Order;
import com.tokobackend.toko.model.OrderItem;
import com.tokobackend.toko.model.User;
import com.tokobackend.toko.model.Product;
import com.tokobackend.toko.payload.request.OrderItemRequest;
import com.tokobackend.toko.payload.request.OrderRequest;
import com.tokobackend.toko.repository.OrderItemRepository;
import com.tokobackend.toko.repository.OrderRepository;
import com.tokobackend.toko.repository.UserRepository;
import com.tokobackend.toko.repository.ProductRepository;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductService productService;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional // Pastikan seluruh proses pemesanan berjalan dalam satu transaksi
    public Order createOrder(OrderRequest orderRequest) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String username = authentication.getName();

      User currentUser = userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("user tidak ditemukan "+username));

        Order order = new Order(currentUser);
        BigDecimal totalAmount = BigDecimal.ZERO;

        // 3. Proses setiap item dalam pesanan
        for (OrderItemRequest itemRequest : orderRequest.getItems()) {
            // Dapatkan produk melalui ProductService
            Product product = productService.getProductById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan dengan ID: " + itemRequest.getProductId()));

            // Periksa stok
            if (product.getStock() < itemRequest.getQuantity()) {
                throw new RuntimeException("Stok tidak mencukupi untuk produk: " + product.getName() + ". Stok tersedia: " + product.getStock());
            }

            // Buat OrderItem
            OrderItem orderItem = new OrderItem(order, product, itemRequest.getQuantity(), product.getPrice());
            order.addOrderItem(orderItem); // Tambahkan item ke daftar orderItems di objek Order

            // Hitung subtotal untuk item ini dan tambahkan ke total pesanan
            BigDecimal itemSubtotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            totalAmount = totalAmount.add(itemSubtotal);

            // Kurangi stok produk melalui ProductService
            productService.decreaseProductStock(product.getId(), itemRequest.getQuantity());
        }

        order.setTotalAmount(totalAmount);
        order.setStatus("COMPLETED"); // Atau "PENDING" jika ada proses pembayaran lebih lanjut

        // 4. Simpan Order (ini juga akan menyimpan OrderItem karena CascadeType.ALL pada Order entity)
        return orderRepository.save(order);
    }


    public void deleteOrder(Long id) {
        // Anda mungkin ingin mengembalikan stok produk sebelum menghapus order
        // Ini akan lebih kompleks dan tergantung pada kebijakan bisnis Anda
        orderRepository.deleteById(id);
    }

    public Order updateOrderStatus(Long id, String newStatus) {
        return orderRepository.findById(id).map(order -> {
            order.setStatus(newStatus);
            // Tambahkan validasi untuk status (misal: hanya bisa dari PENDING ke PROCESSING)
            return orderRepository.save(order);
        }).orElseThrow(() -> new RuntimeException("Order not found with id " + id));
    }

    public List<Order> getOrdersByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));
        return orderRepository.findByUser(user);
    }
}
