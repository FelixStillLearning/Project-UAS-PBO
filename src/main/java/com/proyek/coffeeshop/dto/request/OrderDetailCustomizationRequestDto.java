package com.proyek.coffeeshop.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO untuk request kustomisasi dalam order detail.
 * Berisi ID kustomisasi yang dipilih customer.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailCustomizationRequestDto {

    @NotNull(message = "ID kustomisasi tidak boleh kosong")
    private Long customizationId;
}
