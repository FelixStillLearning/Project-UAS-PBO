package com.proyek.coffeeshop.service.impl;

import com.proyek.coffeeshop.dto.request.LoginRequestDto;
import com.proyek.coffeeshop.dto.request.RegisterRequestDto;
import com.proyek.coffeeshop.dto.response.CustomerDto;
import com.proyek.coffeeshop.dto.response.UserResponseDto;
import com.proyek.coffeeshop.exception.BadRequestException;
import com.proyek.coffeeshop.exception.ResourceNotFoundException;
import com.proyek.coffeeshop.model.entity.Customer;
import com.proyek.coffeeshop.model.entity.User;
import com.proyek.coffeeshop.model.enums.UserRole;
import com.proyek.coffeeshop.repository.CustomerRepository;
import com.proyek.coffeeshop.repository.UserRepository;
import com.proyek.coffeeshop.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementasi service untuk operasi autentikasi.
 * Menangani registrasi dan login user.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDto registerCustomer(RegisterRequestDto request) {
        log.info("Registering new customer with username: {}", request.getUsername());

        // Validasi username dan email sudah ada atau belum
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username sudah terdaftar: " + request.getUsername());
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email sudah terdaftar: " + request.getEmail());
        }

        // Buat User baru
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(UserRole.ROLE_CUSTOMER);

        User savedUser = userRepository.save(user);

        // Buat Customer profile
        Customer customer = new Customer();
        customer.setUser(savedUser);
        customer.setFullName(request.getFullName());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setAddress(request.getAddress());

        Customer savedCustomer = customerRepository.save(customer);

        log.info("Successfully registered customer with ID: {}", savedCustomer.getCustomerId());

        return convertToUserResponseDto(savedUser, savedCustomer);
    }

    @Override
    public UserResponseDto login(LoginRequestDto request) {
        log.info("User login attempt with username: {}", request.getUsername());

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Username tidak ditemukan: " + request.getUsername()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Password salah");
        }

        Customer customer = null;
        if (user.getRole() == UserRole.ROLE_CUSTOMER) {
            customer = customerRepository.findByUser(user).orElse(null);
        }

        log.info("User {} successfully logged in", user.getUsername());

        return convertToUserResponseDto(user, customer);
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        log.info("Getting user by username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan: " + username));

        Customer customer = null;
        if (user.getRole() == UserRole.ROLE_CUSTOMER) {
            customer = customerRepository.findByUser(user).orElse(null);
        }

        return convertToUserResponseDto(user, customer);
    }

    /**
     * Konversi User dan Customer entity ke UserResponseDto.
     *
     * @param user User entity
     * @param customer Customer entity (bisa null untuk admin)
     * @return UserResponseDto
     */
    private UserResponseDto convertToUserResponseDto(User user, Customer customer) {
        CustomerDto customerDto = null;
        if (customer != null) {
            customerDto = new CustomerDto(
                    customer.getCustomerId(),
                    customer.getFullName(),
                    customer.getPhoneNumber(),
                    customer.getAddress()
            );
        }

        return new UserResponseDto(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                customerDto
        );
    }
}
