package com.proyek.coffeeshop.service;

import com.proyek.coffeeshop.dto.request.LoginRequestDto;
import com.proyek.coffeeshop.dto.request.RegisterRequestDto;
import com.proyek.coffeeshop.dto.response.UserResponseDto;

/**
 * Service interface untuk operasi autentikasi.
 * Menangani registrasi dan login user.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
public interface AuthService {

    /**
     * Registrasi customer baru.
     * Membuat user dan customer profile baru.
     *
     * @param request data registrasi
     * @return UserResponseDto data user yang telah dibuat
     */
    UserResponseDto registerCustomer(RegisterRequestDto request);

    /**
     * Login user.
     * Melakukan autentikasi berdasarkan username dan password.
     *
     * @param request data login
     * @return UserResponseDto data user yang berhasil login
     */
    UserResponseDto login(LoginRequestDto request);

    /**
     * Mendapatkan user berdasarkan username.
     *
     * @param username username user
     * @return UserResponseDto data user
     */
    UserResponseDto getUserByUsername(String username);
}
