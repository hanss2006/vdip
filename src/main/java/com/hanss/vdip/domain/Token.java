package com.hanss.vdip.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

/**
 * Зарегистрированные в системе токены.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Token {
    /**
     * Имя владельца.
     */
    private String sub;

    /**
     * Дата создания токена.
     */
    private Date iat;

    /**
     * Дата устаревания токена.
     */
    private Date exp;

    /**
     * Адрес владельца рефреша.
     */
    private String primIp;

    /**
     * Адрес запроса тикета.
     */
    private String secIp;
}
