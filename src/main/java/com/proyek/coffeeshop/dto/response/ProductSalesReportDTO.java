package com.proyek.coffeeshop.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProductSalesReportDTO {
    private Long productId;
    private String productName;
    private String category;
    private int quantitySold;
    private BigDecimal unitPrice;
    private BigDecimal totalRevenue;
    private int stockRemaining;
    private boolean isLowStock;
    private LocalDate reportDate;
    private LocalDateTime generatedAt;

    // Default constructor
    public ProductSalesReportDTO() {
        this.generatedAt = LocalDateTime.now();
    }

    // Constructor with parameters
    public ProductSalesReportDTO(Long productId, String productName, String category,
                                int quantitySold, BigDecimal unitPrice, BigDecimal totalRevenue,
                                int stockRemaining, boolean isLowStock, LocalDate reportDate) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.quantitySold = quantitySold;
        this.unitPrice = unitPrice;
        this.totalRevenue = totalRevenue;
        this.stockRemaining = stockRemaining;
        this.isLowStock = isLowStock;
        this.reportDate = reportDate;
        this.generatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public int getStockRemaining() {
        return stockRemaining;
    }

    public void setStockRemaining(int stockRemaining) {
        this.stockRemaining = stockRemaining;
    }

    public boolean isLowStock() {
        return isLowStock;
    }

    public void setLowStock(boolean lowStock) {
        isLowStock = lowStock;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    @Override
    public String toString() {
        return "ProductSalesReportDTO{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", category='" + category + '\'' +
                ", quantitySold=" + quantitySold +
                ", unitPrice=" + unitPrice +
                ", totalRevenue=" + totalRevenue +
                ", stockRemaining=" + stockRemaining +
                ", isLowStock=" + isLowStock +
                ", reportDate=" + reportDate +
                ", generatedAt=" + generatedAt +
                '}';
    }
}
