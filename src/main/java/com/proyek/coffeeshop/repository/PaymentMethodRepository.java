package com.proyek.coffeeshop.repository;

import com.proyek.coffeeshop.model.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface untuk entitas PaymentMethod.
 * Menyediakan operasi CRUD dan query custom untuk PaymentMethod.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    /**
     * Mencari payment method berdasarkan nama.
     *
     * @param name nama payment method yang dicari
     * @return Optional PaymentMethod jika ditemukan
     */
    Optional<PaymentMethod> findByName(String name);

    /**
     * Mengecek apakah nama payment method sudah ada.
     *
     * @param name nama payment method yang dicek
     * @return true jika nama sudah ada
     */
    boolean existsByName(String name);
}
