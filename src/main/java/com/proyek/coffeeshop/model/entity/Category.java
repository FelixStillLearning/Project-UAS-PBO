package com.proyek.coffeeshop.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entitas JPA untuk tabel Categories.
 * Menyimpan kategori produk seperti Coffee, Tea, Pastry, dll.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Entity
@Table(name = "Categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @NotBlank(message = "Nama kategori tidak boleh kosong")
    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @Column(length = 255) // Tambahkan field description
    private String description;

    /**
     * Relasi one-to-many dengan Product.
     * Satu Category bisa memiliki banyak Product.
     */
    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Product> products;
}
