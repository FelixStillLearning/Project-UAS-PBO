package com.proyek.coffeeshop.model.entity;

import com.proyek.coffeeshop.model.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entitas JPA untuk tabel Users.
 * Menyimpan informasi dasar pengguna seperti username, password, email, dan role.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank(message = "Username tidak boleh kosong")
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @NotBlank(message = "Password tidak boleh kosong")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Format email tidak valid")
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @NotNull(message = "Role tidak boleh kosong")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    /**
     * Relasi one-to-one dengan Customer.
     * Setiap User bisa memiliki satu Customer profile (jika role adalah CUSTOMER).
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Customer customer;
}
