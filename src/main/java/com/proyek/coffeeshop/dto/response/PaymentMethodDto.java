package com.proyek.coffeeshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO untuk response data metode pembayaran.
 * Berisi informasi metode pembayaran yang tersedia.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDto {

    private Long paymentId;
    private String type;
}
