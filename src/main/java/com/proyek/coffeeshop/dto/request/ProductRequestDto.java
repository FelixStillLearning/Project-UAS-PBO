package com.proyek.coffeeshop.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO untuk request pembuatan atau update produk.
 * Berisi informasi produk yang akan dibuat atau diupdate.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    @NotBlank(message = "Nama produk tidak boleh kosong")
    @Size(max = 100, message = "Nama produk maksimal 100 karakter")
    private String name;

    @Size(max = 500, message = "Deskripsi produk maksimal 500 karakter")
    private String description;

    @NotNull(message = "Harga tidak boleh kosong")
    @DecimalMin(value = "0.0", inclusive = false, message = "Harga harus lebih besar dari 0")
    @Digits(integer = 8, fraction = 2, message = "Format harga tidak valid")
    private BigDecimal price;

    @Size(max = 255, message = "URL gambar maksimal 255 karakter")
    private String imageUrl;

    @NotNull(message = "ID kategori tidak boleh kosong")
    private Long categoryId;
}
