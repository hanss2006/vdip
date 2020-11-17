package com.hanss.vdip.security;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Количество неудачных попыток логинится.
 */
@Service
public class LoginAttemptService {

    /**
     * Количество неудачных попыток логиниться.
     */
    private final int maxAttempt = 5;
    /**
     * Хранилище.
     */
    private LoadingCache<String, Integer> attemptsCache;

    /**
     * Инициализация хранилища.
     */
    public LoginAttemptService() {
        super();
        attemptsCache = CacheBuilder.newBuilder().
                expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<String, Integer>() {
            public Integer load(final String key) {
                return 0;
            }
        });
    }

    /**
     * Логин успешен.
     * @param key
     */
    public void loginSucceeded(final String key) {
        attemptsCache.invalidate(key);
    }

    /**
     * Логин провален.
     * @param key
     */
    public void loginFailed(final String key) {
        int attempts = 0;
        try {
            attempts = attemptsCache.get(key);
        } catch (ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }

    /**
     * Количество попыток исчерпано.
     * @param key
     * @return boolean
     */
    public boolean isBlocked(final String key) {
        try {
            return attemptsCache.get(key) >= maxAttempt;
        } catch (ExecutionException e) {
            return false;
        }
    }

}
