//package com.hanss.vdip.auth;
//
//import com.hanss.vdip.common.mail.SendMailService;
//import com.hanss.vdip.domain.User;
//import com.hanss.vdip.repository.UserRepository;
//import com.hanss.vdip.security.LoginAttemptService;
//import com.hanss.vdip.auth.dtos.AuthenticationResponseDto;
//import com.hanss.vdip.security.jwt.JwtTokenProvider;
//import com.hanss.vdip.service.UserService;
//import freemarker.template.TemplateException;
//import lombok.AllArgsConstructor;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AccountExpiredException;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.CredentialsExpiredException;
//import org.springframework.security.authentication.DisabledException;
//import org.springframework.security.authentication.LockedException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//
//import javax.security.auth.message.AuthException;
//import java.io.IOException;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * Сервси аутентификации.
// */
//@AllArgsConstructor
//@Service
//@Slf4j
//public class AuthenticationService {
//
//    /**
//     * Сообщение в письме восстановления пароля.
//     */
//    public static final String REVOVERY_MESSAGE = "Добрый день!<br/> <a href=\"%s\">Ссылка для восстановления пароля</a>";
//    /**
//     * Менеджер аутентификации.
//     */
//    private final AuthenticationManager authenticationManager;
//
//    /**
//     * Провайдер токенов.
//     */
//    private final JwtTokenProvider jwtTokenProvider;
//
//    /**
//     * Пользовательский сервис.
//     */
//    private final UserService userService;
//
//    /**
//     * Пользовательский репозиторий.
//     */
//    private final UserRepository userRepository;
//
//    /**
//     * Сервис отправки е-мейлов.
//     */
//    @Autowired
//    private SendMailService sendMailService;
//
//    /**
//     * Подсчет неудачных попыток логина.
//     */
//    @Autowired
//    private LoginAttemptService loginAttemptService;
//
//    /**
//     * Кодировщик паролей.
//     */
//    private final BCryptPasswordEncoder passwordEncoder;
//
//
//    /**
//     * Логин.
//     *
//     * @param username    логин
//     * @param password пароль
//     * @return ответ
//     */
//    @SneakyThrows
//    public AuthenticationResponseDto login(final String username, final String password) {
//        try {
//            UsernamePasswordAuthenticationToken authReq
//                    = new UsernamePasswordAuthenticationToken(username, password);
//            Authentication auth = authenticationManager.authenticate(authReq);
//            loginAttemptService.loginSucceeded(username);
//            List<String> roles =
//                    auth.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
//            return jwtTokenProvider.buildAuthenticationAnswer(username,  roles);
//        } catch (DisabledException e) {
//            throw new AuthException("ACCOUNTDISABLED");
//        } catch (CredentialsExpiredException e) {
//            throw new AuthException("CREDENTIALSEXPIRED");
//        } catch (LockedException e) {
//            throw new AuthException("ACCOUNTLOCKED");
//        } catch (AccountExpiredException e) {
//            throw new AuthException("ACCOUNTEXPIRED");
//        } catch (BadCredentialsException e) {
//            loginAttemptService.loginFailed(username);
//            throw new AuthException("BADCREDENTIALS");
//        } catch (Exception e) {
//            throw new AuthException("BADCREDENTIALS");
//        }
//
//    }
//
//    /**
//     * Проверить дату действия.
//     *
//     * @param expDate
//     * @return Действует ли еще объект
//     */
//    private boolean checkExpiration(final Date expDate) {
//        boolean returnValue = true;
//        if (expDate != null && expDate.before(new Date())) {
//                returnValue = false;
//        }
//        return returnValue;
//    }
//
//    /**
//     * Выйти из системы.
//     * @return Пустой токен
//     */
//    public AuthenticationResponseDto logout() throws AuthException {
//        try {
//            jwtTokenProvider.logout();
//            return AuthenticationResponseDto.
//                    builder().token("").refreshToken("").build();
//        } catch (DisabledException e) {
//            log.error(e.getMessage(), e);
//            throw new AuthException("ACCOUNTDISABLED");
//        } catch (CredentialsExpiredException e) {
//            log.error(e.getMessage(), e);
//            throw new AuthException("CREDENTIALSEXPIRED");
//        } catch (LockedException e) {
//            log.error(e.getMessage(), e);
//            throw new AuthException("ACCOUNTLOCKED");
//        } catch (AccountExpiredException e) {
//            log.error(e.getMessage(), e);
//            throw new AuthException("ACCOUNTEXPIRED");
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            throw new AuthException("BADCREDENTIALS");
//        }
//
//    }
//
//    /**
//     * Получение нового токена по рефрешу.
//     *
//     * @param refreshToken    рефреш токен
//     * @return новый токен аутентификации
//     */
//    public AuthenticationResponseDto token(final String refreshToken) throws AuthException {
//        try {
//            return jwtTokenProvider.refreshToken(refreshToken);
//        } catch (DisabledException e) {
//            log.error(e.getMessage(), e);
//            throw new AuthException("ACCOUNTDISABLED");
//        } catch (CredentialsExpiredException e) {
//            log.error(e.getMessage(), e);
//            throw new AuthException("CREDENTIALSEXPIRED");
//        } catch (LockedException e) {
//            log.error(e.getMessage(), e);
//            throw new AuthException("ACCOUNTLOCKED");
//        } catch (AccountExpiredException e) {
//            log.error(e.getMessage(), e);
//            throw new AuthException("ACCOUNTEXPIRED");
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            throw new AuthException("BADCREDENTIALS");
//        }
//
//    }
//
//    /**
//     * Выслать ссылку для смены пароля.
//     *
//     * @param username Емейл пользователя
//     */
//    public void linkToRecoveryPassword(final String username) throws Exception {
//        User user = (User) userService.loadUserByUsername(username);
//        String tokenId = jwtTokenProvider.createRecoveryPasswordToken(username);
//
//        String sURI = ServletUriComponentsBuilder.fromCurrentRequestUri()
//                .replacePath("/login/reset_password?secret=" + SendMailService.encodeValue(tokenId))
//                .build().toUriString();
//
//        sendResetEmail(user, sURI);
//    }
//
//    /**
//     * Выслать письмо о сбросе пароля пользователя.
//     *
//     * @param user пользователь
//     * @param sURI
//     */
//    public void sendResetEmail(final User user, final String sURI) {
//        Map templateModel = new HashMap();
//        templateModel.put("fio", user.getUsername());
//        templateModel.put("resetpasswordlink", sURI);
//        templateModel.put("login", user.getUsername());
//        String html = "";
//        try {
//            html = sendMailService.templater("reset-password", templateModel);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (TemplateException e) {
//            e.printStackTrace();
//        }
//        sendMailService.sendEmail(user.getUsername(), "Восстановление пароля", html);
//    }
//
//    /**
//     * Восстановление пароля по идентификатору заявки.
//     *
//     * @param tikid идентификатор заявки
//     * @param newPassword новый пароль
//     */
//    public void changePassword(final String tikid, final String newPassword) throws Exception {
//        String username = jwtTokenProvider.getUsernameByTokenId(tikid);
//        User user = (User) userService.loadUserByUsername(username);
//        checkPassword(newPassword);
//        userService.changePassword(user.getId(), newPassword);
//        sendChangeEmail(user);
//    }
//
//    /**
//     * Изменение пароля по имени пользователя и старому паролю.
//     *
//     * @param userName имя пользователя
//     * @param oldPassword старый пароль
//     * @param newPassword новый пароль
//     */
//    public void changePassword(final String userName, final String oldPassword, final String newPassword) throws Exception {
//        User user = (User) userService.loadUserByUsername(userName);
//        if (!user.isMatchesPassword(oldPassword)) {
//            throw new AuthException("BADCREDENTIALS");
//        }
//        checkPassword(newPassword);
//        userService.changePassword(user.getId(), newPassword);
//        sendChangeEmail(user);
//    }
//
//    /**
//     * Выслать письмо о смене пароля пользователя.
//     *
//     * @param user пользователь
//     */
//    public void sendChangeEmail(final User user) {
//        Map templateModel = new HashMap();
//        templateModel.put("fio", user.getUsername());
//        templateModel.put("login", user.getUsername());
//        String expired =  "не ограничен";
//        templateModel.put("passwordexpared", expired);
//        String html = "";
//        try {
//            html = sendMailService.templater("change-password", templateModel);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (TemplateException e) {
//            e.printStackTrace();
//        }
//        sendMailService.sendEmail(user.getUsername(), "Пароль успешно изменен", html);
//    }
//
//    @SuppressWarnings("checkstyle:magicnumber")
//    private void checkPassword(final String pass) throws Exception {
//        String regexp = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!?,:;@#%]).*$";
//        if  (!(StringUtils.hasText(pass) && pass.length() > 7 && pass.matches(regexp))) {
//            throw new Exception("INSECURE_PASSWORD");
//        }
//    }
//
//}
