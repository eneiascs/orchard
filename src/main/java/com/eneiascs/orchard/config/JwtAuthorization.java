package com.eneiascs.orchard.config;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtAuthorization extends OncePerRequestFilter {
	
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin", SecurityConstants.CLIENT_DOMAIN_URL);
		response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, "
		+ "Content-Type, Access-Control-Request-Method, "
		+ "Access-ControlRequest-Headers, Authorization");
		response.addHeader("Access-Control-Expose-Headers", 
				"Access-Control-Allow-Origin, " + "Access-Control-Allow-Credentials, " +"Authorization"
				);
		response.addHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,PUT");
		
		if(request.getMethod().equalsIgnoreCase("OPTIONS")) {
			try {
				response.setStatus(HttpServletResponse.SC_OK);
				return;
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}else {
			String jwtToken = request.getHeader(SecurityConstants.HEAD_TYPE);
			if(jwtToken == null) {
				filterChain.doFilter(request, response);
				return;
			}
			if(jwtToken != null && !jwtToken.startsWith(SecurityConstants.TOKEN_PREFIX)) {
				filterChain.doFilter(request, response);
				return;
			}
			JWT.require(Algorithm.HMAC256(JwtConfig.getInstance().getSecret()));
			DecodedJWT jwt = JWT.decode(jwtToken.substring(SecurityConstants.TOKEN_PREFIX.length()));
			String username = jwt.getSubject();
			List<String> roles = jwt.getClaims().get("roles").asList(String.class);
			Collection<GrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
			UsernamePasswordAuthenticationToken authenticatedUser = new UsernamePasswordAuthenticationToken(username,  null, authorities);
			SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
			filterChain.doFilter(request, response);
		}
		
	}
}
