package com.proyek.coffeeshop.repository;

import com.proyek.coffeeshop.model.entity.Product;
import com.proyek.coffeeshop.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository interface untuk entitas Product.
 * Menyediakan operasi CRUD dan query custom untuk Product.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Mencari produk berdasarkan kategori.
     *
     * @param category kategori produk
     * @return List Product dalam kategori tertentu
     */
    List<Product> findByCategory(Category category);

    /**
     * Mencari produk berdasarkan kategori dengan pagination.
     *
     * @param category kategori produk
     * @param pageable informasi pagination
     * @return Page Product dalam kategori tertentu
     */
    Page<Product> findByCategory(Category category, Pageable pageable);

    /**
     * Mencari produk berdasarkan ID kategori.
     *
     * @param categoryId ID kategori
     * @return List Product dalam kategori tertentu
     */
    @Query("SELECT p FROM Product p WHERE p.category.categoryId = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * Mencari produk berdasarkan nama (contains, case insensitive).
     *
     * @param name nama produk yang dicari
     * @return List Product yang mengandung nama tertentu
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Mencari produk berdasarkan range harga.
     *
     * @param minPrice harga minimum
     * @param maxPrice harga maksimum
     * @return List Product dalam range harga tertentu
     */
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);

    /**
     * Mencari produk berdasarkan range harga menggunakan Between.
     *
     * @param minPrice harga minimum
     * @param maxPrice harga maksimum
     * @return List Product dalam range harga tertentu
     */
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Memeriksa apakah produk dengan nama tertentu sudah ada.
     *
     * @param name nama produk yang diperiksa
     * @return true jika produk dengan nama tersebut sudah ada
     */
    boolean existsByName(String name);

    /**
     * Mencari produk dengan stock yang kurang dari minimum stock level.
     *
     * @return List Product dengan stock rendah
     */
    @Query("SELECT p FROM Product p WHERE p.stockQuantity < p.minStockLevel")
    List<Product> findByStockQuantityLessThanMinStockLevel();

    /**
     * Mencari produk berdasarkan availability.
     *
     * @param available status availability yang dicari
     * @return List Product dengan status available tertentu
     */
    List<Product> findByAvailable(Boolean available);
}
