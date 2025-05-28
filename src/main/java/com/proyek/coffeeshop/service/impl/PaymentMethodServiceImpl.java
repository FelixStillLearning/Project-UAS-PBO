package com.proyek.coffeeshop.service.impl;

import com.proyek.coffeeshop.dto.response.PaymentMethodDto;
import com.proyek.coffeeshop.exception.BadRequestException;
import com.proyek.coffeeshop.exception.ResourceNotFoundException;
import com.proyek.coffeeshop.model.entity.PaymentMethod;
import com.proyek.coffeeshop.repository.PaymentMethodRepository;
import com.proyek.coffeeshop.service.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementasi service untuk operasi metode pembayaran.
 * Menangani CRUD metode pembayaran.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    public List<PaymentMethodDto> getAllPaymentMethods() {
        log.info("Getting all payment methods");
        return paymentMethodRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentMethodDto getPaymentMethodById(Long id) {
        log.info("Getting payment method by ID: {}", id);
        PaymentMethod paymentMethod = getPaymentMethodEntityById(id);
        return convertToDto(paymentMethod);
    }

    @Override
    @Transactional
    public PaymentMethodDto createPaymentMethod(PaymentMethodDto paymentMethodDto) {
        log.info("Creating new payment method: name='{}', description='{}'", paymentMethodDto.getName(), paymentMethodDto.getDescription());

        if (paymentMethodRepository.existsByName(paymentMethodDto.getName())) {
            throw new BadRequestException("Metode pembayaran dengan nama '" + paymentMethodDto.getName() + "' sudah ada");
        }

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setName(paymentMethodDto.getName());
        paymentMethod.setDescription(paymentMethodDto.getDescription());

        PaymentMethod savedPaymentMethod = paymentMethodRepository.save(paymentMethod);
        log.info("Successfully created payment method with ID: {}", savedPaymentMethod.getPaymentId());

        return convertToDto(savedPaymentMethod);
    }

    @Override
    @Transactional
    public PaymentMethodDto updatePaymentMethod(Long id, PaymentMethodDto paymentMethodDto) {
        log.info("Updating payment method with ID: {}", id);

        PaymentMethod paymentMethod = getPaymentMethodEntityById(id);

        // Check if new name already exists (excluding current payment method)
        if (!paymentMethod.getName().equals(paymentMethodDto.getName()) && 
            paymentMethodRepository.existsByName(paymentMethodDto.getName())) {
            throw new BadRequestException("Metode pembayaran dengan nama '" + paymentMethodDto.getName() + "' sudah ada");
        }

        paymentMethod.setName(paymentMethodDto.getName());
        paymentMethod.setDescription(paymentMethodDto.getDescription());

        PaymentMethod updatedPaymentMethod = paymentMethodRepository.save(paymentMethod);
        log.info("Successfully updated payment method with ID: {}", updatedPaymentMethod.getPaymentId());

        return convertToDto(updatedPaymentMethod);
    }

    @Override
    @Transactional
    public void deletePaymentMethod(Long id) {
        log.info("Deleting payment method with ID: {}", id);

        PaymentMethod paymentMethod = getPaymentMethodEntityById(id);
        paymentMethodRepository.delete(paymentMethod);
        log.info("Successfully deleted payment method with ID: {}", id);
    }

    @Override
    public PaymentMethod getPaymentMethodEntityById(Long id) {
        return paymentMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Metode pembayaran tidak ditemukan dengan ID: " + id));
    }

    /**
     * Konversi PaymentMethod entity ke PaymentMethodDto.
     */
    private PaymentMethodDto convertToDto(PaymentMethod paymentMethod) {
        return new PaymentMethodDto(
                paymentMethod.getPaymentId(),
                paymentMethod.getName(),
                paymentMethod.getDescription()
        );
    }
}
