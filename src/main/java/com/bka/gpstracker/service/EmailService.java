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

}