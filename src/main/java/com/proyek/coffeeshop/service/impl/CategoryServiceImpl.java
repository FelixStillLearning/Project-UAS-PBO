package com.proyek.coffeeshop.service.impl;

import com.proyek.coffeeshop.dto.response.CategoryDto;
import com.proyek.coffeeshop.exception.BadRequestException;
import com.proyek.coffeeshop.exception.ResourceNotFoundException;
import com.proyek.coffeeshop.model.entity.Category;
import com.proyek.coffeeshop.repository.CategoryRepository;
import com.proyek.coffeeshop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementasi service untuk operasi kategori.
 * Menangani CRUD kategori produk.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        log.info("Getting all categories");
        return categoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long id) {
        log.info("Getting category by ID: {}", id);
        Category category = getCategoryEntityById(id);
        return convertToDto(category);
    }

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        log.info("Creating new category: {}", categoryDto.getName());

        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new BadRequestException("Kategori dengan nama '" + categoryDto.getName() + "' sudah ada");
        }

        Category category = new Category();
        category.setName(categoryDto.getName());

        Category savedCategory = categoryRepository.save(category);
        log.info("Successfully created category with ID: {}", savedCategory.getCategoryId());

        return convertToDto(savedCategory);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        log.info("Updating category with ID: {}", id);

        Category category = getCategoryEntityById(id);

        // Check if new name already exists (excluding current category)
        if (!category.getName().equals(categoryDto.getName()) && 
            categoryRepository.existsByName(categoryDto.getName())) {
            throw new BadRequestException("Kategori dengan nama '" + categoryDto.getName() + "' sudah ada");
        }

        category.setName(categoryDto.getName());

        Category updatedCategory = categoryRepository.save(category);
        log.info("Successfully updated category with ID: {}", updatedCategory.getCategoryId());

        return convertToDto(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        log.info("Deleting category with ID: {}", id);

        Category category = getCategoryEntityById(id);
        
        // Check if category has products (if needed for business logic)
        // You might want to prevent deletion if category has products
        
        categoryRepository.delete(category);
        log.info("Successfully deleted category with ID: {}", id);
    }    @Override
    @Transactional(readOnly = true)
    public Category getCategoryEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kategori tidak ditemukan dengan ID: " + id));
    }

    /**
     * Konversi Category entity ke CategoryDto.
     */
    private CategoryDto convertToDto(Category category) {
        return new CategoryDto(
                category.getCategoryId(),
                category.getName()
        );
    }
}
