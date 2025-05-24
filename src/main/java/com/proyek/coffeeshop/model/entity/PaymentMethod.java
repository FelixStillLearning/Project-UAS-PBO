package com.proyek.coffeeshop.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entitas JPA untuk tabel PaymentMethods.
 * Menyimpan metode pembayaran seperti Cash, Credit Card, E-Wallet, dll.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Entity
@Table(name = "PaymentMethods")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @NotBlank(message = "Tipe pembayaran tidak boleh kosong")
    @Column(unique = true, nullable = false, length = 50)
    private String type;
}
