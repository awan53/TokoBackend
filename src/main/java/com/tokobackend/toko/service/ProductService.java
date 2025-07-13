package com.tokobackend.toko.service;
import com.tokobackend.toko.model.Product;
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

    public List<Product> getAllProducts()
    {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id)
    {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        // Logika validasi (misal stock > 0, price > 0)
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id).map(product -> {
            product.setName(productDetails.getName());
            product.setDescription(productDetails.getDescription());
            product.setPrice(productDetails.getPrice());
            product.setStock(productDetails.getStock());
            product.setImgUrl(productDetails.getImgUrl());
            product.setCategory(productDetails.getCategory()); // Hati-hati dengan relasi
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found with id " + id));
    }

    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

}
