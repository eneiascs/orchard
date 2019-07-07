package com.eneiascs.orchard.config;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.eneiascs.orchard.model.AppUser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtAuthentication extends UsernamePasswordAuthenticationFilter {

	
	private AuthenticationManager authenticationManager;

	
	public JwtAuthentication(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		AppUser appUser = null;
		try {
			appUser = new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new RuntimeException("Unable to convert Json to Java object");
		} 
		return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(appUser.getUsername(), appUser.getPassword()));
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {
		JwtConfig jwtConfig = JwtConfig.getInstance();

		User user = (User) authentication.getPrincipal();
		List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
		
		String jwtToken = JWT.create()
				.withIssuer(request.getRequestURI())
				.withSubject(user.getUsername())
				.withArrayClaim("roles", roles.toArray(new String[roles.size()]))
				.withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.sign(Algorithm.HMAC256(jwtConfig.getSecret()));
		response.addHeader(SecurityConstants.HEAD_TYPE, SecurityConstants.TOKEN_PREFIX + jwtToken);
	}
}
