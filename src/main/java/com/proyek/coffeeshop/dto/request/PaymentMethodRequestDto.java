package com.proyek.coffeeshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO untuk request pembuatan atau update metode pembayaran.
 * Berisi informasi metode pembayaran yang akan dibuat atau diupdate.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodRequestDto {

    @NotBlank(message = "Tipe pembayaran tidak boleh kosong")
    @Size(max = 50, message = "Tipe pembayaran maksimal 50 karakter")
    private String type;
}
