package com.tokobackend.toko.repository;
import com.tokobackend.toko.model.OrderItem;
import com.tokobackend.toko.model.Order;
import com.tokobackend.toko.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);
    List<OrderItem> findByProduct(Product product);
}
