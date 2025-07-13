package com.tokobackend.toko.repository;

import com.tokobackend.toko.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    List<Category> findByParentCategoryIsNull(); // Mencari kategori utama (tanpa parent)
    List<Category> findByParentCategoryId(Long parentCategoryId);
}
