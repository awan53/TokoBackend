package com.tokobackend.toko.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relasi Many-to-One dengan Order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false) // Foreign Key
    @JsonBackReference("orderItems") // Untuk menghindari loop rekursif pada JSON
    private Order order;

    // Relasi Many-to-One dengan Product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false) // Foreign Key
    @JsonBackReference("productOrderItems") // Untuk menghindari loop rekursif pada JSON
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity; // Quantity (> 0 akan ditangani di level service/controller)

    @Column(name = "price_at_order", precision = 10, scale = 2, nullable = false) // DECIMAL(10, 2)
    private BigDecimal priceAtOrder; // Harga produk saat order dibuat (> 0 akan ditangani di level service/controller)

    public OrderItem() {
    }

    public OrderItem(Order order, Product product, Integer quantity, BigDecimal priceAtOrder) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.priceAtOrder = priceAtOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPriceAtOrder() {
        return priceAtOrder;
    }

    public void setPriceAtOrder(BigDecimal priceAtOrder) {
        this.priceAtOrder = priceAtOrder;
    }
}
