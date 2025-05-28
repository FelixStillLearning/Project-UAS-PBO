package com.proyek.coffeeshop.model.enums;

/**
 * Enum untuk status pesanan dalam sistem Coffee Shop.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
public enum OrderStatus {
    WAITING_PAYMENT,
    PROCESSING,
    READY_FOR_PICKUP,
    COMPLETED,
    CANCELLED,
    // Tambahan untuk Kasir
    PAID, // Pembayaran telah diterima (bisa menggantikan WAITING_PAYMENT jika alur kasir langsung bayar)
    PREPARING, // Pesanan sedang disiapkan (mirip PROCESSING, tapi bisa lebih spesifik untuk alur kasir)
    READY_TO_SERVE // Pesanan siap disajikan/diambil (untuk kasus di tempat oleh kasir)
}
