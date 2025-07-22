package com.tokobackend.toko.controller;

import com.tokobackend.toko.model.Product;
import com.tokobackend.toko.payload.request.ProductRequest;
import com.tokobackend.toko.service.ProductService;
import com.tokobackend.toko.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProduct(){
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id){
        return productService.getProductById(id).map(product -> new ResponseEntity<>(product, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    // --- KOREKSI METODE createProduct DIMULAI ---
    public ResponseEntity<?> createProduct( // Mengubah return type ke '?' untuk fleksibilitas pesan error
                                            @Valid @RequestBody ProductRequest productRequest,
                                            BindingResult bindingResult) {

        // Penanganan Validasi DTO
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            // Memanggil service untuk menyimpan produk, meneruskan payload (ProductRequest)
            Product savedProduct = productService.saveProduct(productRequest);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Tangani error jika kategori tidak ditemukan atau masalah lain dari service
            System.err.println("Error creating product: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>("Failed to create product: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    } // --- KOREKSI METODE createProduct BERAKHIR ---

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest productRequest,
            BindingResult bindingResult) {

        // Penanganan Validasi DTO
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            // Panggil service untuk update, meneruskan id dan ProductRequest
            Product updatedProduct = productService.updateProduct(id, productRequest);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Tangani error jika produk atau kategori tidak ditemukan
            System.err.println("Error updating product: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>("Failed to update product: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProductsByName(@RequestParam String name) {
        List<Product> products = productService.searchProductsByName(name);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.getProductsByCategory(categoryId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


}
