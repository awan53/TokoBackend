package com.tokobackend.toko.service;
import com.tokobackend.toko.model.Category; // Perlu untuk mencari kategori
import com.tokobackend.toko.model.Product;
import com.tokobackend.toko.payload.request.ProductRequest; // <<< TAMBAHKAN INI
import com.tokobackend.toko.repository.CategoryRepository; // <<< TAMBAHKAN INI jika belum ada
import com.tokobackend.toko.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // --- METODE YANG HILANG (TAMBAHKAN KEMBALI INI) ---

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }

    public List<Product> searchProductsByName(String name) {
        // Asumsi ada metode findByNameContainingIgnoreCase di ProductRepository
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        // Asumsi ada metode findByCategoryId di ProductRepository
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        // Asumsi ada metode findByPriceBetween di ProductRepository
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    // --- METODE saveProduct dan updateProduct (yang sudah kita revisi) ---

    public Product saveProduct(ProductRequest productRequest) {
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + productRequest.getCategoryId()));

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());
        // PASTIKAN method setImageUrl() ada di Product.java
        product.setImgUrl(productRequest.getImageUrl());
        product.setCategory(category);

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, ProductRequest productRequest) {
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(productRequest.getName());
            existingProduct.setDescription(productRequest.getDescription());
            existingProduct.setPrice(productRequest.getPrice());
            existingProduct.setStock(productRequest.getStock());
            // PASTIKAN method setImageUrl() ada di Product.java
            existingProduct.setImgUrl(productRequest.getImageUrl());

            if (productRequest.getCategoryId() != null) {
                Category newCategory = categoryRepository.findById(productRequest.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("Category not found with ID: " + productRequest.getCategoryId()));
                existingProduct.setCategory(newCategory);
            } else {
                throw new RuntimeException("Category ID cannot be null for product update.");
            }

            return productRepository.save(existingProduct);
        }).orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }

}
