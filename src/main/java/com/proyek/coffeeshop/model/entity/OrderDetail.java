package com.proyek.coffeeshop.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Entitas JPA untuk tabel OrderDetails.
 * Menyimpan detail item dalam pesanan termasuk produk, quantity, harga satuan, dan subtotal.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Entity
@Table(name = "OrderDetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Long detailId;

    /**
     * Relasi many-to-one dengan Order.
     * Banyak OrderDetail bisa dimiliki oleh satu Order.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @NotNull(message = "Order tidak boleh kosong")
    private Order order;

    /**
     * Relasi many-to-one dengan Product.
     * Banyak OrderDetail bisa menggunakan satu Product.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @NotNull(message = "Produk tidak boleh kosong")
    private Product product;

    @NotNull(message = "Quantity tidak boleh kosong")
    @Min(value = 1, message = "Quantity minimal adalah 1")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "Unit price tidak boleh kosong")
    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price harus lebih besar dari 0")
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @NotNull(message = "Subtotal price tidak boleh kosong")
    @DecimalMin(value = "0.0", inclusive = false, message = "Subtotal price harus lebih besar dari 0")
    @Column(name = "subtotal_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotalPrice;

    /**
     * Relasi one-to-many dengan OrderDetailCustomization.
     * Satu OrderDetail bisa memiliki banyak OrderDetailCustomization.
     */
    @OneToMany(mappedBy = "orderDetail", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderDetailCustomization> customizations;

    /**
     * Method untuk menghitung subtotal price berdasarkan quantity, unit price, dan customizations.
     */
    @PrePersist
    @PreUpdate
    private void calculateSubtotalPrice() {
        BigDecimal basePrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        
        if (customizations != null && !customizations.isEmpty()) {
            BigDecimal customizationTotal = customizations.stream()
                    .map(OrderDetailCustomization::getPriceAdjustmentSnapshot)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            this.subtotalPrice = basePrice.add(customizationTotal.multiply(BigDecimal.valueOf(quantity)));
        } else {
            this.subtotalPrice = basePrice;
        }
    }
}
