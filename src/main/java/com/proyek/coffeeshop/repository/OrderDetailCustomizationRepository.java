package com.proyek.coffeeshop.repository;

import com.proyek.coffeeshop.model.entity.OrderDetailCustomization;
import com.proyek.coffeeshop.model.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface untuk entitas OrderDetailCustomization.
 * Menyediakan operasi CRUD dan query custom untuk OrderDetailCustomization.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Repository
public interface OrderDetailCustomizationRepository extends JpaRepository<OrderDetailCustomization, Long> {

    /**
     * Mencari kustomisasi berdasarkan order detail.
     *
     * @param orderDetail order detail yang memiliki kustomisasi
     * @return List OrderDetailCustomization milik order detail tertentu
     */
    List<OrderDetailCustomization> findByOrderDetail(OrderDetail orderDetail);

    /**
     * Mencari kustomisasi berdasarkan order detail ID.
     *
     * @param orderDetailId ID order detail
     * @return List OrderDetailCustomization milik order detail tertentu
     */
    @Query("SELECT odc FROM OrderDetailCustomization odc WHERE odc.orderDetail.detailId = :orderDetailId")
    List<OrderDetailCustomization> findByOrderDetailId(@Param("orderDetailId") Long orderDetailId);

    /**
     * Mencari kustomisasi berdasarkan customization ID.
     *
     * @param customizationId ID customization
     * @return List OrderDetailCustomization yang menggunakan customization tertentu
     */
    @Query("SELECT odc FROM OrderDetailCustomization odc WHERE odc.customization.customizationId = :customizationId")
    List<OrderDetailCustomization> findByCustomizationId(@Param("customizationId") Long customizationId);
}
