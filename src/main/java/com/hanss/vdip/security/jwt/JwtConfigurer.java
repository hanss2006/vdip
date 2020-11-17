//package com.hanss.vdip.security.jwt;
//
//import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.DefaultSecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
///**
// * JWT configuration for application that add {@link JwtTokenFilter} for security chain.
// */
//public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
//    /**
//     * Провайдер токенов.
//     */
//    private JwtTokenProvider jwtTokenProvider;
//
//    /**
//     * Конструктор.
//     * @param jwtTokenProviderPar
//     */
//    public JwtConfigurer(final JwtTokenProvider jwtTokenProviderPar) {
//        this.jwtTokenProvider = jwtTokenProviderPar;
//    }
//
//    /**
//     * Конфигурировать.
//     * @param httpSecurity
//     * @throws Exception
//     */
//    @Override
//    public void configure(final HttpSecurity httpSecurity) throws Exception {
//        JwtTokenFilter jwtTokenFilter = new JwtTokenFilter(jwtTokenProvider);
//        httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
//    }
//}
