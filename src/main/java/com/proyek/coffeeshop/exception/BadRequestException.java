package com.proyek.coffeeshop.exception;

/**
 * Exception untuk request yang tidak valid.
 * Digunakan ketika input dari user tidak sesuai dengan business rules.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
