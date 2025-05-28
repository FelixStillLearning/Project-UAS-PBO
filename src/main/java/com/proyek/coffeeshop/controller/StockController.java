package com.proyek.coffeeshop.controller;

import com.proyek.coffeeshop.dto.request.StockUpdateRequestDTO;
import com.proyek.coffeeshop.dto.response.StockInfoResponseDTO;
import com.proyek.coffeeshop.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller untuk Stock Management
 * Mengelola operasi stock produk coffee shop
 */
@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
@Slf4j
public class StockController {

    private final StockService stockService;

    /**
     * Update stock produk
     * Hanya Admin yang bisa update stock
     */
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StockInfoResponseDTO> updateStock(
            @Valid @RequestBody StockUpdateRequestDTO request) {
        
        log.info("Request to update stock for product ID: {}", request.getProductId());
        StockInfoResponseDTO response = stockService.updateStock(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get stock information untuk produk tertentu
     * Admin dan Kasir bisa akses
     */
    @GetMapping("/product/{productId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('KASIR')")
    public ResponseEntity<StockInfoResponseDTO> getStockInfo(@PathVariable Long productId) {
        
        log.info("Request to get stock info for product ID: {}", productId);
        StockInfoResponseDTO response = stockService.getStockInfo(productId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get semua produk dengan low stock
     * Admin dan Kasir bisa akses
     */
    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('KASIR')")
    public ResponseEntity<List<StockInfoResponseDTO>> getLowStockProducts() {
        
        log.info("Request to get low stock products");
        List<StockInfoResponseDTO> response = stockService.getLowStockProducts();
        return ResponseEntity.ok(response);
    }

    /**
     * Get semua stock information
     * Hanya Admin yang bisa akses semua stock info
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StockInfoResponseDTO>> getAllStockInfo() {
        
        log.info("Request to get all stock information");
        List<StockInfoResponseDTO> response = stockService.getAllStockInfo();
        return ResponseEntity.ok(response);
    }

    /**
     * Add stock untuk produk tertentu
     * Hanya Admin yang bisa add stock
     */
    @PostMapping("/add/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StockInfoResponseDTO> addStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity,
            @RequestParam(required = false, defaultValue = "Manual stock addition") String reason) {
        
        log.info("Request to add stock for product ID: {} - Quantity: {}", productId, quantity);
        StockInfoResponseDTO response = stockService.addStock(productId, quantity, reason);
        return ResponseEntity.ok(response);
    }
}
