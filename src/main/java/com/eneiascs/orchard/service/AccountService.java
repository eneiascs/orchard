package com.eneiascs.orchard.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.eneiascs.orchard.model.AppUser;
import com.eneiascs.orchard.model.Role;

public interface AccountService {
	public AppUser findByUsername(String username);

	public AppUser findByEmail(String email);

	public List<AppUser> userList();

	public Role findUserRoleByName(String role);

	public Role saveRole(Role role);

	public AppUser updateUser(AppUser appUser, HashMap<String, String> request);

	public AppUser findById(Long id);

	public void deleteUser(AppUser appUser);

	public void resetPassword(AppUser appUser);

	public List<AppUser> getUserListByUsername(String username);

	public AppUser simpleSave(AppUser appUser);

	public AppUser createUser(String name, String username, String email);

	public void saveUserImage(MultipartFile multipartFile, Long userImageId) throws IOException;

	public void updateUserPassword(AppUser appUser, String newPassword);

}
