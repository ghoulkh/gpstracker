package com.bka.gpstracker.config;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.PrintWriter;

@Configuration
@Log4j2
public class FTPConfig {
    @Value("${ftp.hostname}")
    private String hostname;
    @Value("${ftp.username}")
    private String username;
    @Value("${ftp.password}")
    private String password;

    @Bean
    public FTPClient ftpClient() {
        FTPClient ftp = new FTPClient();

        try {
            ftp.connect(this.hostname, 21);
            ftp.enterLocalPassiveMode();
            int reply = ftp.getReplyCode();
            System.out.println(reply);

            ftp.login(this.username, this.password);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ftp;
    }
}
