package com.proyek.coffeeshop.service;

import com.proyek.coffeeshop.dto.response.PaymentMethodDto;
import com.proyek.coffeeshop.model.entity.PaymentMethod;

import java.util.List;

/**
 * Service interface untuk operasi metode pembayaran.
 * Menangani CRUD metode pembayaran.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
public interface PaymentMethodService {

    /**
     * Mendapatkan semua metode pembayaran.
     *
     * @return List PaymentMethodDto semua metode pembayaran
     */
    List<PaymentMethodDto> getAllPaymentMethods();

    /**
     * Mendapatkan metode pembayaran berdasarkan ID.
     *
     * @param id ID metode pembayaran
     * @return PaymentMethodDto data metode pembayaran
     */
    PaymentMethodDto getPaymentMethodById(Long id);

    /**
     * Membuat metode pembayaran baru.
     *
     * @param paymentMethodDto data metode pembayaran baru
     * @return PaymentMethodDto metode pembayaran yang telah dibuat
     */
    PaymentMethodDto createPaymentMethod(PaymentMethodDto paymentMethodDto);

    /**
     * Update metode pembayaran.
     *
     * @param id ID metode pembayaran yang akan diupdate
     * @param paymentMethodDto data metode pembayaran baru
     * @return PaymentMethodDto metode pembayaran yang telah diupdate
     */
    PaymentMethodDto updatePaymentMethod(Long id, PaymentMethodDto paymentMethodDto);

    /**
     * Hapus metode pembayaran.
     *
     * @param id ID metode pembayaran yang akan dihapus
     */
    void deletePaymentMethod(Long id);

    /**
     * Mendapatkan entitas metode pembayaran berdasarkan ID.
     * Digunakan internal oleh service lain.
     *
     * @param id ID metode pembayaran
     * @return PaymentMethod entitas metode pembayaran
     */
    PaymentMethod getPaymentMethodEntityById(Long id);
}
