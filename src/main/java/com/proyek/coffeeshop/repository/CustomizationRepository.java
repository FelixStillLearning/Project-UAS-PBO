package com.proyek.coffeeshop.repository;

import com.proyek.coffeeshop.model.entity.Customization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface untuk entitas Customization.
 * Menyediakan operasi CRUD dan query custom untuk Customization.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Repository
public interface CustomizationRepository extends JpaRepository<Customization, Long> {

    /**
     * Mencari kustomisasi berdasarkan tipe.
     *
     * @param type tipe kustomisasi yang dicari
     * @return List Customization dengan tipe tertentu
     */
    List<Customization> findByType(String type);

    /**
     * Mencari kustomisasi berdasarkan nama (contains, case insensitive).
     *
     * @param name nama kustomisasi yang dicari
     * @return List Customization yang mengandung nama tertentu
     */
    @Query("SELECT c FROM Customization c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Customization> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Mencari kustomisasi yang gratis (price adjustment = 0).
     *
     * @return List Customization dengan harga 0
     */
    @Query("SELECT c FROM Customization c WHERE c.priceAdjustment = 0")
    List<Customization> findFreeCustomizations();

    /**
     * Mencari kustomisasi berbayar (price adjustment > 0).
     *
     * @return List Customization dengan harga > 0
     */
    @Query("SELECT c FROM Customization c WHERE c.priceAdjustment > 0")
    List<Customization> findPaidCustomizations();

    /**
     * Memeriksa apakah kustomisasi dengan nama tertentu sudah ada.
     *
     * @param name nama kustomisasi yang diperiksa
     * @return true jika kustomisasi dengan nama tersebut sudah ada
     */
    boolean existsByName(String name);
}
