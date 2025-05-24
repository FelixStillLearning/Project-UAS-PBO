package com.proyek.coffeeshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO untuk response kustomisasi dalam order detail.
 * Berisi informasi kustomisasi yang dipilih dalam order.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailCustomizationResponseDto {

    private Long detailCustomizationId;
    private CustomizationDto customization;
    private String customizationNameSnapshot;
    private BigDecimal priceAdjustmentSnapshot;
}
