package com.hanss.vdip.auth.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO class for authentication (login) request.
 */
@Data
public class AuthenticationRequestDto {
    /**
     * Логин.
     */
    @Schema(example = "admin")
    private String username;

    /**
     * Пароль.
     */
    @Schema(example = "123")
    private String password;
}
