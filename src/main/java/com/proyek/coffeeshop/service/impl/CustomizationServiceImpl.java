package com.proyek.coffeeshop.service.impl;

import com.proyek.coffeeshop.dto.response.CustomizationDto;
import com.proyek.coffeeshop.exception.BadRequestException;
import com.proyek.coffeeshop.exception.ResourceNotFoundException;
import com.proyek.coffeeshop.model.entity.Customization;
import com.proyek.coffeeshop.repository.CustomizationRepository;
import com.proyek.coffeeshop.service.CustomizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementasi service untuk operasi kustomisasi.
 * Menangani CRUD kustomisasi produk.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomizationServiceImpl implements CustomizationService {

    private final CustomizationRepository customizationRepository;

    @Override
    public List<CustomizationDto> getAllCustomizations() {
        log.info("Getting all customizations");
        return customizationRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CustomizationDto getCustomizationById(Long id) {
        log.info("Getting customization by ID: {}", id);
        Customization customization = getCustomizationEntityById(id);
        return convertToDto(customization);
    }

    @Override
    public List<CustomizationDto> getCustomizationsByType(String type) {
        log.info("Getting customizations by type: {}", type);
        return customizationRepository.findByType(type).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomizationDto> searchCustomizationsByName(String name) {
        log.info("Searching customizations by name: {}", name);
        return customizationRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomizationDto> getFreeCustomizations() {
        log.info("Getting free customizations");
        return customizationRepository.findFreeCustomizations().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomizationDto> getPaidCustomizations() {
        log.info("Getting paid customizations");
        return customizationRepository.findPaidCustomizations().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CustomizationDto createCustomization(CustomizationDto customizationDto) {
        log.info("Creating new customization: {}", customizationDto.getName());

        if (customizationRepository.existsByName(customizationDto.getName())) {
            throw new BadRequestException("Kustomisasi dengan nama '" + customizationDto.getName() + "' sudah ada");
        }

        Customization customization = new Customization();
        customization.setName(customizationDto.getName());
        customization.setType(customizationDto.getType());
        customization.setPriceAdjustment(customizationDto.getPriceAdjustment());
        customization.setDescription(customizationDto.getDescription());

        Customization savedCustomization = customizationRepository.save(customization);
        log.info("Successfully created customization with ID: {}", savedCustomization.getCustomizationId());

        return convertToDto(savedCustomization);
    }

    @Override
    @Transactional
    public CustomizationDto updateCustomization(Long id, CustomizationDto customizationDto) {
        log.info("Updating customization with ID: {}", id);

        Customization customization = getCustomizationEntityById(id);

        // Check if new name already exists (excluding current customization)
        if (!customization.getName().equals(customizationDto.getName()) && 
            customizationRepository.existsByName(customizationDto.getName())) {
            throw new BadRequestException("Kustomisasi dengan nama '" + customizationDto.getName() + "' sudah ada");
        }

        customization.setName(customizationDto.getName());
        customization.setType(customizationDto.getType());
        customization.setPriceAdjustment(customizationDto.getPriceAdjustment());
        customization.setDescription(customizationDto.getDescription());

        Customization updatedCustomization = customizationRepository.save(customization);
        log.info("Successfully updated customization with ID: {}", updatedCustomization.getCustomizationId());

        return convertToDto(updatedCustomization);
    }

    @Override
    @Transactional
    public void deleteCustomization(Long id) {
        log.info("Deleting customization with ID: {}", id);

        Customization customization = getCustomizationEntityById(id);
        customizationRepository.delete(customization);
        log.info("Successfully deleted customization with ID: {}", id);
    }

    @Override
    public Customization getCustomizationEntityById(Long id) {
        return customizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kustomisasi tidak ditemukan dengan ID: " + id));
    }

    /**
     * Konversi Customization entity ke CustomizationDto.
     */
    private CustomizationDto convertToDto(Customization customization) {
        return new CustomizationDto(
                customization.getCustomizationId(),
                customization.getName(),
                customization.getType(),
                customization.getPriceAdjustment(),
                customization.getDescription()
        );
    }
}
