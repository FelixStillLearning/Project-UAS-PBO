package com.proyek.coffeeshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO untuk response stock information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockInfoResponseDTO {

    private Long productId;
    private String productName;
    private Integer stockQuantity;
    private Integer minStockLevel;
    private Integer maxStockLevel;
    private boolean isLowStock;
    private boolean isInStock;
    private boolean available;
    private BigDecimal price;
    private String categoryName;
}
