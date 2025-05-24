package com.proyek.coffeeshop.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO untuk request item dalam order.
 * Berisi informasi produk, quantity, dan kustomisasi yang dipilih.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailRequestDto {

    @NotNull(message = "ID produk tidak boleh kosong")
    private Long productId;

    @NotNull(message = "Quantity tidak boleh kosong")
    @Min(value = 1, message = "Quantity minimal 1")
    private Integer quantity;

    @Valid
    private List<OrderDetailCustomizationRequestDto> customizations;
}
