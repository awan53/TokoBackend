package com.tokobackend.toko.service;

import com.tokobackend.toko.model.Order;
import com.tokobackend.toko.model.OrderItem;
import com.tokobackend.toko.model.User;
import com.tokobackend.toko.model.Product;
import com.tokobackend.toko.repository.OrderRepository;
import com.tokobackend.toko.repository.UserRepository;
import com.tokobackend.toko.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository; // Diperlukan untuk cek stok & harga

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional // Pastikan operasi ini bersifat transaksional
    public Order createOrder(Order orderDetails) {
        // Pastikan user ada
        User user = userRepository.findById(orderDetails.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found with id " + orderDetails.getUser().getId()));
        orderDetails.setUser(user);

        // Hitung total_amount dan validasi stok untuk setiap item
        BigDecimal calculatedTotalAmount = BigDecimal.ZERO;
        for (OrderItem item : orderDetails.getOrderItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id " + item.getProduct().getId()));

            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }

            // Set harga produk saat order dibuat (penting untuk histori)
            item.setPriceAtOrder(product.getPrice());
            calculatedTotalAmount = calculatedTotalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));

            // Kurangi stok produk
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product); // Simpan perubahan stok

            // Atur relasi bidirectional
            item.setOrder(orderDetails);
            item.setProduct(product);
        }

        // Set total amount dari perhitungan server, bukan dari input user
        orderDetails.setTotalAmount(calculatedTotalAmount);
        orderDetails.setStatus("PENDING"); // Pastikan status awal PENDING

        return orderRepository.save(orderDetails);
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
