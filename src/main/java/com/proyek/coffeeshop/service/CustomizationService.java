package com.proyek.coffeeshop.service;

import com.proyek.coffeeshop.dto.response.CustomizationDto;
import com.proyek.coffeeshop.model.entity.Customization;

import java.util.List;

/**
 * Service interface untuk operasi kustomisasi.
 * Menangani CRUD kustomisasi produk.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
public interface CustomizationService {

    /**
     * Mendapatkan semua kustomisasi.
     *
     * @return List CustomizationDto semua kustomisasi
     */
    List<CustomizationDto> getAllCustomizations();

    /**
     * Mendapatkan kustomisasi berdasarkan ID.
     *
     * @param id ID kustomisasi
     * @return CustomizationDto data kustomisasi
     */
    CustomizationDto getCustomizationById(Long id);

    /**
     * Mendapatkan kustomisasi berdasarkan tipe.
     *
     * @param type tipe kustomisasi
     * @return List CustomizationDto kustomisasi dengan tipe tertentu
     */
    List<CustomizationDto> getCustomizationsByType(String type);

    /**
     * Mendapatkan kustomisasi gratis.
     *
     * @return List CustomizationDto kustomisasi gratis
     */
    List<CustomizationDto> getFreeCustomizations();

    /**
     * Mendapatkan kustomisasi berbayar.
     *
     * @return List CustomizationDto kustomisasi berbayar
     */
    List<CustomizationDto> getPaidCustomizations();

    /**
     * Membuat kustomisasi baru.
     *
     * @param customizationDto data kustomisasi baru
     * @return CustomizationDto kustomisasi yang telah dibuat
     */
    CustomizationDto createCustomization(CustomizationDto customizationDto);

    /**
     * Update kustomisasi.
     *
     * @param id ID kustomisasi yang akan diupdate
     * @param customizationDto data kustomisasi baru
     * @return CustomizationDto kustomisasi yang telah diupdate
     */
    CustomizationDto updateCustomization(Long id, CustomizationDto customizationDto);

    /**
     * Hapus kustomisasi.
     *
     * @param id ID kustomisasi yang akan dihapus
     */
    void deleteCustomization(Long id);

    /**
     * Mendapatkan entitas kustomisasi berdasarkan ID.
     * Digunakan internal oleh service lain.
     *
     * @param id ID kustomisasi
     * @return Customization entitas kustomisasi
     */
    Customization getCustomizationEntityById(Long id);

    /**
     * Mencari kustomisasi berdasarkan nama.
     *
     * @param name nama kustomisasi yang dicari
     * @return List CustomizationDto kustomisasi yang mengandung nama tersebut
     */
    List<CustomizationDto> searchCustomizationsByName(String name);
}
