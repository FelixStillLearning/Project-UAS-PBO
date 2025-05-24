package com.proyek.coffeeshop.controller;

import com.proyek.coffeeshop.dto.request.LoginRequestDto;
import com.proyek.coffeeshop.dto.request.RegisterRequestDto;
import com.proyek.coffeeshop.dto.response.UserResponseDto;
import com.proyek.coffeeshop.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller untuk operasi autentikasi.
 * Menangani registrasi dan login user.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint untuk registrasi customer baru.
     *
     * @param request data registrasi
     * @return ResponseEntity dengan data user yang terdaftar
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerCustomer(@Valid @RequestBody RegisterRequestDto request) {
        log.info("POST /api/auth/register - Registering customer: {}", request.getUsername());
        
        UserResponseDto response = authService.registerCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint untuk login user.
     *
     * @param request data login
     * @return ResponseEntity dengan data user yang login
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        log.info("POST /api/auth/login - User login: {}", request.getUsername());
        
        UserResponseDto response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint untuk mendapatkan informasi user yang sedang login.
     *
     * @param authentication data autentikasi dari Spring Security
     * @return ResponseEntity dengan data user
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(Authentication authentication) {
        log.info("GET /api/auth/me - Getting current user: {}", authentication.getName());
        
        UserResponseDto response = authService.getUserByUsername(authentication.getName());
        return ResponseEntity.ok(response);
    }
}
