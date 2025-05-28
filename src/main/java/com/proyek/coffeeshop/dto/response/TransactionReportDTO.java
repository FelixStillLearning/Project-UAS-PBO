package com.proyek.coffeeshop.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TransactionReportDTO {
    private LocalDate reportDate;
    private int totalTransactions;
    private BigDecimal totalRevenue;
    private BigDecimal averageOrderValue;
    private int totalItemsSold;
    private String mostPopularProduct;
    private String paymentMethodBreakdown;
    private LocalDateTime generatedAt;

    // Default constructor
    public TransactionReportDTO() {
        this.generatedAt = LocalDateTime.now();
    }

    // Constructor with parameters
    public TransactionReportDTO(LocalDate reportDate, int totalTransactions, BigDecimal totalRevenue,
                               BigDecimal averageOrderValue, int totalItemsSold, String mostPopularProduct,
                               String paymentMethodBreakdown) {
        this.reportDate = reportDate;
        this.totalTransactions = totalTransactions;
        this.totalRevenue = totalRevenue;
        this.averageOrderValue = averageOrderValue;
        this.totalItemsSold = totalItemsSold;
        this.mostPopularProduct = mostPopularProduct;
        this.paymentMethodBreakdown = paymentMethodBreakdown;
        this.generatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public int getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(int totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getAverageOrderValue() {
        return averageOrderValue;
    }

    public void setAverageOrderValue(BigDecimal averageOrderValue) {
        this.averageOrderValue = averageOrderValue;
    }

    public int getTotalItemsSold() {
        return totalItemsSold;
    }

    public void setTotalItemsSold(int totalItemsSold) {
        this.totalItemsSold = totalItemsSold;
    }

    public String getMostPopularProduct() {
        return mostPopularProduct;
    }

    public void setMostPopularProduct(String mostPopularProduct) {
        this.mostPopularProduct = mostPopularProduct;
    }

    public String getPaymentMethodBreakdown() {
        return paymentMethodBreakdown;
    }

    public void setPaymentMethodBreakdown(String paymentMethodBreakdown) {
        this.paymentMethodBreakdown = paymentMethodBreakdown;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    @Override
    public String toString() {
        return "TransactionReportDTO{" +
                "reportDate=" + reportDate +
                ", totalTransactions=" + totalTransactions +
                ", totalRevenue=" + totalRevenue +
                ", averageOrderValue=" + averageOrderValue +
                ", totalItemsSold=" + totalItemsSold +
                ", mostPopularProduct='" + mostPopularProduct + '\'' +
                ", paymentMethodBreakdown='" + paymentMethodBreakdown + '\'' +
                ", generatedAt=" + generatedAt +
                '}';
    }
}
