package com.eneiascs.orchard.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eneiascs.orchard.model.AppUser;
import com.eneiascs.orchard.model.Role;
import com.eneiascs.orchard.model.UserRole;
import com.eneiascs.orchard.repo.AppUserRepo;
import com.eneiascs.orchard.repo.RoleRepo;
import com.eneiascs.orchard.service.AccountService;
import com.eneiascs.orchard.utility.Constants;
import com.eneiascs.orchard.utility.EmailConstructor;

@Transactional
@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private AppUserRepo appUserRepo;

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private EmailConstructor emailConstructor;

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public AppUser findByUsername(String username) {
		return appUserRepo.findByUsername(username);
	}

	@Override
	public AppUser findByEmail(String email) {
		return appUserRepo.findByEmail(email);

	}

	@Override
	public List<AppUser> userList() {
		return appUserRepo.findAll();
	}

	@Override
	public Role findUserRoleByName(String role) {
		return roleRepo.findRoleByName(role);
	}

	@Override
	public Role saveRole(Role role) {

		return roleRepo.save(role);
	}

	@Override
	public AppUser updateUser(AppUser appUser, HashMap<String, String> request) {
		String password = appUser.getPassword();
		String encryptedPassword = bCryptPasswordEncoder.encode(password);
		appUser.setPassword(encryptedPassword);
		appUser = appUserRepo.save(appUser);
		mailSender.send(emailConstructor.constructUpdateProfileEmail(appUser));
		return appUser;

	}

	@Override
	public AppUser findById(Long id) {

		return appUserRepo.findUserById(id);
	}

	@Override
	public void deleteUser(AppUser appUser) {
		appUserRepo.delete(appUser);
	};

	@Override
	public void resetPassword(AppUser appUser) {
		String password = RandomStringUtils.randomAlphanumeric(10);
		String encryptedPassword = bCryptPasswordEncoder.encode(password);
		appUser.setPassword(encryptedPassword);
		appUserRepo.save(appUser);
		mailSender.send(emailConstructor.constructResetPasswordEmail(appUser, password));

	}

	@Override
	public List<AppUser> getUserListByUsername(String username) {
		return appUserRepo.findByUsernameContaining(username);
	}

	@Override
	public AppUser simpleSave(AppUser appUser) {
		appUserRepo.save(appUser);
		mailSender.send(emailConstructor.constructUpdateProfileEmail(appUser));
		return appUser;

	}

	@Override
	public AppUser createUser(String name, String username, String email) {
		AppUser appUser = new AppUser(name, username, email);
		String password = RandomStringUtils.randomAlphanumeric(10);
		String encryptedPassword = bCryptPasswordEncoder.encode(password);
		appUser.setPassword(encryptedPassword);

		Role role = findUserRoleByName("USER");
		appUser.addUserRole(new UserRole(appUser, role));
		appUser = appUserRepo.save(appUser);

		byte[] bytes;
		try {
			bytes = Files.readAllBytes(Constants.TEMP_USER.toPath());
			String fileName = appUser.getId() + ".png";
			Path path = Paths.get(Constants.USER_FOLDER + fileName);
			Files.write(path, bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Password: " + password);
		// mailSender.send(emailConstructor.constructNewUserEmail(appUser,
		// password));
		return appUser;
	}

	@Override
	public void saveUserImage(MultipartFile multipartFile, Long userImageId) throws IOException {
		byte[] bytes = multipartFile.getBytes();
		Path path = Paths.get(Constants.USER_FOLDER + userImageId + ".png");
		Files.write(path, bytes, StandardOpenOption.CREATE);

	}

	@Override
	public void updateUserPassword(AppUser appUser, String newPassword) {

		String encryptedPassword = bCryptPasswordEncoder.encode(newPassword);
		appUser.setPassword(encryptedPassword);
		appUser = appUserRepo.save(appUser);
		mailSender.send(emailConstructor.constructUpdateProfileEmail(appUser));

	}

}
