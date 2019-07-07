package com.eneiascs.orchard.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eneiascs.orchard.model.AppUser;

public interface AppUserRepo extends JpaRepository<AppUser, Long>{
	public AppUser findByUsername(String username);
	
	public AppUser findByEmail(String email);
	
	public AppUser findUserById(Long id);

	public List<AppUser> findByUsernameContaining(String username);

}
