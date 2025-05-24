package com.proyek.coffeeshop.repository;

import com.proyek.coffeeshop.model.entity.OrderDetail;
import com.proyek.coffeeshop.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface untuk entitas OrderDetail.
 * Menyediakan operasi CRUD dan query custom untuk OrderDetail.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    /**
     * Mencari order detail berdasarkan order.
     *
     * @param order order yang memiliki detail
     * @return List OrderDetail milik order tertentu
     */
    List<OrderDetail> findByOrder(Order order);

    /**
     * Mencari order detail berdasarkan order ID.
     *
     * @param orderId ID order
     * @return List OrderDetail milik order tertentu
     */
    @Query("SELECT od FROM OrderDetail od WHERE od.order.orderId = :orderId")
    List<OrderDetail> findByOrderId(@Param("orderId") Long orderId);

    /**
     * Mencari order detail berdasarkan product ID.
     *
     * @param productId ID product
     * @return List OrderDetail yang menggunakan product tertentu
     */
    @Query("SELECT od FROM OrderDetail od WHERE od.product.productId = :productId")
    List<OrderDetail> findByProductId(@Param("productId") Long productId);
}
