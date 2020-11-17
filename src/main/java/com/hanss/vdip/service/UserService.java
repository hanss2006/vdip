package com.hanss.vdip.service;

import com.hanss.vdip.common.mail.SendMailService;
import com.hanss.vdip.domain.Role;
import com.hanss.vdip.domain.User;
import com.hanss.vdip.repository.UserRepository;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service("userService")
public class UserService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    /**
     * Длина временного пароля.
     */
    public static final int TEMP_PASS_SIZE = 10;

    /**
     * Пользовательский репозиторий.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Сервис отправки е-мейлов.
     */
    @Autowired
    private SendMailService sendMailService;

    /**
     * @return User
     */
    public User getCurrentUserEntityFromContext() {
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equalsIgnoreCase("anonymousUser")) {
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return null;
    }

    /**
     * Сгенерировать пароль.
     * @return password
     */
    public static String generatePassword() {
        String symbols = "123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";
        String random = new Random().ints(TEMP_PASS_SIZE, 0, symbols.length())
                .mapToObj(symbols::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
        return random;
    }


    /**
     * Сообщение в письме о создании аккаунта2.
     */
    public static final String CREATE_ACCOUNT_MESSAGE2 = "Роль: %s.<br/>\n";


    /**
     * Выслать письмо о создании аккаунта пользователя.
     *
     * @param registeredUser пользователь
     * @param passwordPar
     */
    public void sendCreationEmail(final User registeredUser, final String passwordPar) throws Exception {

        String sURI = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .replacePath("/")
                .build().toUriString();

        StringBuilder sb = new StringBuilder();
        for (Iterator<Role> iterator = registeredUser.getRoles().iterator(); iterator.hasNext();) {
            Role userRole = iterator.next();
            sb.append(
                    String.format(
                            CREATE_ACCOUNT_MESSAGE2,
                            userRole.getName()
                    )
            );
        }

        Map templateModel = new HashMap();
        templateModel.put("fio", registeredUser.getUsername());
        templateModel.put("httpAddress", sURI);
        templateModel.put("login", registeredUser.getUsername());
        templateModel.put("password", passwordPar);
        templateModel.put("roles", sb.toString());
        String html = "";
        try {
            html = sendMailService.templater("create-user", templateModel);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        sendMailService.sendEmail(registeredUser.getUsername(), "Регистрация завершена", html);
    }

    /**
     * Сменить пароль пользователя.
     *
     * @param id       идентификатор пользователя
     * @param password пароль пользователя
     * @return User
     */
    public User changePassword(final Integer id, final String password) {
        User vo = userRepository.findById(id).get();
        vo.setPassword(password);
        return userRepository.save(vo);
    }

    /**
     * Сменить пароль текущего пользователя.
     *
     * @param oldPassword старый пароль пользователя
     * @param newPassword новый пароль пользователя
     */
    public void changeCurrentPassword(final String oldPassword, final String newPassword) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        User currentUser = userRepository.findByUsername(name);
        if (currentUser == null || !currentUser.isMatchesPassword(oldPassword)) {
            throw new Exception("BADCREDENTIALS");
        }
        currentUser.setPassword(newPassword);
        userRepository.save(currentUser);
    }

}
