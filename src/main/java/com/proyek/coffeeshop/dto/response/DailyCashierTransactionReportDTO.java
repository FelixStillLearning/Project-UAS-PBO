package com.proyek.coffeeshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyCashierTransactionReportDTO {

    private LocalDate date;
    private String kasirUsername;
    private Integer totalTransactions;
    private BigDecimal totalSales;
    private BigDecimal totalCashPayments;
    private BigDecimal totalNonCashPayments;
    private List<CashierOrderResponseDTO> transactions; // Opsional, untuk detail
}
