package com.proyek.coffeeshop.model.entity;

import com.proyek.coffeeshop.model.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entitas JPA untuk tabel Orders.
 * Menyimpan informasi pesanan customer termasuk tanggal, total, status, dan metode pembayaran.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Entity
@Table(name = "Orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    /**
     * Relasi many-to-one dengan Customer.
     * Banyak Order bisa dimiliki oleh satu Customer.
     * Diubah menjadi nullable untuk mengakomodasi walk-in customer.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = true) // Diubah menjadi nullable = true
    private Customer customer;

    /**
     * Relasi many-to-one dengan User (Kasir).
     * Untuk mencatat kasir yang memproses pesanan.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processed_by_kasir_id", nullable = true)
    private User processedByKasir;

    @NotNull(message = "Tanggal order tidak boleh kosong")
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @NotNull(message = "Total amount tidak boleh kosong")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total amount harus lebih besar dari 0")
    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @NotNull(message = "Status tidak boleh kosong")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    /**
     * Relasi many-to-one dengan PaymentMethod.
     * Banyak Order bisa menggunakan satu PaymentMethod.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    @NotNull(message = "Metode pembayaran tidak boleh kosong")
    private PaymentMethod paymentMethod;

    /**
     * Relasi one-to-many dengan OrderDetail.
     * Satu Order bisa memiliki banyak OrderDetail.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails;

    @Column(name = "customer_notes", length = 500)
    private String customerNotes;

    // Tambahan untuk pembayaran tunai oleh kasir
    @Column(name = "amount_tendered", precision = 12, scale = 2)
    private BigDecimal amountTendered;

    @Column(name = "change_given", precision = 12, scale = 2)
    private BigDecimal changeGiven;

    /**
     * Method untuk menghitung total amount dari semua order details.
     */
    @PrePersist
    @PreUpdate
    private void calculateTotalAmount() {
        if (orderDetails != null && !orderDetails.isEmpty()) {
            this.totalAmount = orderDetails.stream()
                    .map(OrderDetail::getSubtotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }
}
