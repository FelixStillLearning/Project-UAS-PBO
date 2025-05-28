package com.proyek.coffeeshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponseDTO {

    private Long detailId; // Changed from orderDetailId to detailId to match OrderServiceImpl
    private ProductDto product; // Menggunakan ProductDto yang sudah ada
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotalPrice;
    private List<OrderDetailCustomizationResponseDto> customizations; // Changed to match expected type
}
