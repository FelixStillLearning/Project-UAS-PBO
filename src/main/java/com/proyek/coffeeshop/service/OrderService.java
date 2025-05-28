package com.proyek.coffeeshop.service;

import com.proyek.coffeeshop.dto.request.CashierOrderRequestDTO;
import com.proyek.coffeeshop.dto.request.OrderRequestDto;
import com.proyek.coffeeshop.dto.response.CashierOrderResponseDTO;
import com.proyek.coffeeshop.dto.response.OrderResponseDto;
import com.proyek.coffeeshop.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface untuk operasi order.
 * Menangani pembuatan, update, dan pencarian order.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
public interface OrderService {

    /**
     * Membuat order baru.
     * Menghitung total amount dan subtotal price.
     *
     * @param request data order baru
     * @param username username customer yang membuat order
     * @return OrderResponseDto order yang telah dibuat
     */
    OrderResponseDto createOrder(OrderRequestDto request, String username);

    /**
     * Mendapatkan order berdasarkan customer username.
     *
     * @param username username customer
     * @return List OrderResponseDto order milik customer
     */
    List<OrderResponseDto> getOrdersByCustomer(String username);

    /**
     * Mendapatkan detail order berdasarkan ID.
     *
     * @param orderId ID order
     * @return OrderResponseDto detail order
     */
    OrderResponseDto getOrderDetailsById(Long orderId);

    /**
     * Mendapatkan semua order dengan pagination (untuk admin).
     *
     * @param pageable informasi pagination
     * @return Page OrderResponseDto semua order
     */
    Page<OrderResponseDto> getAllOrders(Pageable pageable);

    /**
     * Mendapatkan order berdasarkan status.
     *
     * @param status status order
     * @param pageable informasi pagination
     * @return Page OrderResponseDto order dengan status tertentu
     */
    Page<OrderResponseDto> getOrdersByStatus(OrderStatus status, Pageable pageable);

    /**
     * Update status order.
     *
     * @param orderId ID order
     * @param newStatus status baru
     * @return OrderResponseDto order yang telah diupdate
     */
    OrderResponseDto updateOrderStatus(Long orderId, OrderStatus newStatus);

    /**
     * Konfirmasi pembayaran order.
     * Mengubah status dari WAITING_PAYMENT ke PROCESSING.
     *
     * @param orderId ID order
     * @return OrderResponseDto order yang telah dikonfirmasi
     */
    OrderResponseDto confirmOrderPayment(Long orderId);

    /**
     * Mendapatkan order berdasarkan range tanggal.
     *
     * @param startDate tanggal mulai
     * @param endDate tanggal akhir
     * @return List OrderResponseDto order dalam range tanggal
     */
    List<OrderResponseDto> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Batalkan order.
     * Mengubah status menjadi CANCELLED.
     *
     * @param orderId ID order
     * @param username username customer (untuk validasi)
     * @return OrderResponseDto order yang telah dibatalkan
     */
    OrderResponseDto cancelOrder(Long orderId, String username);

    /**
     * Membuat order baru melalui kasir (walk-in).
     *
     * @param request data order dari kasir
     * @param cashierUsername username kasir yang memproses order
     * @return CashierOrderResponseDTO order yang telah dibuat
     */
    CashierOrderResponseDTO createCashierOrder(CashierOrderRequestDTO request, String cashierUsername);
}
