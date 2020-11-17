package com.hanss.vdip.config;

import com.hanss.vdip.security.CustomAccessDeniedHandler;
import com.hanss.vdip.security.CustomAuthenticationEntryPoint;
//import com.hanss.vdip.security.jwt.JwtConfigurer;
//import com.hanss.vdip.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Security configuration class for JWT based Spring Security application.
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Поставщик тонинов.
     */
    //private final JwtTokenProvider jwtTokenProvider;

    /**
     * Аутентификация.
     */
    private static final String LOGIN_ENDPOINT = "/api/auth/login";

    /**
     * Выслать ссылку для восстановления пароля.
     */
    private static final String LINKTORECOVERYPASSWORD = "/api/password/send-to";

    /**
     * Восстановить пароль.
     */
    private static final String RECOVERYPASSWORD = "/api/password/recovery";

    /**
     * Изменить пароль.
     */
    private static final String CHANGEPASSWORD = "/api/password/reset";

    /**
     * Выход из системы.
     */
    public static final String LOGOUT_ENDPOINT = "/api/auth/logout";

    /**
     * Обновление токина.
     */
    public static final String TOKEN_REFRESH_ENTRY_POINT = "/api/auth/token";

    /**
     * Документация.
     */
    private static final String[] SWAGGER_ENDPOINTS = {"/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"};

    /**
     * Конструктор.
     *
     * @param jwtTokenProviderPar
     */
//    @Autowired
//    public SecurityConfig(final JwtTokenProvider jwtTokenProviderPar) {
//        this.jwtTokenProvider = jwtTokenProviderPar;
//    }

    /**
     * Кодировщик паролей.
     *
     * @return bCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Менеджер аутентификации.
     *
     * @return authenticationManagerBean
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Обработчик ошибок аутентификации.
     *
     * @return бин
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    /**
     * Обработчик обращения к запрещенным ресурсам.
     *
     * @return бин
     */
    @Bean
    public CustomAccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    /**
     * Конфигурировать систему безопасности.
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(LOGIN_ENDPOINT).permitAll()
                .antMatchers(LINKTORECOVERYPASSWORD).permitAll()
                .antMatchers(RECOVERYPASSWORD).permitAll()
                .antMatchers(CHANGEPASSWORD).permitAll()
                .antMatchers(TOKEN_REFRESH_ENTRY_POINT).permitAll() // Token refresh end-point
                .antMatchers(SWAGGER_ENDPOINTS).permitAll()
                //.anyRequest().authenticated()
                .anyRequest().permitAll()
                .and().exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler());
//                .and()
//                .apply(new JwtConfigurer(jwtTokenProvider));

    }

}
