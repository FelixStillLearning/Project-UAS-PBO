package com.proyek.coffeeshop.controller;

import com.proyek.coffeeshop.dto.request.CustomizationRequestDto;
import com.proyek.coffeeshop.dto.response.CustomizationDto;
import com.proyek.coffeeshop.service.CustomizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller untuk operasi kustomisasi.
 * Menangani CRUD kustomisasi produk.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/customizations")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class CustomizationController {

    private final CustomizationService customizationService;

    /**
     * Endpoint untuk mendapatkan semua kustomisasi.
     * Dapat diakses oleh semua user yang sudah login.
     *
     * @return ResponseEntity dengan list kustomisasi
     */
    @GetMapping
    public ResponseEntity<List<CustomizationDto>> getAllCustomizations() {
        log.info("GET /api/customizations - Getting all customizations");
        
        List<CustomizationDto> customizations = customizationService.getAllCustomizations();
        return ResponseEntity.ok(customizations);
    }

    /**
     * Endpoint untuk mendapatkan kustomisasi berdasarkan ID.
     * Dapat diakses oleh semua user yang sudah login.
     *
     * @param id ID kustomisasi
     * @return ResponseEntity dengan data kustomisasi
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomizationDto> getCustomizationById(@PathVariable Long id) {
        log.info("GET /api/customizations/{} - Getting customization by ID", id);
        
        CustomizationDto customization = customizationService.getCustomizationById(id);
        return ResponseEntity.ok(customization);
    }

    /**
     * Endpoint untuk mendapatkan kustomisasi berdasarkan tipe.
     * Dapat diakses oleh semua user yang sudah login.
     *
     * @param type tipe kustomisasi
     * @return ResponseEntity dengan list kustomisasi
     */
    @GetMapping("/type")
    public ResponseEntity<List<CustomizationDto>> getCustomizationsByType(@RequestParam String type) {
        log.info("GET /api/customizations/type?type={} - Getting customizations by type", type);
        
        List<CustomizationDto> customizations = customizationService.getCustomizationsByType(type);
        return ResponseEntity.ok(customizations);
    }

    /**
     * Endpoint untuk mencari kustomisasi berdasarkan nama.
     * Dapat diakses oleh semua user yang sudah login.
     *
     * @param name nama kustomisasi yang dicari
     * @return ResponseEntity dengan list kustomisasi
     */
    @GetMapping("/search")
    public ResponseEntity<List<CustomizationDto>> searchCustomizationsByName(@RequestParam String name) {
        log.info("GET /api/customizations/search?name={} - Searching customizations by name", name);
        
        List<CustomizationDto> customizations = customizationService.searchCustomizationsByName(name);
        return ResponseEntity.ok(customizations);
    }

    /**
     * Endpoint untuk mendapatkan kustomisasi gratis.
     * Dapat diakses oleh semua user yang sudah login.
     *
     * @return ResponseEntity dengan list kustomisasi gratis
     */
    @GetMapping("/free")
    public ResponseEntity<List<CustomizationDto>> getFreeCustomizations() {
        log.info("GET /api/customizations/free - Getting free customizations");
        
        List<CustomizationDto> customizations = customizationService.getFreeCustomizations();
        return ResponseEntity.ok(customizations);
    }

    /**
     * Endpoint untuk mendapatkan kustomisasi berbayar.
     * Dapat diakses oleh semua user yang sudah login.
     *
     * @return ResponseEntity dengan list kustomisasi berbayar
     */
    @GetMapping("/paid")
    public ResponseEntity<List<CustomizationDto>> getPaidCustomizations() {
        log.info("GET /api/customizations/paid - Getting paid customizations");
        
        List<CustomizationDto> customizations = customizationService.getPaidCustomizations();
        return ResponseEntity.ok(customizations);
    }

    /**
     * Endpoint untuk membuat kustomisasi baru.
     * Hanya dapat diakses oleh admin.
     *
     * @param request data kustomisasi baru
     * @return ResponseEntity dengan kustomisasi yang telah dibuat
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomizationDto> createCustomization(@Valid @RequestBody CustomizationRequestDto request) {
        log.info("POST /api/customizations - Creating customization: {}", request.getName());
        
        CustomizationDto customizationDto = new CustomizationDto(
                null,
                request.getName(),
                request.getType(),
                request.getPriceAdjustment(),
                request.getDescription()
        );
        
        CustomizationDto response = customizationService.createCustomization(customizationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint untuk update kustomisasi.
     * Hanya dapat diakses oleh admin.
     *
     * @param id ID kustomisasi yang akan diupdate
     * @param request data kustomisasi baru
     * @return ResponseEntity dengan kustomisasi yang telah diupdate
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomizationDto> updateCustomization(
            @PathVariable Long id,
            @Valid @RequestBody CustomizationRequestDto request) {
        log.info("PUT /api/customizations/{} - Updating customization", id);
        
        CustomizationDto customizationDto = new CustomizationDto(
                id,
                request.getName(),
                request.getType(),
                request.getPriceAdjustment(),
                request.getDescription()
        );
        
        CustomizationDto response = customizationService.updateCustomization(id, customizationDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint untuk menghapus kustomisasi.
     * Hanya dapat diakses oleh admin.
     *
     * @param id ID kustomisasi yang akan dihapus
     * @return ResponseEntity dengan status penghapusan
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCustomization(@PathVariable Long id) {
        log.info("DELETE /api/customizations/{} - Deleting customization", id);
        
        customizationService.deleteCustomization(id);
        return ResponseEntity.noContent().build();
    }
}
