package com.hanss.vdip.auth.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * DTO class for authentication (token) request.
 */
@Data
public class TokenRequestDTO {
    /**
     * Токен обновления.
     */
    @JsonProperty("refresh_token")
    private String refreshToken;
}
