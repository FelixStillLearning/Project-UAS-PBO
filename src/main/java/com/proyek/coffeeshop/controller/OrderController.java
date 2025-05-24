package com.proyek.coffeeshop.controller;

import com.proyek.coffeeshop.dto.request.OrderRequestDto;
import com.proyek.coffeeshop.dto.response.OrderResponseDto;
import com.proyek.coffeeshop.model.enums.OrderStatus;
import com.proyek.coffeeshop.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller untuk operasi order.
 * Menangani pembuatan, update, dan pencarian order.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrderController {

    private final OrderService orderService;

    /**
     * Endpoint untuk membuat order baru.
     * Hanya dapat diakses oleh customer.
     *
     * @param request data order baru
     * @param authentication data autentikasi dari Spring Security
     * @return ResponseEntity dengan order yang telah dibuat
     */
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderResponseDto> createOrder(
            @Valid @RequestBody OrderRequestDto request,
            Authentication authentication) {
        log.info("POST /api/orders - Creating order for user: {}", authentication.getName());
        
        OrderResponseDto response = orderService.createOrder(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint untuk mendapatkan order customer yang sedang login.
     * Hanya dapat diakses oleh customer.
     *
     * @param authentication data autentikasi dari Spring Security
     * @return ResponseEntity dengan list order customer
     */
    @GetMapping("/my-orders")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<OrderResponseDto>> getMyOrders(Authentication authentication) {
        log.info("GET /api/orders/my-orders - Getting orders for user: {}", authentication.getName());
        
        List<OrderResponseDto> orders = orderService.getOrdersByCustomer(authentication.getName());
        return ResponseEntity.ok(orders);
    }

    /**
     * Endpoint untuk mendapatkan detail order berdasarkan ID.
     * Customer hanya bisa melihat order miliknya, admin bisa melihat semua.
     *
     * @param id ID order
     * @return ResponseEntity dengan detail order
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id) {
        log.info("GET /api/orders/{} - Getting order details", id);
        
        OrderResponseDto order = orderService.getOrderDetailsById(id);
        return ResponseEntity.ok(order);
    }

    /**
     * Endpoint untuk mendapatkan semua order dengan pagination (admin only).
     * Hanya dapat diakses oleh admin.
     *
     * @param pageable informasi pagination
     * @return ResponseEntity dengan Page order
     */
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<OrderResponseDto>> getAllOrders(
            @PageableDefault(size = 20, sort = "orderDate") Pageable pageable) {
        log.info("GET /api/orders/admin/all - Getting all orders with pagination");
        
        Page<OrderResponseDto> orders = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }

    /**
     * Endpoint untuk mendapatkan order berdasarkan status (admin only).
     * Hanya dapat diakses oleh admin.
     *
     * @param status status order
     * @param pageable informasi pagination
     * @return ResponseEntity dengan Page order
     */
    @GetMapping("/admin/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<OrderResponseDto>> getOrdersByStatus(
            @PathVariable OrderStatus status,
            @PageableDefault(size = 20, sort = "orderDate") Pageable pageable) {
        log.info("GET /api/orders/admin/status/{} - Getting orders by status", status);
        
        Page<OrderResponseDto> orders = orderService.getOrdersByStatus(status, pageable);
        return ResponseEntity.ok(orders);
    }

    /**
     * Endpoint untuk mendapatkan order berdasarkan range tanggal (admin only).
     * Hanya dapat diakses oleh admin.
     *
     * @param startDate tanggal mulai
     * @param endDate tanggal akhir
     * @return ResponseEntity dengan list order
     */
    @GetMapping("/admin/date-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("GET /api/orders/admin/date-range - Getting orders by date range: {} to {}", startDate, endDate);
        
        List<OrderResponseDto> orders = orderService.getOrdersByDateRange(startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    /**
     * Endpoint untuk update status order (admin only).
     * Hanya dapat diakses oleh admin.
     *
     * @param id ID order
     * @param newStatus status baru
     * @return ResponseEntity dengan order yang telah diupdate
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus newStatus) {
        log.info("PUT /api/orders/{}/status - Updating order status to: {}", id, newStatus);
        
        OrderResponseDto response = orderService.updateOrderStatus(id, newStatus);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint untuk konfirmasi pembayaran order (admin only).
     * Hanya dapat diakses oleh admin.
     *
     * @param id ID order
     * @return ResponseEntity dengan order yang telah dikonfirmasi
     */
    @PutMapping("/{id}/confirm-payment")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDto> confirmOrderPayment(@PathVariable Long id) {
        log.info("PUT /api/orders/{}/confirm-payment - Confirming payment", id);
        
        OrderResponseDto response = orderService.confirmOrderPayment(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint untuk membatalkan order.
     * Customer hanya bisa membatalkan order miliknya, admin bisa membatalkan semua.
     *
     * @param id ID order
     * @param authentication data autentikasi dari Spring Security
     * @return ResponseEntity dengan order yang telah dibatalkan
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponseDto> cancelOrder(
            @PathVariable Long id,
            Authentication authentication) {
        log.info("PUT /api/orders/{}/cancel - Cancelling order for user: {}", id, authentication.getName());
        
        OrderResponseDto response = orderService.cancelOrder(id, authentication.getName());
        return ResponseEntity.ok(response);
    }
}
