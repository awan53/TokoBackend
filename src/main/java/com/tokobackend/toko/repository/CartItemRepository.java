package com.tokobackend.toko.repository;
import com.tokobackend.toko.model.CartItem;
import com.tokobackend.toko.model.Product;
import com.tokobackend.toko.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{
    List<CartItem> findByUser(User user);
    Optional<CartItem> findByUserAndProduct(User user, Product product);
    void deleteByUser(User user);

}
