package com.proyek.coffeeshop.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Entitas JPA untuk tabel Products.
 * Menyimpan informasi produk coffee shop seperti nama, deskripsi, harga, dan gambar.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Entity
@Table(name = "Products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @NotBlank(message = "Nama produk tidak boleh kosong")
    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Harga tidak boleh kosong")
    @DecimalMin(value = "0.0", inclusive = false, message = "Harga harus lebih besar dari 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "img_url", length = 255)
    private String imageUrl;

    /**
     * Relasi many-to-one dengan Category.
     * Banyak Product bisa memiliki satu Category.
     */    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull(message = "Kategori tidak boleh kosong")
    private Category category;

    @Column(nullable = false)
    private boolean available = true; // Default value true

    /**
     * Stock management fields
     */
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity = 0; // Current stock quantity

    @Column(name = "min_stock_level", nullable = false)
    private Integer minStockLevel = 5; // Minimum stock threshold

    @Column(name = "max_stock_level", nullable = false)
    private Integer maxStockLevel = 100; // Maximum stock capacity

    /**
     * Helper method to check if stock is low
     */
    public boolean isLowStock() {
        return stockQuantity <= minStockLevel;
    }

    /**
     * Helper method to check if product is in stock
     */
    public boolean isInStock() {
        return available && stockQuantity > 0;
    }

    /**
     * Method to reduce stock when order is placed
     */
    public void reduceStock(int quantity) {
        if (stockQuantity >= quantity) {
            stockQuantity -= quantity;
            if (stockQuantity <= 0) {
                available = false;
            }
        } else {
            throw new IllegalArgumentException("Insufficient stock. Available: " + stockQuantity + ", Required: " + quantity);
        }
    }

    /**
     * Method to add stock
     */
    public void addStock(int quantity) {
        stockQuantity += quantity;
        if (stockQuantity > 0) {
            available = true;
        }
    }
}
