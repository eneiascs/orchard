package com.eneiascs.orchard.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eneiascs.orchard.model.Role;

public interface RoleRepo extends JpaRepository<Role, Long>{

	Role findRoleByName(String role);

}
