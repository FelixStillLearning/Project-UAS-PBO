package com.proyek.coffeeshop.dto.response;

import com.proyek.coffeeshop.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO untuk response data order.
 * Berisi informasi lengkap order termasuk detail items dan customer.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {

    private Long orderId;
    private CustomerDto customer;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private PaymentMethodDto paymentMethod;
    private String customerNotes;
    private List<OrderDetailResponseDto> orderDetails;
}
