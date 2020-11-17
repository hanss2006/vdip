package com.hanss.vdip.auth.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * DTO class for authentication (login) response.
 */
@Data
@Builder
public class AuthenticationResponseDto {
    /**
     * Токен.
     */
    private String token;

    /**
     * Срок действия токена.
     */
    private Date expiration;

    /**
     * Токен обновления.
     */
    @JsonProperty("refresh_token")
    private String refreshToken;

    /**
     * Срок действия рефреш токена.
     */
    @JsonProperty("refresh_expiration")
    private Date refreshExpiration;

}
