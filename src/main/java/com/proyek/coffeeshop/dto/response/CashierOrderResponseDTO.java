package com.proyek.coffeeshop.dto.response;

import com.proyek.coffeeshop.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashierOrderResponseDTO {

    private Long orderId;
    private LocalDateTime orderDate;
    private String kasirUsername;
    private List<OrderItemResponseDTO> orderItems; // Anda mungkin perlu membuat OrderItemResponseDTO jika belum ada
    private BigDecimal totalPrice;
    private String paymentMethodName;
    private OrderStatus status;
    private BigDecimal amountTendered;
    private BigDecimal changeGiven;
    private String customerNotes;
}
