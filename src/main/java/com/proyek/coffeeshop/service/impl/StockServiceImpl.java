package com.proyek.coffeeshop.service.impl;

import com.proyek.coffeeshop.dto.request.StockUpdateRequestDTO;
import com.proyek.coffeeshop.dto.response.StockInfoResponseDTO;
import com.proyek.coffeeshop.exception.BadRequestException;
import com.proyek.coffeeshop.model.entity.Product;
import com.proyek.coffeeshop.repository.ProductRepository;
import com.proyek.coffeeshop.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation untuk Stock Management Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StockServiceImpl implements StockService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public StockInfoResponseDTO updateStock(StockUpdateRequestDTO request) {
        log.info("Updating stock for product ID: {}", request.getProductId());

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new BadRequestException("Product dengan ID " + request.getProductId() + " tidak ditemukan"));

        // Update stock information
        product.setStockQuantity(request.getStockQuantity());
        product.setMinStockLevel(request.getMinStockLevel());
        product.setMaxStockLevel(request.getMaxStockLevel());

        // Update availability based on stock
        product.setAvailable(product.isInStock());

        Product savedProduct = productRepository.save(product);
        log.info("Stock updated successfully for product: {} - New quantity: {}", 
                savedProduct.getName(), savedProduct.getStockQuantity());

        return mapToStockInfoDTO(savedProduct);
    }

    @Override
    public StockInfoResponseDTO getStockInfo(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException("Product dengan ID " + productId + " tidak ditemukan"));

        return mapToStockInfoDTO(product);
    }

    @Override
    public List<StockInfoResponseDTO> getLowStockProducts() {
        log.info("Fetching low stock products");
        
        List<Product> lowStockProducts = productRepository.findAll()
                .stream()
                .filter(Product::isLowStock)
                .collect(Collectors.toList());

        return lowStockProducts.stream()
                .map(this::mapToStockInfoDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockInfoResponseDTO> getAllStockInfo() {
        log.info("Fetching all stock information");
        
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::mapToStockInfoDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StockInfoResponseDTO addStock(Long productId, Integer quantity, String reason) {
        log.info("Adding stock for product ID: {} - Quantity: {} - Reason: {}", 
                productId, quantity, reason);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException("Product dengan ID " + productId + " tidak ditemukan"));

        product.addStock(quantity);
        Product savedProduct = productRepository.save(product);

        log.info("Stock added successfully for product: {} - New quantity: {}", 
                savedProduct.getName(), savedProduct.getStockQuantity());

        return mapToStockInfoDTO(savedProduct);
    }

    @Override
    @Transactional
    public void reduceStock(Long productId, Integer quantity) {
        log.info("Reducing stock for product ID: {} - Quantity: {}", productId, quantity);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException("Product dengan ID " + productId + " tidak ditemukan"));

        try {
            product.reduceStock(quantity);
            productRepository.save(product);
            
            log.info("Stock reduced successfully for product: {} - Remaining quantity: {}", 
                    product.getName(), product.getStockQuantity());
        } catch (IllegalArgumentException e) {
            log.error("Failed to reduce stock for product: {} - Error: {}", 
                    product.getName(), e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }

    /**
     * Helper method untuk mapping Product ke StockInfoResponseDTO
     */
    private StockInfoResponseDTO mapToStockInfoDTO(Product product) {
        return StockInfoResponseDTO.builder()
                .productId(product.getProductId())
                .productName(product.getName())
                .stockQuantity(product.getStockQuantity())
                .minStockLevel(product.getMinStockLevel())
                .maxStockLevel(product.getMaxStockLevel())
                .isLowStock(product.isLowStock())
                .isInStock(product.isInStock())
                .available(product.isAvailable())
                .price(product.getPrice())
                .categoryName(product.getCategory().getName())
                .build();
    }
}
