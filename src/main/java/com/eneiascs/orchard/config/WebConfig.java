package com.eneiascs.orchard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.thymeleaf.TemplateEngine;

@Configuration
public class WebConfig {

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	    return bCryptPasswordEncoder;
	}
	
	@Bean
	public TemplateEngine templateEngine() {
	    TemplateEngine templateEngine = new TemplateEngine();
	    return templateEngine;
	}
	
	@Bean
	public JwtConfig jwtConfiguration() {
	    JwtConfig.initialize();
	    return JwtConfig.getInstance();
	}
	
}
