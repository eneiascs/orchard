package com.eneiascs.orchard.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConfig {
	
	private static JwtConfig instance = null;
	
	@Value("${security.jwt.secret}")
	private String secret;
	
	private long expirationTime = 432_000_000;
	private String tokenPrefix = "Bearer ";
	private String headType = "Authorization";
	private String clientDomainUrl = "*";
	
	
	private JwtConfig() {
		
	}
	
	public static void initialize() {
		instance = new JwtConfig();
	}
	
	public static JwtConfig getInstance() {
		return instance;
	}
	
	public String getSecret() {
		return secret;
	}




	public long getExpirationTime() {
		return expirationTime;
	}




	public String getTokenPrefix() {
		return tokenPrefix;
	}




	public String getHeadType() {
		return headType;
	}




	public String getClientDomainUrl() {
		return clientDomainUrl;
	}
	
	

}
