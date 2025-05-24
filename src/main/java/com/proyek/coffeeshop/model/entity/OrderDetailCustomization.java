package com.proyek.coffeeshop.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Entitas JPA untuk tabel OrderDetailsCustomization.
 * Menyimpan kustomisasi yang dipilih untuk setiap OrderDetail.
 * Menyimpan snapshot nama dan harga kustomisasi untuk keperluan arsip.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Entity
@Table(name = "OrderDetailsCustomization")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailCustomization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_customization_id")
    private Long detailCustomizationId;

    /**
     * Relasi many-to-one dengan OrderDetail.
     * Banyak OrderDetailCustomization bisa dimiliki oleh satu OrderDetail.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detail_id", nullable = false)
    @NotNull(message = "Order detail tidak boleh kosong")
    private OrderDetail orderDetail;

    /**
     * Relasi many-to-one dengan Customization.
     * Banyak OrderDetailCustomization bisa menggunakan satu Customization.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customization_id", nullable = false)
    @NotNull(message = "Kustomisasi tidak boleh kosong")
    private Customization customization;

    /**
     * Snapshot nama kustomisasi saat order dibuat.
     * Digunakan untuk keperluan arsip jika nama kustomisasi berubah di masa depan.
     */
    @Column(name = "customization_name_snapshot", length = 100)
    private String customizationNameSnapshot;

    /**
     * Snapshot harga adjustment kustomisasi saat order dibuat.
     * Digunakan untuk keperluan arsip jika harga kustomisasi berubah di masa depan.
     */
    @Column(name = "price_adjustment_snapshot", precision = 8, scale = 2)
    private BigDecimal priceAdjustmentSnapshot;

    /**
     * Method untuk membuat snapshot dari data kustomisasi saat order dibuat.
     */
    @PrePersist
    private void createSnapshot() {
        if (customization != null) {
            this.customizationNameSnapshot = customization.getName();
            this.priceAdjustmentSnapshot = customization.getPriceAdjustment();
        }
    }
}
