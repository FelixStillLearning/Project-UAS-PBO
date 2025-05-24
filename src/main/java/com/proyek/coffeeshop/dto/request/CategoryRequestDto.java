package com.proyek.coffeeshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO untuk request pembuatan atau update kategori.
 * Berisi informasi kategori yang akan dibuat atau diupdate.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDto {

    @NotBlank(message = "Nama kategori tidak boleh kosong")
    @Size(max = 50, message = "Nama kategori maksimal 50 karakter")
    private String name;
}
