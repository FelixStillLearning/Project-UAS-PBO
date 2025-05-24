package com.proyek.coffeeshop.dto.response;

import com.proyek.coffeeshop.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO untuk response data user.
 * Berisi informasi user beserta detail customer jika ada.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private Long userId;
    private String username;
    private String email;
    private UserRole role;
    private CustomerDto customerDetails;
}
