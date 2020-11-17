//package com.hanss.vdip.security.jwt;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.GenericFilterBean;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
///**
// * JWT token filter that handles all HTTP requests to application.
// */
//public class JwtTokenFilter extends GenericFilterBean {
//    /**
//     * Провайдер токенов.
//     */
//    private JwtTokenProvider jwtTokenProvider;
//
//    /**
//     * Конструктор.
//     * @param jwtTokenProviderPar
//     */
//    public JwtTokenFilter(final JwtTokenProvider jwtTokenProviderPar) {
//        this.jwtTokenProvider = jwtTokenProviderPar;
//    }
//
//    @Override
//    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain filterChain)
//            throws IOException, ServletException {
//
//        String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
//        if (token != null && jwtTokenProvider.validateToken(token)) {
//            Authentication auth = jwtTokenProvider.getAuthentication(token);
//
//            if (auth != null) {
//                SecurityContextHolder.getContext().setAuthentication(auth);
//            }
//        }
//        filterChain.doFilter(req, res);
//    }
//
//}
