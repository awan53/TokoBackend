package com.tokobackend.toko.repository;

import com.tokobackend.toko.model.Order;
import com.tokobackend.toko.model.Product;
import com.tokobackend.toko.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
//    List<Product> findByNameContainingIgnoreCase(String name);
//    List<Product> findByCategoryId(Long categoryId);
//    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    List<Order> findByUser(User user);

}
