package com.hanss.vdip.auth.dtos;

import lombok.Builder;
import lombok.Data;

/**
 * DTO class for authentication (token) response.
 */
@Data
@Builder
public class TokenResponseDTO {
    /**
     * Токен.
     */
    private String token;

}
