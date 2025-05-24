package com.proyek.coffeeshop.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO untuk request pembuatan atau update kustomisasi.
 * Berisi informasi kustomisasi yang akan dibuat atau diupdate.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomizationRequestDto {

    @NotBlank(message = "Nama kustomisasi tidak boleh kosong")
    @Size(max = 100, message = "Nama kustomisasi maksimal 100 karakter")
    private String name;

    @Size(max = 50, message = "Tipe kustomisasi maksimal 50 karakter")
    private String type;

    @NotNull(message = "Price adjustment tidak boleh kosong")
    @DecimalMin(value = "0.0", message = "Price adjustment minimal 0")
    @Digits(integer = 6, fraction = 2, message = "Format price adjustment tidak valid")
    private BigDecimal priceAdjustment;

    @Size(max = 500, message = "Deskripsi kustomisasi maksimal 500 karakter")
    private String description;
}
