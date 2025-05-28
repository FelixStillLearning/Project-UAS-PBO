package com.proyek.coffeeshop.service;

import com.proyek.coffeeshop.dto.request.StockUpdateRequestDTO;
import com.proyek.coffeeshop.dto.response.StockInfoResponseDTO;

import java.util.List;

/**
 * Service interface untuk stock management
 */
public interface StockService {

    /**
     * Update stock produk
     */
    StockInfoResponseDTO updateStock(StockUpdateRequestDTO request);

    /**
     * Get stock information untuk produk tertentu
     */
    StockInfoResponseDTO getStockInfo(Long productId);

    /**
     * Get semua produk dengan low stock
     */
    List<StockInfoResponseDTO> getLowStockProducts();

    /**
     * Get semua stock information
     */
    List<StockInfoResponseDTO> getAllStockInfo();

    /**
     * Add stock untuk produk tertentu
     */
    StockInfoResponseDTO addStock(Long productId, Integer quantity, String reason);

    /**
     * Reduce stock untuk produk tertentu (untuk order processing)
     */
    void reduceStock(Long productId, Integer quantity);
}
