package com.proyek.coffeeshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO untuk response data kategori.
 * Berisi informasi kategori produk.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private Long categoryId;
    private String name;
}
