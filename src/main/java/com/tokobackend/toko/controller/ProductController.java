package com.tokobackend.toko.controller;

import com.tokobackend.toko.model.Order;
import com.tokobackend.toko.model.Product;
import com.tokobackend.toko.service.ProductService;
import com.tokobackend.toko.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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
    public ResponseEntity<Product> createProduct(@RequestBody Product product){
        if (product.getCategory()== null || product.getCategory().getId()== null)
        {
          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        categoryService.getCategoryById(product.getCategory().getId()).ifPresentOrElse(product::setCategory, () ->{ throw new RuntimeException("Category tidak ditemukan" +product.getCategory().getId());});

        Product savedProduct = productService.saveProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        try {
            // Handle category update if provided
            if (productDetails.getCategory() != null && productDetails.getCategory().getId() != null) {
                categoryService.getCategoryById(productDetails.getCategory().getId())
                        .ifPresentOrElse(
                                productDetails::setCategory,
                                () -> { throw new RuntimeException("Category not found with id " + productDetails.getCategory().getId()); }
                        );
            } else {
                // Jika category_id dihilangkan dari request, ambil category_id yang lama
                // Atau putuskan apakah category_id bisa jadi null (sesuai skema NOT NULL)
                // Untuk kasus ini, karena NOT NULL, harus ada di request update juga
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Product updatedProduct = productService.updateProduct(id, productDetails);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
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
