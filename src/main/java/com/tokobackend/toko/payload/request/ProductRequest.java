package com.tokobackend.toko.payload.request;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ProductRequest {

    @NotBlank(message = "Nama produk tidak boleh kosong")
    @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Description cannot be empty")
    private String description; // Deskripsi bisa kosong

    @NotNull(message = "Harga produk tidak boleh kosong")
    @Min(value = 0, message = "Harga harus lebih besar dari atau sama dengan 0")
    private BigDecimal price; // Menggunakan BigDecimal untuk menghindari masalah presisi floating-point

    @NotNull(message = "Stock cannot be null")
    @Positive(message = "Stock must be positive")
    private Integer stock;

    private String imageUrl; // URL gambar bisa kosong

    @NotNull(message = "ID kategori tidak boleh kosong") // Biasanya kategori wajib ada
    private Long categoryId; // Klien akan mengirim ID kategori, bukan objek Kategori penuh

    // --- Konstruktor ---
    // Konstruktor default tanpa argumen diperlukan oleh Spring untuk deserialisasi JSON.
    public ProductRequest() {
    }

    // Konstruktor dengan semua field (opsional, berguna untuk pengujian unit atau saat membuat objek secara manual)
    public ProductRequest(String name, String description, BigDecimal price, Integer stock, String imageUrl, Long categoryId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    // --- Getter dan Setter ---
    // Spring akan menggunakan metode ini untuk mengisi objek dari JSON dan mengambil nilainya.
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
