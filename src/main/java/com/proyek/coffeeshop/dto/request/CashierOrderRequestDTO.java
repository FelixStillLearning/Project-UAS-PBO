package com.proyek.coffeeshop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashierOrderRequestDTO {

    @NotEmpty(message = "Order items tidak boleh kosong")
    @Valid
    private List<CashierOrderItemRequestDTO> orderItems;

    @NotNull(message = "Metode pembayaran tidak boleh kosong")
    private String paymentMethodName; // Atau Long paymentMethodId

    private BigDecimal amountTendered; // Opsional, untuk pembayaran tunai

    private String customerNotes; // Opsional
}
