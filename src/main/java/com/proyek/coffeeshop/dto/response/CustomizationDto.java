package com.proyek.coffeeshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO untuk response data kustomisasi.
 * Berisi informasi kustomisasi yang tersedia.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomizationDto {

    private Long customizationId;
    private String name;
    private String type;
    private BigDecimal priceAdjustment;
    private String description;
}
