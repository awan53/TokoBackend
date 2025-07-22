package com.tokobackend.toko.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", precision = 10, scale = 2, nullable = false) // DECIMAL(10, 2)
    private BigDecimal price;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "img_url")
    private String imgUrl;

    // Relasi Many-to-One dengan Category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false) // Foreign Key
    @JsonBackReference // Untuk menghindari loop rekursif pada JSON
    private Category category;

    // Relasi One-to-Many dengan CartItem
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    // @JsonManagedReference // Aktifkan jika ingin menyertakan daftar cart items saat mengambil produk
    private List<CartItem> cartItems = new ArrayList<>();

    // Relasi One-to-Many dengan OrderItem
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    // @JsonManagedReference // Aktifkan jika ingin menyertakan daftar order items saat mengambil produk
    private List<OrderItem> orderItems = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Product(){}

    public Product(String name, String description, BigDecimal price, Integer stock, String imgUrl, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imgUrl = imgUrl;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void addCartItem(CartItem cartItem) {
        this.cartItems.add(cartItem);
        cartItem.setProduct(this);
    }
    public void removeCartItem(CartItem cartItem) {
        this.cartItems.remove(cartItem);
        cartItem.setProduct(null);
    }
    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setProduct(this);
    }
    public void removeOrderItem(OrderItem orderItem) {
        this.orderItems.remove(orderItem);
        orderItem.setProduct(null);
    }
}
