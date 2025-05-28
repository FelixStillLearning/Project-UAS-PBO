package com.proyek.coffeeshop.controller;

import com.proyek.coffeeshop.dto.response.ApiResponse;
import com.proyek.coffeeshop.dto.response.DailyKasirReportDTO;
import com.proyek.coffeeshop.dto.response.ProductSalesReportDTO;
import com.proyek.coffeeshop.dto.response.TransactionReportDTO;
import com.proyek.coffeeshop.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller for handling report-related operations.
 * Provides endpoints for generating various types of reports including
 * kasir reports, transaction reports, and product sales reports.
 */
@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * Generate daily kasir (cashier) report
     * 
     * @param kasirId The ID of the kasir
     * @param date The date for the report (optional, defaults to today)
     * @return Daily kasir report data
     */
    @GetMapping("/kasir/daily")
    @PreAuthorize("hasRole('ADMIN') or hasRole('KASIR')")
    public ResponseEntity<ApiResponse<DailyKasirReportDTO>> getDailyKasirReport(
            @RequestParam Long kasirId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        try {
            if (date == null) {
                date = LocalDate.now();
            }
            
            DailyKasirReportDTO report = reportService.generateDailyKasirReport(kasirId, date);
            
            ApiResponse<DailyKasirReportDTO> response = new ApiResponse<>(
                true, "Daily kasir report generated successfully", report);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<DailyKasirReportDTO> response = new ApiResponse<>(
                false, "Failed to generate kasir report: " + e.getMessage(), null);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Generate transaction report for a specific date range
     * 
     * @param startDate Start date of the report period
     * @param endDate End date of the report period
     * @return Transaction report data
     */
    @GetMapping("/transactions")
    @PreAuthorize("hasRole('ADMIN') or hasRole('KASIR')")
    public ResponseEntity<ApiResponse<TransactionReportDTO>> getTransactionReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        try {
            TransactionReportDTO report = reportService.generateTransactionReport(startDate, endDate);
            
            ApiResponse<TransactionReportDTO> response = new ApiResponse<>(
                true, "Transaction report generated successfully", report);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<TransactionReportDTO> response = new ApiResponse<>(
                false, "Failed to generate transaction report: " + e.getMessage(), null);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Generate daily transaction report
     * 
     * @param date The date for the report (optional, defaults to today)
     * @return Daily transaction report data
     */
    @GetMapping("/transactions/daily")
    @PreAuthorize("hasRole('ADMIN') or hasRole('KASIR')")
    public ResponseEntity<ApiResponse<TransactionReportDTO>> getDailyTransactionReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        try {
            if (date == null) {
                date = LocalDate.now();
            }
            
            TransactionReportDTO report = reportService.generateDailyTransactionReport(date);
            
            ApiResponse<TransactionReportDTO> response = new ApiResponse<>(
                true, "Daily transaction report generated successfully", report);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<TransactionReportDTO> response = new ApiResponse<>(
                false, "Failed to generate daily transaction report: " + e.getMessage(), null);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Generate product sales report for a specific date range
     * 
     * @param startDate Start date of the report period
     * @param endDate End date of the report period
     * @return List of product sales data
     */
    @GetMapping("/products/sales")
    @PreAuthorize("hasRole('ADMIN') or hasRole('KASIR')")
    public ResponseEntity<ApiResponse<List<ProductSalesReportDTO>>> getProductSalesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        try {
            List<ProductSalesReportDTO> report = reportService.generateProductSalesReport(startDate, endDate);
            
            ApiResponse<List<ProductSalesReportDTO>> response = new ApiResponse<>(
                true, "Product sales report generated successfully", report);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<ProductSalesReportDTO>> response = new ApiResponse<>(
                false, "Failed to generate product sales report: " + e.getMessage(), null);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Generate daily product sales report
     * 
     * @param date The date for the report (optional, defaults to today)
     * @return List of daily product sales data
     */
    @GetMapping("/products/sales/daily")
    @PreAuthorize("hasRole('ADMIN') or hasRole('KASIR')")
    public ResponseEntity<ApiResponse<List<ProductSalesReportDTO>>> getDailyProductSalesReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        try {
            if (date == null) {
                date = LocalDate.now();
            }
            
            List<ProductSalesReportDTO> report = reportService.generateDailyProductSalesReport(date);
            
            ApiResponse<List<ProductSalesReportDTO>> response = new ApiResponse<>(
                true, "Daily product sales report generated successfully", report);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<ProductSalesReportDTO>> response = new ApiResponse<>(
                false, "Failed to generate daily product sales report: " + e.getMessage(), null);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Generate top selling products report
     * 
     * @param startDate Start date of the report period
     * @param endDate End date of the report period
     * @param limit Maximum number of products to include (optional, defaults to 10)
     * @return List of top selling products
     */
    @GetMapping("/products/top-selling")
    @PreAuthorize("hasRole('ADMIN') or hasRole('KASIR')")
    public ResponseEntity<ApiResponse<List<ProductSalesReportDTO>>> getTopSellingProductsReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "10") int limit) {
        
        try {
            List<ProductSalesReportDTO> report = reportService.generateTopSellingProductsReport(startDate, endDate, limit);
            
            ApiResponse<List<ProductSalesReportDTO>> response = new ApiResponse<>(
                true, "Top selling products report generated successfully", report);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<ProductSalesReportDTO>> response = new ApiResponse<>(
                false, "Failed to generate top selling products report: " + e.getMessage(), null);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Generate low stock products report
     * 
     * @return List of products with low stock
     */
    @GetMapping("/products/low-stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('KASIR')")
    public ResponseEntity<ApiResponse<List<ProductSalesReportDTO>>> getLowStockReport() {
        try {
            List<ProductSalesReportDTO> report = reportService.generateLowStockReport();
            
            ApiResponse<List<ProductSalesReportDTO>> response = new ApiResponse<>(
                true, "Low stock report generated successfully", report);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<ProductSalesReportDTO>> response = new ApiResponse<>(
                false, "Failed to generate low stock report: " + e.getMessage(), null);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
