package com.tokobackend.toko.repository;

import com.tokobackend.toko.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository  extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name); // Mencari produk berdasarkan nama
    List<Product> findByCategoryId(Long categoryId); // Mencari produk berdasarkan kategori
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice); //
}
