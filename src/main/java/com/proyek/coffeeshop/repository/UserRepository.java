package com.proyek.coffeeshop.repository;

import com.proyek.coffeeshop.model.entity.User;
import com.proyek.coffeeshop.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface untuk entitas User.
 * Menyediakan operasi CRUD dan query custom untuk User.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Mencari user berdasarkan username.
     *
     * @param username username yang dicari
     * @return Optional User jika ditemukan
     */
    Optional<User> findByUsername(String username);

    /**
     * Mencari user berdasarkan email.
     *
     * @param email email yang dicari
     * @return Optional User jika ditemukan
     */
    Optional<User> findByEmail(String email);

    /**
     * Mengecek apakah username sudah ada.
     *
     * @param username username yang dicek
     * @return true jika username sudah ada
     */
    boolean existsByUsername(String username);

    /**
     * Mengecek apakah email sudah ada.
     *
     * @param email email yang dicek
     * @return true jika email sudah ada
     */
    boolean existsByEmail(String email);

    /**
     * Mencari user berdasarkan role.
     *
     * @param role role yang dicari
     * @return List User dengan role tertentu
     */
    @Query("SELECT u FROM User u WHERE u.role = :role")
    java.util.List<User> findByRole(@Param("role") UserRole role);
}
