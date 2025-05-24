package com.proyek.coffeeshop.controller;

import com.proyek.coffeeshop.dto.request.PaymentMethodRequestDto;
import com.proyek.coffeeshop.dto.response.PaymentMethodDto;
import com.proyek.coffeeshop.service.PaymentMethodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller untuk operasi metode pembayaran.
 * Menangani CRUD metode pembayaran.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/payment-methods")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    /**
     * Endpoint untuk mendapatkan semua metode pembayaran.
     * Dapat diakses oleh semua user yang sudah login.
     *
     * @return ResponseEntity dengan list metode pembayaran
     */
    @GetMapping
    public ResponseEntity<List<PaymentMethodDto>> getAllPaymentMethods() {
        log.info("GET /api/payment-methods - Getting all payment methods");
        
        List<PaymentMethodDto> paymentMethods = paymentMethodService.getAllPaymentMethods();
        return ResponseEntity.ok(paymentMethods);
    }

    /**
     * Endpoint untuk mendapatkan metode pembayaran berdasarkan ID.
     * Dapat diakses oleh semua user yang sudah login.
     *
     * @param id ID metode pembayaran
     * @return ResponseEntity dengan data metode pembayaran
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodDto> getPaymentMethodById(@PathVariable Long id) {
        log.info("GET /api/payment-methods/{} - Getting payment method by ID", id);
        
        PaymentMethodDto paymentMethod = paymentMethodService.getPaymentMethodById(id);
        return ResponseEntity.ok(paymentMethod);
    }

    /**
     * Endpoint untuk membuat metode pembayaran baru.
     * Hanya dapat diakses oleh admin.
     *
     * @param request data metode pembayaran baru
     * @return ResponseEntity dengan metode pembayaran yang telah dibuat
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentMethodDto> createPaymentMethod(@Valid @RequestBody PaymentMethodRequestDto request) {
        log.info("POST /api/payment-methods - Creating payment method: {}", request.getType());
        
        PaymentMethodDto paymentMethodDto = new PaymentMethodDto(null, request.getType());
        PaymentMethodDto response = paymentMethodService.createPaymentMethod(paymentMethodDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint untuk update metode pembayaran.
     * Hanya dapat diakses oleh admin.
     *
     * @param id ID metode pembayaran yang akan diupdate
     * @param request data metode pembayaran baru
     * @return ResponseEntity dengan metode pembayaran yang telah diupdate
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentMethodDto> updatePaymentMethod(
            @PathVariable Long id,
            @Valid @RequestBody PaymentMethodRequestDto request) {
        log.info("PUT /api/payment-methods/{} - Updating payment method", id);
        
        PaymentMethodDto paymentMethodDto = new PaymentMethodDto(id, request.getType());
        PaymentMethodDto response = paymentMethodService.updatePaymentMethod(id, paymentMethodDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint untuk menghapus metode pembayaran.
     * Hanya dapat diakses oleh admin.
     *
     * @param id ID metode pembayaran yang akan dihapus
     * @return ResponseEntity dengan status penghapusan
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable Long id) {
        log.info("DELETE /api/payment-methods/{} - Deleting payment method", id);
        
        paymentMethodService.deletePaymentMethod(id);
        return ResponseEntity.noContent().build();
    }
}
