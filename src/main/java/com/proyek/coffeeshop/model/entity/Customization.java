package com.proyek.coffeeshop.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Entitas JPA untuk tabel Customization.
 * Menyimpan kustomisasi yang tersedia seperti Extra Shot, Decaf, Sugar Level, dll.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Entity
@Table(name = "Customization")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customization_id")
    private Long customizationId;

    @NotBlank(message = "Nama kustomisasi tidak boleh kosong")
    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 50)
    private String type;

    @NotNull(message = "Price adjustment tidak boleh kosong")
    @Column(name = "price_adjustment", nullable = false, precision = 8, scale = 2)
    private BigDecimal priceAdjustment;

    @Column(columnDefinition = "TEXT")
    private String description;
}
