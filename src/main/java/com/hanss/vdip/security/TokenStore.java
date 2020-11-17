package com.hanss.vdip.security;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.hanss.vdip.domain.Token;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Хранилище рефреш трокенов.
 */
@Service
public class TokenStore {

    /**
     * Хранилище.
     */
    private LoadingCache<UUID, Token> tokenCache;

    /**
     * Инициализация хранилища.
     */
    public TokenStore() {
        super();
        tokenCache = CacheBuilder.newBuilder().
                expireAfterWrite(5, TimeUnit.DAYS).build(new CacheLoader<UUID, Token>() {
            public Token load(final UUID key) {
                return null;
            }
        });
    }

    /**
     * Добавить токен.
     * @param key
     * @param token
     */
    public void putToken(final UUID key, Token token) {
        tokenCache.put(key, token);
    }

    /**
     * .
     * @param key
     * @return Token
     */
    public Token getToken(final UUID key) {
        try {
            return tokenCache.get(key);
        } catch (ExecutionException e) {
            return null;
        }
    }

}
