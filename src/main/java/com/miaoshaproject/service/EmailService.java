package com.miaoshaproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author maxu
 * @date 2019/1/28
 */
@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(content);
        mailMessage.setFrom(from);
        javaMailSender.send(mailMessage);

    }

    public void sendHtmlMail(String to, String subject, String content) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
        helper.setTo(to);
        helper.setFrom(from);
        helper.setText(content,true);
        javaMailSender.send(mimeMessage);
    }
}
