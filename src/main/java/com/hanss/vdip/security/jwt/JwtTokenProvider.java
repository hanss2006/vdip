//package com.hanss.vdip.security.jwt;
//
//import com.hanss.vdip.auth.dtos.AuthenticationResponseDto;
//import com.hanss.vdip.domain.Role;
//import com.hanss.vdip.domain.Token;
//import com.hanss.vdip.security.TokenStore;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.JwtParser;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.Jws;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.annotation.PostConstruct;
//import javax.security.auth.message.AuthException;
//import javax.servlet.http.HttpServletRequest;
//import java.util.Base64;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Date;
//import java.util.UUID;
//import java.util.concurrent.atomic.AtomicBoolean;
//
///**
// * Util class that provides methods for generation, validation, etc. of JWT token.
// */
//@Component
//public class JwtTokenProvider {
//
//    /**
//     * Позиция с которой в хедере начинается токен.
//     */
//    public static final int BEGIN_INDEX = 7;
//
//    /**
//     * Наименование скопа обновления токена.
//     */
//    public static final String ROLE_REFRESH_TOKEN = "ROLE_REFRESH_TOKEN";
//
//    /**
//     * Роли.
//     */
//    public static final String ROLES = "roles";
//
//    /**
//     * Ключ.
//     */
//    @Value("${jwt.token.secret:jwtsecret}")
//    private String secret;
//
//    /**
//     * Ключ устаревает через.
//     */
//    @Value("${jwt.token.expired:3600000}")
//    private long validityInMilliseconds;
//
//    /**
//     * Ключ обновления устаревает через.
//     */
//    @Value("${jwt.refreshtoken.expired:36000000}")
//    private long validityRefreshInMilliseconds;
//
//    /**
//     * Сервис информации о пользователе.
//     */
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    /**
//     * repository.
//     */
//    @Autowired
//    private TokenStore tokenRepository;
//
//
//    /**
//     * Срок действия токена.
//     */
//    private Date validity = null;
//
//    /**
//     * Срок действия рефреш токена.
//     */
//    private Date refreshExpiration = null;
//
//
//    /**
//     * Идентификатор рефреша.
//     */
//    private UUID idRefToken = null;
//
//
//    /**
//     * Инициализация ключа.
//     */
//    @PostConstruct
//    protected void init() {
//        secret = Base64.getEncoder().encodeToString(secret.getBytes());
//    }
//
//    /**
//     * Создать тикет.
//     *
//     * @param username
//     * @param roles
//     * @return Токен
//     */
//    public String createToken(final String username, final List<String> roles) {
//
//        Claims claims = Jwts.claims().setSubject(username);
//        claims.put(ROLES, roles);
//
//        Date now = new Date();
//        this.validity = new Date(now.getTime() + validityInMilliseconds);
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setId(this.idRefToken.toString())
//                .setIssuedAt(now)
//                .setExpiration(this.validity)
//                .signWith(SignatureAlgorithm.HS256, secret)
//                .compact();
//    }
//
//    /**
//     * Создать новый рефреш по тикету обновления.
//     *
//     * @param refreshToken
//     * @return Токен
//     */
//    public AuthenticationResponseDto refreshToken(final String refreshToken) throws AuthException {
//        if (refreshToken.isEmpty()) {
//            throw new IllegalArgumentException("Invalid refreshToken");
//        }
//        if (!validateToken(refreshToken)) {
//            throw new AuthException("REFRESH_TOKEN_EXPIRED");
//        }
//
//        JwtParser jwtParser = Jwts.parser().setSigningKey(secret);
//        Claims claims = jwtParser.parseClaimsJws(refreshToken).getBody();
//        String username = claims.getSubject();
//        UUID idToken = UUID.fromString(claims.getId());
//        ArrayList roles = (ArrayList) claims.get(ROLES);
//        ArrayList scopes = (ArrayList) claims.get("scopes");
//        AtomicBoolean isInRefreshScope = new AtomicBoolean(false);
//        scopes.forEach(scope -> {
//            if (scope.equals(ROLE_REFRESH_TOKEN)) {
//                isInRefreshScope.set(true);
//            }
//        });
//        Token token = touchRefresh(idToken);
//        if (!isInRefreshScope.get() || token == null || !checkRefreshExpiration(token.getExp())) {
//            throw new AuthException("UNAUTHORIZED");
//        }
//        return buildAuthenticationAnswer(username, roles);
//    }
//
//    /**
//     * Создать ответ за запрос аутентификации.
//     *
//     * @param username
//     * @param roles
//     * @return AuthenticationResponseDto
//     */
//    public AuthenticationResponseDto buildAuthenticationAnswer(final String username, final List roles) {
//        String refreshToken = createRefreshToken(username, roles);
//        String token = createToken(username, roles);
//        return AuthenticationResponseDto.
//                builder().token(token).expiration(this.validity).refreshToken(refreshToken).
//                refreshExpiration(this.refreshExpiration).build();
//    }
//
//    /**
//     * Выйти из системы.
//     */
//    public void logout() {
//        if (null == this.idRefToken) {
//            return;
//        }
//        Token savedToken = tokenRepository.getToken(this.idRefToken);
//        if (savedToken!=null) {
//            // Принудительно завершаем срок действия рефреша
//            savedToken.setExp(new Date());
//            tokenRepository.putToken(this.idRefToken, savedToken);
//        }
//    }
//
//    /**
//     * Проверить дату действия токена.
//     *
//     * @param expDate
//     * @return Действует ли еще токен
//     */
//    private boolean checkRefreshExpiration(final Date expDate) {
//        boolean returnValue = true;
//        if (expDate != null) {
//            // Рефреш просроченный
//            if (expDate.before(new Date())) {
//                returnValue = false;
//            }
//        } else {
//            returnValue = false;
//        }
//        return returnValue;
//    }
//
//
//    /**
//     * Получение срока действия _одноразового_ токена (токен помечается затем ивалидным).
//     *
//     * @param idToken
//     * @return Данные сохраненного токена
//     */
//    private Token touchRefresh(final UUID idToken) {
//        Token token = tokenRepository.getToken(idToken);
//        Token returnValue = null;
//        if (token!=null) {
//            returnValue = token.toBuilder().build();
//            // Принудительно завершаем срок действия рефреша
//            token.setExp(new Date());
//            tokenRepository.putToken(idToken, token);
//        }
//        return returnValue;
//    }
//
//
//    /**
//     * Создать тикет обновления.
//     *
//     * @param username
//     * @param roles
//     * @return Токен
//     */
//    public String createRefreshToken(final String username, final List<String> roles) {
//        if (username.isEmpty()) {
//            throw new IllegalArgumentException("Cannot create JWT Token without username");
//        }
//        this.idRefToken = UUID.randomUUID();
//        Date now = new Date();
//        this.refreshExpiration = new Date(now.getTime() + validityRefreshInMilliseconds);
//        String ipAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
//                .getRequest().getRemoteAddr();
//        Claims claims = Jwts.claims().setSubject(username);
//        claims.put("scopes", getRefreshScope());
//        claims.put("roles", roles);
//
//        String token = Jwts.builder()
//                .setClaims(claims)
//                .setId(this.idRefToken.toString())
//                .setIssuedAt(now)
//                .setExpiration(this.refreshExpiration)
//                .signWith(SignatureAlgorithm.HS256, secret)
//                .compact();
//        tokenRepository.putToken(this.idRefToken, new Token(username, now, this.refreshExpiration, ipAddress, null));
//        return token;
//    }
//
//    /**
//     * Вернуть аутонтификацию.
//     *
//     * @param token
//     * @return AuthenticationToken
//     */
//    public Authentication getAuthentication(final String token) {
//        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }
//
//    /**
//     * Возвратить логин по токену.
//     *
//     * @param token
//     * @return Username
//     */
//    public String getUsername(final String token) {
//        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
//    }
//
//    /**
//     * Извлечь токен из заголовка запроса.
//     *
//     * @param req
//     * @return Токен
//     */
//    public String resolveToken(final HttpServletRequest req) {
//        String bearerToken = req.getHeader("Authorization");
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(BEGIN_INDEX, bearerToken.length());
//        }
//        return null;
//    }
//
//    /**
//     * Проверить токен.
//     *
//     * @param token
//     * @return Результат проверки
//     */
//    public boolean validateToken(final String token) {
//        try {
//            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
//            return !claims.getBody().getExpiration().before(new Date());
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    /**
//     * Получить список ролей.
//     *
//     * @param userRoles
//     * @return список ролей
//     */
//    public static List<String> getRoleNames(final List<Role> userRoles) {
//        List<String> result = new ArrayList<>();
//
//        userRoles.forEach(role -> {
//            result.add(role.getName());
//        });
//
//        return result;
//    }
//
//    /**
//     * Получить скоп для рефреша.
//     *
//     * @return Список скопа обновления токена
//     */
//    private List<String> getRefreshScope() {
//        List<String> result = new ArrayList<>();
//
//        result.add(ROLE_REFRESH_TOKEN);
//
//        return result;
//    }
//
//    /**
//     * Создать тикет восстановления пароля.
//     *
//     * @param username
//     * @return Идентификатор токена
//     */
//    public String createRecoveryPasswordToken(final String username) {
//        if (username.isEmpty()) {
//            throw new IllegalArgumentException("Cannot create recovery password token without username");
//        }
//        this.idRefToken = UUID.randomUUID();
//        Date now = new Date();
//        this.refreshExpiration = new Date(now.getTime() + validityRefreshInMilliseconds);
//        String ipAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
//                .getRequest().getRemoteAddr();
//        tokenRepository.putToken(this.idRefToken, new Token(username, now, this.refreshExpiration, ipAddress, null));
//        return this.idRefToken.toString();
//    }
//
//    /**
//     * Получить имя пользователя по идентификатору тикетапользователя .
//     *
//     * @param tokenId
//     * @return Идентификатор токена
//     */
//    public String getUsernameByTokenId(final String tokenId) throws AuthException {
//        if (tokenId.isEmpty()) {
//            throw new AuthException("BADCREDENTIALS");
//        }
//
//        Token token = touchRefresh(UUID.fromString(tokenId));
//        if (token == null) {
//            throw new AuthException("BADCREDENTIALS");
//        }
//        if (!checkRefreshExpiration(token.getExp())) {
//            throw new AuthException("CREDENTIALSEXPIRED");
//        }
//        return token.getSub();
//    }
//}
