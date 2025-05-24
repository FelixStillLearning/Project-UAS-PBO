package com.proyek.coffeeshop.service;

import com.proyek.coffeeshop.dto.response.CategoryDto;
import com.proyek.coffeeshop.model.entity.Category;

import java.util.List;

/**
 * Service interface untuk operasi kategori.
 * Menangani CRUD kategori produk.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
public interface CategoryService {

    /**
     * Mendapatkan semua kategori.
     *
     * @return List CategoryDto semua kategori
     */
    List<CategoryDto> getAllCategories();

    /**
     * Mendapatkan kategori berdasarkan ID.
     *
     * @param id ID kategori
     * @return CategoryDto data kategori
     */
    CategoryDto getCategoryById(Long id);

    /**
     * Membuat kategori baru.
     *
     * @param categoryDto data kategori baru
     * @return CategoryDto kategori yang telah dibuat
     */
    CategoryDto createCategory(CategoryDto categoryDto);

    /**
     * Update kategori.
     *
     * @param id ID kategori yang akan diupdate
     * @param categoryDto data kategori baru
     * @return CategoryDto kategori yang telah diupdate
     */
    CategoryDto updateCategory(Long id, CategoryDto categoryDto);

    /**
     * Hapus kategori.
     *
     * @param id ID kategori yang akan dihapus
     */
    void deleteCategory(Long id);

    /**
     * Mendapatkan entitas kategori berdasarkan ID.
     * Digunakan internal oleh service lain.
     *
     * @param id ID kategori
     * @return Category entitas kategori
     */
    Category getCategoryEntityById(Long id);
}
