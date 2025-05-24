package com.proyek.coffeeshop.repository;

import com.proyek.coffeeshop.model.entity.Order;
import com.proyek.coffeeshop.model.entity.Customer;
import com.proyek.coffeeshop.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface untuk entitas Order.
 * Menyediakan operasi CRUD dan query custom untuk Order.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Mencari order berdasarkan customer.
     *
     * @param customer customer yang memiliki order
     * @return List Order milik customer tertentu
     */
    List<Order> findByCustomer(Customer customer);

    /**
     * Mencari order berdasarkan customer dengan pagination.
     *
     * @param customer customer yang memiliki order
     * @param pageable informasi pagination
     * @return Page Order milik customer tertentu
     */
    Page<Order> findByCustomer(Customer customer, Pageable pageable);

    /**
     * Mencari order berdasarkan customer ID.
     *
     * @param customerId ID customer
     * @return List Order milik customer tertentu
     */
    @Query("SELECT o FROM Order o WHERE o.customer.customerId = :customerId")
    List<Order> findByCustomerId(@Param("customerId") Long customerId);

    /**
     * Mencari order berdasarkan username customer.
     *
     * @param username username customer
     * @return List Order milik customer dengan username tertentu
     */
    @Query("SELECT o FROM Order o WHERE o.customer.user.username = :username ORDER BY o.orderDate DESC")
    List<Order> findByCustomerUsername(@Param("username") String username);

    /**
     * Mencari order berdasarkan status.
     *
     * @param status status order yang dicari
     * @return List Order dengan status tertentu
     */
    List<Order> findByStatus(OrderStatus status);

    /**
     * Mencari order berdasarkan status dengan pagination.
     *
     * @param status status order yang dicari
     * @param pageable informasi pagination
     * @return Page Order dengan status tertentu
     */
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    /**
     * Mencari order berdasarkan range tanggal.
     *
     * @param startDate tanggal mulai
     * @param endDate tanggal akhir
     * @return List Order dalam range tanggal tertentu
     */
    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate ORDER BY o.orderDate DESC")
    List<Order> findByOrderDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * Mencari semua order dengan pagination dan sorting berdasarkan tanggal terbaru.
     *
     * @param pageable informasi pagination
     * @return Page Order diurutkan berdasarkan tanggal terbaru
     */
    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC")
    Page<Order> findAllOrderByDateDesc(Pageable pageable);
}
