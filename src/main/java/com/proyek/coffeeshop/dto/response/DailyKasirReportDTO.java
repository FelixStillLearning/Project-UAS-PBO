package com.proyek.coffeeshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO untuk laporan transaksi harian kasir
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyKasirReportDTO {

    private Long kasirId;
    private String kasirUsername;
    private String kasirName;
    private LocalDate reportDate;
    private Integer totalOrders;
    private BigDecimal totalRevenue;
    private BigDecimal averageOrderValue;
    private Integer totalItemsSold;    private String mostPopularProduct;
    private LocalDateTime generatedAt;
}
