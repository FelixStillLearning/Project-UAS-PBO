package com.proyek.coffeeshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO untuk response detail item dalam order.
 * Berisi informasi produk, quantity, harga, dan kustomisasi.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponseDto {

    private Long detailId;
    private ProductDto product;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotalPrice;
    private List<OrderDetailCustomizationResponseDto> customizations;
}
