package com.codex.codex_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.codex.codex_api.repositories")

public class CodexApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodexApiApplication.class, args);
	}

}
