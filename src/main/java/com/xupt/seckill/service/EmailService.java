package com.xupt.seckill.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @author maxu
 * @date 2019/1/28
 */
@Service
@Slf4j
public class EmailService {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender javaMailSender;


    @Autowired
    private TemplateEngine templateEngine;

    public void s() throws MessagingException {
        Context context = new Context();
        context.setVariable("id","006");
        String emailContent = templateEngine.process("emailTemplate", context);
        sendHtmlMail("","",emailContent);
    }

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

    public void sendAttachmentMail(String to, String subject, String content,String filePath) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
        helper.setTo(to);
        helper.setFrom(from);
        helper.setText(content,true);
        FileSystemResource file = new FileSystemResource(new File(filePath));
        String fileName = file.getFilename();
        helper.addAttachment(fileName,file);
        helper.addAttachment(fileName+"_test",file);
        helper.addAttachment(fileName+"_test1",file);
        javaMailSender.send(mimeMessage);
    }

    public void sendInlineResourceMail(String to, String subject, String content,String rscPath,String rscId) {
        log.info("发送邮件参数：{},{},{},{},{}", to, subject, content, rscPath, rscId);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMessage,true);
            helper.setTo(to);
            helper.setFrom(from);
            helper.setText(content,true);
            FileSystemResource res = new FileSystemResource(new File(rscPath));
            helper.addInline(rscId,res);
            javaMailSender.send(mimeMessage);
            log.info("发送成功");
        } catch (MessagingException e) {
            log.error("发送失败",e);
        }


    }
}
