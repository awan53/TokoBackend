package com.tokobackend.toko.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public class CategoryRequest {
    @NotBlank(message = "nama kategori tidak boleh kosong")
    @Size(min = 3, max = 50, message = "nama kategori harus 3 dan 50 karater")
    private String name;

    @Size(max = 255, message = "Descripsi tidak boleh lebih 255 karater")
    private String description;

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
}
