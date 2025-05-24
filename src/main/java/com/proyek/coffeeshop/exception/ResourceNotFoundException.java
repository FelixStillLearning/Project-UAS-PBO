package com.proyek.coffeeshop.exception;

/**
 * Exception untuk resource yang tidak ditemukan.
 * Digunakan ketika mencari data berdasarkan ID atau kriteria tertentu tidak ditemukan.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
