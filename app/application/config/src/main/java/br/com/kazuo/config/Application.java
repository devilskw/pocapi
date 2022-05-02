package br.com.kazuo.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "br.com.kazuo.**")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
