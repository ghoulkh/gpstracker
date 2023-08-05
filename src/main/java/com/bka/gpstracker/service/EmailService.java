package com.bka.gpstracker.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Service
@Log4j2
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;


    private String messageNewDelivery(String deliveryId) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append("Bạn vừa có một đơn hàng\n" +
                "Mã vận đơn của bạn là: ").append(deliveryId).append("\n");
        return builder.toString();
    }

    private void sendSimpleMail(String deliveryId, String email) {
        try {

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(messageNewDelivery(deliveryId), false); // Use this or above line.
            helper.setTo(email);
            helper.setSubject("Đơn hàng của bạn đang được giao!");
            helper.setFrom("managermenthouse@gmail.com");
            javaMailSender.send(mimeMessage);
            log.info("Mail Sent Successfully...");
        } catch (Exception e) {
            log.error("Error while Sending Mail");
        }
    }

    public void sendMailNewDelivery(String deliveryId, String email) {
        sendSimpleMail(deliveryId, email);
    }

    @Async
    public void sendMailForgotPassword(String codeResetPass, String email, String username) {
        try {

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(messageForgotPasswordBuilder(codeResetPass, username), false); // Use this or above line.
            helper.setTo(email);
            helper.setSubject("Forgot password!!!");
            helper.setFrom("1203ddp@gmail.com");
            javaMailSender.send(mimeMessage);
            log.info("Mail Sent Successfully...");
        }

        catch (Exception e) {
            log.error("Error while Sending Mail");
        }
    }


    private String messageForgotPasswordBuilder(String codeResetPassword, String username) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append("Reset password\n" +
                "Link will expire within 5 minutes\n").append("Link: ");
        builder.append("http://gps.hust.id.vn/restpassword?code=").append(codeResetPassword);
        builder.append("&username=").append(username);
        return builder.toString();
    }

}