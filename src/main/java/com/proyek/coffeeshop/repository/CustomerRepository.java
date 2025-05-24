package com.proyek.coffeeshop.repository;

import com.proyek.coffeeshop.model.entity.Customer;
import com.proyek.coffeeshop.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface untuk entitas Customer.
 * Menyediakan operasi CRUD dan query custom untuk Customer.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Mencari customer berdasarkan user.
     *
     * @param user user yang terkait dengan customer
     * @return Optional Customer jika ditemukan
     */
    Optional<Customer> findByUser(User user);

    /**
     * Mencari customer berdasarkan user ID.
     *
     * @param userId ID user yang terkait dengan customer
     * @return Optional Customer jika ditemukan
     */
    @Query("SELECT c FROM Customer c WHERE c.user.userId = :userId")
    Optional<Customer> findByUserId(@Param("userId") Long userId);

    /**
     * Mencari customer berdasarkan username.
     *
     * @param username username dari user yang terkait
     * @return Optional Customer jika ditemukan
     */
    @Query("SELECT c FROM Customer c WHERE c.user.username = :username")
    Optional<Customer> findByUsername(@Param("username") String username);
}
