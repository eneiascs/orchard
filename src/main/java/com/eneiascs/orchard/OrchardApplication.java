package com.eneiascs.orchard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.eneiascs.orchard.model.Role;
import com.eneiascs.orchard.service.AccountService;

@SpringBootApplication
public class OrchardApplication {

	@Autowired
	private AccountService accountService;
	
	public static void main(String[] args) {
		SpringApplication.run(OrchardApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void setup() {
		Role userRole = new Role();
		userRole.setName("USER");
		accountService.saveRole(userRole);
		
		Role adminRole = new Role();
		adminRole.setName("ADMIN");
		accountService.saveRole(adminRole);
		
		accountService.createUser("John Smith", "smith", "smith@gmail.com");
	}
}
