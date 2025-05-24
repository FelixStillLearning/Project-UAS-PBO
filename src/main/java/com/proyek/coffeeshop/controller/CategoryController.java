package com.proyek.coffeeshop.controller;

import com.proyek.coffeeshop.dto.request.CategoryRequestDto;
import com.proyek.coffeeshop.dto.response.CategoryDto;
import com.proyek.coffeeshop.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller untuk operasi kategori.
 * Menangani CRUD kategori produk.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Endpoint untuk mendapatkan semua kategori.
     * Dapat diakses oleh semua user yang sudah login.
     *
     * @return ResponseEntity dengan list kategori
     */
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        log.info("GET /api/categories - Getting all categories");
        
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * Endpoint untuk mendapatkan kategori berdasarkan ID.
     * Dapat diakses oleh semua user yang sudah login.
     *
     * @param id ID kategori
     * @return ResponseEntity dengan data kategori
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        log.info("GET /api/categories/{} - Getting category by ID", id);
        
        CategoryDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    /**
     * Endpoint untuk membuat kategori baru.
     * Hanya dapat diakses oleh admin.
     *
     * @param request data kategori baru
     * @return ResponseEntity dengan kategori yang telah dibuat
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryRequestDto request) {
        log.info("POST /api/categories - Creating category: {}", request.getName());
        
        CategoryDto categoryDto = new CategoryDto(null, request.getName());
        CategoryDto response = categoryService.createCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint untuk update kategori.
     * Hanya dapat diakses oleh admin.
     *
     * @param id ID kategori yang akan diupdate
     * @param request data kategori baru
     * @return ResponseEntity dengan kategori yang telah diupdate
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDto request) {
        log.info("PUT /api/categories/{} - Updating category", id);
        
        CategoryDto categoryDto = new CategoryDto(id, request.getName());
        CategoryDto response = categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint untuk menghapus kategori.
     * Hanya dapat diakses oleh admin.
     *
     * @param id ID kategori yang akan dihapus
     * @return ResponseEntity dengan status penghapusan
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.info("DELETE /api/categories/{} - Deleting category", id);
        
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
