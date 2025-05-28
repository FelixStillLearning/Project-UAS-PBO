package com.proyek.coffeeshop.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO untuk update stock produk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockUpdateRequestDTO {

    @NotNull(message = "Product ID tidak boleh kosong")
    private Long productId;

    @NotNull(message = "Stock quantity tidak boleh kosong")
    @Min(value = 0, message = "Stock quantity tidak boleh negatif")
    private Integer stockQuantity;

    @Min(value = 1, message = "Minimum stock level harus minimal 1")
    private Integer minStockLevel = 5;

    @Min(value = 1, message = "Maximum stock level harus minimal 1")
    private Integer maxStockLevel = 100;

    private String updateReason; // Alasan update stock
}
