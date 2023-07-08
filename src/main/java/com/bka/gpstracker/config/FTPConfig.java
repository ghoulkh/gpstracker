package com.bka.gpstracker.config;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sun.net.ftp.FtpClient;

import java.io.PrintWriter;

@Configuration
@Log4j2
public class FTPConfig {

    @Bean
    public FTPClient ftpClient() {
        FTPClient ftp = new FTPClient();

        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        try {
            ftp.connect("gpstracker.somee.com", 21);
            ftp.enterLocalPassiveMode();
            int reply = ftp.getReplyCode();
            System.out.println(reply);

            ftp.login("gpstracker", "Daotak1310.");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ftp;
    }
}
