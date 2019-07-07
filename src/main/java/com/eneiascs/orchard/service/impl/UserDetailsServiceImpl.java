package com.eneiascs.orchard.service.impl;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.eneiascs.orchard.model.AppUser;
import com.eneiascs.orchard.service.AccountService;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private AccountService accountService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser appUser = accountService.findByUsername(username);
		if(appUser == null) {
			throw new UsernameNotFoundException(String.format("Username %s not found", username));
		}
		
		Collection<GrantedAuthority> authorities = appUser.getUserRoles().stream().map(userRole -> new SimpleGrantedAuthority(appUser.getUserRoles().toString())).collect(Collectors.toList());
		
		
		return new User(appUser.getUsername(), appUser.getPassword(),  authorities);
	}

}
