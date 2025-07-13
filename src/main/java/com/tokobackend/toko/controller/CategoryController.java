package com.tokobackend.toko.controller;

import com.tokobackend.toko.model.Category;
import com.tokobackend.toko.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(category -> new ResponseEntity<>(category, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        // Jika parentCategoryId ada, cari dan set objek parentCategory
        if (category.getParentCategory() != null && category.getParentCategory().getId() != null) {
            categoryService.getCategoryById(category.getParentCategory().getId())
                    .ifPresent(category::setParentCategory);
        }
        Category savedCategory = categoryService.saveCategory(category);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails) {
        try {
            // Logika untuk set parentCategory di update
            if (categoryDetails.getParentCategory() != null && categoryDetails.getParentCategory().getId() != null) {
                categoryService.getCategoryById(categoryDetails.getParentCategory().getId())
                        .ifPresent(categoryDetails::setParentCategory);
            } else {
                categoryDetails.setParentCategory(null); // Jika parent_category_id dihilangkan dari request
            }
            Category updatedCategory = categoryService.updateCategory(id, categoryDetails);
            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/root")
    public ResponseEntity<List<Category>> getRootCategories() {
        List<Category> categories = categoryService.getRootCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/parent/{parentId}/subcategories")
    public ResponseEntity<List<Category>> getSubCategoriesByParentId(@PathVariable Long parentId) {
        List<Category> categories = categoryService.getSubCategoriesParentId(parentId);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
}
