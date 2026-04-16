package com.revision_platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RevisionPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(RevisionPlatformApplication.class, args);
	}

}
