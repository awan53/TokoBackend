package com.tokobackend.toko.service;

import com.tokobackend.toko.model.CartItem;
import com.tokobackend.toko.model.Product;
import com.tokobackend.toko.model.User;
import com.tokobackend.toko.repository.CartItemRepository;
import com.tokobackend.toko.repository.ProductRepository;
import com.tokobackend.toko.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;


    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    public Optional<CartItem> getCartItemById(Long id) {
        return cartItemRepository.findById(id);
    }

    @Transactional
    public CartItem addOrUpdateCartItem(Long userId, Long productId, Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));

        Optional<CartItem> existingCartItem = cartItemRepository.findByUserAndProduct(user, product);

        if (existingCartItem.isPresent()) {
            CartItem item = existingCartItem.get();
            item.setQuantity(item.getQuantity() + quantity); // Tambah kuantitas jika sudah ada
            return cartItemRepository.save(item);
        } else {
            CartItem newCartItem = new CartItem(user, product, quantity);
            return cartItemRepository.save(newCartItem);
        }
    }

    public void deleteCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }

    public List<CartItem> getCartItemsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));
        return cartItemRepository.findByUser(user);
    }

    @Transactional
    public void clearCartByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));
        cartItemRepository.deleteByUser(user);
    }


}
