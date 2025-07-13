package com.tokobackend.toko.repository;

import com.tokobackend.toko.model.Order;
import com.tokobackend.toko.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    List<Order> findByStatus(String status);
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);


}
