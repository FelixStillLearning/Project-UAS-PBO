package com.proyek.coffeeshop.service;

import com.proyek.coffeeshop.dto.response.DailyKasirReportDTO;
import com.proyek.coffeeshop.dto.response.ProductSalesReportDTO;
import com.proyek.coffeeshop.dto.response.TransactionReportDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for generating various reports in the coffee shop system.
 * Provides methods for transaction reports, product sales reports, and kasir (cashier) reports.
 */
public interface ReportService {

    /**
     * Generate a daily kasir (cashier) report for a specific user and date.
     * 
     * @param kasirId The ID of the kasir (cashier)
     * @param date The date for which to generate the report
     * @return DailyKasirReportDTO containing kasir performance data
     */
    DailyKasirReportDTO generateDailyKasirReport(Long kasirId, LocalDate date);

    /**
     * Generate a transaction report for a specific date range.
     * 
     * @param startDate The start date of the report period
     * @param endDate The end date of the report period
     * @return TransactionReportDTO containing transaction statistics
     */
    TransactionReportDTO generateTransactionReport(LocalDate startDate, LocalDate endDate);

    /**
     * Generate a transaction report for a specific date.
     * 
     * @param date The date for which to generate the report
     * @return TransactionReportDTO containing transaction statistics
     */
    TransactionReportDTO generateDailyTransactionReport(LocalDate date);

    /**
     * Generate product sales report for a specific date range.
     * 
     * @param startDate The start date of the report period
     * @param endDate The end date of the report period
     * @return List of ProductSalesReportDTO containing product sales data
     */
    List<ProductSalesReportDTO> generateProductSalesReport(LocalDate startDate, LocalDate endDate);

    /**
     * Generate product sales report for a specific date.
     * 
     * @param date The date for which to generate the report
     * @return List of ProductSalesReportDTO containing product sales data
     */
    List<ProductSalesReportDTO> generateDailyProductSalesReport(LocalDate date);

    /**
     * Generate top selling products report for a specific date range.
     * 
     * @param startDate The start date of the report period
     * @param endDate The end date of the report period
     * @param limit The maximum number of products to include in the report
     * @return List of ProductSalesReportDTO containing top selling products
     */
    List<ProductSalesReportDTO> generateTopSellingProductsReport(LocalDate startDate, LocalDate endDate, int limit);

    /**
     * Generate low stock products report.
     * 
     * @return List of ProductSalesReportDTO containing products with low stock
     */
    List<ProductSalesReportDTO> generateLowStockReport();
}
