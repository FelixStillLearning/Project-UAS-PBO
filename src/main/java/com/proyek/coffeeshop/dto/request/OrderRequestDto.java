package com.proyek.coffeeshop.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO untuk request pembuatan order baru.
 * Berisi daftar item yang dipesan, metode pembayaran, dan catatan customer.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

    @NotEmpty(message = "Daftar item tidak boleh kosong")
    @Valid
    private List<OrderDetailRequestDto> items;

    @NotNull(message = "ID metode pembayaran tidak boleh kosong")
    private Long paymentMethodId;

    @Size(max = 500, message = "Catatan customer maksimal 500 karakter")
    private String customerNotes;
}
