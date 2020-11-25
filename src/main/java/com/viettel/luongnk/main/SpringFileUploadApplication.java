package com.viettel.luongnk.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class SpringFileUploadApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringFileUploadApplication.class, args);
	}

}
