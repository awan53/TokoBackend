package com.tokobackend.toko.service;
import com.tokobackend.toko.model.Category; // Perlu untuk mencari kategori
import com.tokobackend.toko.model.Product;
import com.tokobackend.toko.payload.request.ProductRequest; // <<< TAMBAHKAN INI
import com.tokobackend.toko.repository.CategoryRepository; // <<< TAMBAHKAN INI jika belum ada
import com.tokobackend.toko.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

      // --- METODE saveProduct dan updateProduct (yang sudah kita revisi) ---

    @Transactional
    public Product saveProduct(ProductRequest productRequest) {
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + productRequest.getCategoryId()));

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());

        product.setImgUrl(productRequest.getImageUrl());
        product.setCategory(category);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(product);
    }

    @Transactional
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


    @Transactional
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

    @Transactional
    public void decreaseProductStock(Long productId, Integer quantity){
        Product product = productRepository.findById(productId).orElseThrow(()-> new RuntimeException("Product not Found with Id : "+productId));

        if (product.getStock() < quantity){
            throw new RuntimeException("Stok tidak mencukupi untuk product : " +product.getName()+ ".Stok tersedia : " +product.getStock());
        }
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }





}
