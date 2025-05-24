package com.proyek.coffeeshop.repository;

import com.proyek.coffeeshop.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface untuk entitas Category.
 * Menyediakan operasi CRUD dan query custom untuk Category.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Mencari kategori berdasarkan nama.
     *
     * @param name nama kategori yang dicari
     * @return Optional Category jika ditemukan
     */
    Optional<Category> findByName(String name);

    /**
     * Mengecek apakah nama kategori sudah ada.
     *
     * @param name nama kategori yang dicek
     * @return true jika nama kategori sudah ada
     */
    boolean existsByName(String name);
}
