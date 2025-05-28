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
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull(message = "Kategori tidak boleh kosong")
    private Category category;

    @Column(nullable = false) // Tambahkan field available
    private boolean available = true; // Default value true
}
