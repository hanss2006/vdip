package com.hanss.vdip.common.mail;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

/**
 * Серсис рассылки почты.
 */
@Component
@Slf4j
public class SendMailService {
    /**
     * Хост e-mail сервера.
     */
    @Value("${mail.host}")
    private String mailHost;

    /**
     * Порт e-mail сервера.
     */
    @Value("${mail.port}")
    private String mailPort;

    /**
     * Логин e-mail сервера.
     */
    @Value("${mail.username}")
    private String mailUsername;

    /**
     * Пароль e-mail сервера.
     */
    @Value("${mail.password}")
    private String mailPassword;

    /**
     * Адрес от кого письмо.
     */
    @Value("${mail.default.from.email}")
    private String fromMail;

    /**
     * Аутонтификация.
     */
    @Value("${mail.smtp.auth}")
    private String auth;

    /**
     * Шаблонизатор е-мейлов.
     */
//    @Autowired
//    private FreeMarkerConfigurationFactoryBean freeMarkerConfiguration;



//    /**
//     * Настройка STARTTLS-протокола.
//     */
//    @Value("${mail.smtp.starttls.enable}")
//    private String mailSmtpStarttlsEnable;
//
//    /**
//     * Настройка дебаг-режима.
//     */
//    @Value("${mail.debug}")
//    private String mailDebug;
//
//    /**
//     * Настройка SSL.
//     */
//    @Value("${mail.ssl}")
//    private static String mailSsh;

    /**
     * Послать емейл.
     *
     * @param toEmailId
     * @param mailSubject
     * @param message
     */
    public void sendEmail(final String toEmailId, final String mailSubject, final String message) {
//        this.sendEmail(this.fromMail, toEmailId, this.mailHost,
//                this.mailUsername, this.mailPassword, mailSubject, message);
    }

    /**
     * Encode the URL.
     *
     * @param value
     * @return String
     * @exception CommonException
     */
    public static String encodeValue(final String value) throws Exception {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException mex) {
            throw new Exception("INTERNALERROR");
        }
    }

    /**
     * Послать емейл.
     *
     * @param fromEmailId
     * @param toEmailId
     * @param host
     * @param hostUserName
     * @param hostPassword
     * @param mailSubject
     * @param mailBody
     */
    public void sendEmail(final String fromEmailId, final String toEmailId, final String host,
                                     final String hostUserName, final String hostPassword, final String mailSubject,
                                     final String mailBody) {
        /*
        // Get system properties.
        Properties props = System.getProperties();
        // Setup mail server
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", this.mailPort);
        props.put("mail.smtp.auth", this.auth);

        final String hostUName = hostUserName;
        final String hPassword = hostPassword;

        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(hostUName, hPassword);
            }
        };

        // Get the default Session object.
        Session session = Session.getDefaultInstance(props, authenticator);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(fromEmailId));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmailId));

            // Set Subject: header field
            message.setSubject(mailSubject);

            // Send the actual HTML message, as big as you like
            // message.setContent(mailBody, "text/html");
            message.setContent(mailBody, "text/html; charset=UTF-8");
            //messageBodyPart.setContent(mailBody, "text/html; charset=UTF-8");

            // Send message
            Transport.send(message, message.getAllRecipients());
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

         */

    }

    /**
     * Шаблонизатор текстов писем.
     * @param tempateName Имя шаблона
     * @param templateModel Данные для шаблона
     * @return Текст письма
     */
    public String templater(final String tempateName, final Map templateModel) throws IOException, TemplateException {
//        Configuration freeMarkerConfig = freeMarkerConfiguration.createConfiguration();
//        TemplateLoader templateLoader = new ClassTemplateLoader(this.getClass(), "/templates");
//        freeMarkerConfig.setTemplateLoader(templateLoader);
//        Template template = freeMarkerConfig.getTemplate(tempateName + ".html");
//        return FreeMarkerTemplateUtils.processTemplateIntoString(template, templateModel);
        return "";
    }
}
