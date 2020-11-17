//package com.hanss.vdip.api;
//
//import com.hanss.vdip.auth.AuthenticationService;
//import com.hanss.vdip.auth.dtos.AuthenticationRequestDto;
//import com.hanss.vdip.auth.dtos.AuthenticationResponseDto;
//import com.hanss.vdip.auth.dtos.TokenRequestDTO;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.security.auth.message.AuthException;
//
///**
// * REST controller for authentication requests (login, logout, register, etc.).
// */
//@Tag(name = "Сервис аутентификации", description = "API для идентификации пользователя")
//@RestController
//@RequiredArgsConstructor
//@RequestMapping(path = "/api")
//public class AuthenticationController {
//
//    /**
//     * Сервис аутентификации.
//     */
//    @Autowired
//    private final AuthenticationService authenticationService;
//
//    /**
//     * Конечная точка Логин.
//     *
//     * @param requestDto
//     * @return ResponseEntity
//     */
//    @Operation(summary = "Идентифициорваться в системе и получить тикет.")
//    @PostMapping("/auth/login")
//    public AuthenticationResponseDto login(@RequestBody final AuthenticationRequestDto requestDto)
//    {
//        return authenticationService.login(requestDto.getUsername(), requestDto.getPassword());
//    }
//
//    /**
//     * Выйти из системы.
//     *
//     * @return AuthenticationResponseDto
//     */
//    @Operation(summary = "Выйти из системы.")
//    @PostMapping("/auth/logout")
//    public AuthenticationResponseDto logout() throws AuthException {
//        return authenticationService.logout();
//    }
//
//
//    /**
//     * Конечная точка Обновить токин.
//     *
//     * @param requestDto
//     * @return AuthenticationResponseDto
//     */
//    @Operation(summary = "Запросить новый токен.")
//    @PostMapping("/auth/token")
//    public AuthenticationResponseDto token(@RequestBody final TokenRequestDTO requestDto) throws AuthException {
//        return authenticationService.token(requestDto.getRefreshToken());
//    }
//
//
//    /**
//     * Выслать ссылку на восстановление пароля.
//     * @param email
//     */
//    @Operation(summary = "Выслать ссылку на почту для восстановления пароля")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    @PostMapping(value = "/password/send-to")
//    public void linkToRecoveryPassword(
//            @Parameter(description = "email")
//            @RequestParam(required = true) final String email
//    ) throws Exception {
//        authenticationService.linkToRecoveryPassword(email);
//    }
//
//    /**
//     * Восстановить пароль.
//     *
//     * @param tikid
//     * @param newPassword
//     */
//    @Operation(summary = "Восстановление пароля.")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    @PostMapping("/password/recovery")
//    public void recoveryPassword(
//            @Parameter(description = "Идентификатор запроса на восстановление пароля")
//            @RequestParam(required = true) final String tikid,
//            @Parameter(description = "Новый пароль")
//            @RequestParam(required = true) final String newPassword
//    ) throws Exception {
//        authenticationService.changePassword(tikid, newPassword);
//    }
//
//    /**
//     * Изменить пароль.
//     *
//     * @param userName
//     * @param oldPassword
//     * @param newPassword
//     */
//    @Operation(summary = "Изменение пароля.")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    @PostMapping("/password/reset")
//    public void changePassword(
//            @Parameter(description = "Имя пользователя")
//            @RequestParam(required = true) final String userName,
//            @Parameter(description = "Старый пароль")
//            @RequestParam(required = true) final String oldPassword,
//            @Parameter(description = "Новый пароль")
//            @RequestParam(required = true) final String newPassword
//    ) throws Exception {
//        authenticationService.changePassword(userName, oldPassword, newPassword);
//    }
//
//}
