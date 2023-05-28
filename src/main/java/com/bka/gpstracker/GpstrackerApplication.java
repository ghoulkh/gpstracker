package com.bka.gpstracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GpstrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GpstrackerApplication.class, args);
	}

}
