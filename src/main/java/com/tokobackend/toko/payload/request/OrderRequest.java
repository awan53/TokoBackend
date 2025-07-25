package com.tokobackend.toko.payload.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class OrderRequest {

    @Valid
    @NotEmpty(message = "Daftar item pesanan tidak boleh kosong")
    @NotNull(message = "Daftar item pesanan tidak boleh null")
    private List<OrderItemRequest> items;

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }
}
