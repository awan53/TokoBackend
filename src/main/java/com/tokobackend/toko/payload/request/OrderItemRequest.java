package com.tokobackend.toko.payload.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderItemRequest {

    @NotNull(message = "Id product tidak boleh kosong")
    private Long ProductId;

    @NotNull(message = "Kuantitas tidak boleh kosong")
    @Positive(message = "Kuantitas harus lebidah dari nol")
    private Integer quantity;

    public Long getProductId() {
        return ProductId;
    }

    public void setProductId(Long productId) {
        ProductId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
