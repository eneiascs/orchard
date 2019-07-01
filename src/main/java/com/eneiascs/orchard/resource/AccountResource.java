package com.eneiascs.orchard.resource;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eneiascs.orchard.model.AppUser;
import com.eneiascs.orchard.service.AccountService;

@RestController
@RequestMapping("/user")
public class AccountResource {
	private Long userImageId;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private AccountService accountService;

	@GetMapping("/list")
	public ResponseEntity<?> getUsersList() {
		List<AppUser> users = accountService.userList();
		if (users.isEmpty()) {
			return new ResponseEntity<>(ResponseEnum.NO_USERS_FOUND, HttpStatus.OK);
		}
		return new ResponseEntity<>(users, HttpStatus.OK);

	}

	@GetMapping("/{username}")
	public ResponseEntity<?> getUserInfo(@PathVariable("username") String username) {
		AppUser user = accountService.findByUsername(username);
		if (user == null) {
			return new ResponseEntity<>(ResponseEnum.NO_USER_FOUND, HttpStatus.OK);
		}
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping("/findByUsername/{username}")
	public ResponseEntity<?> getUsersByUsername(@PathVariable("username") String username) {
		List<AppUser> users = accountService.getUserListByUsername(username);
		if (users.isEmpty()) {
			return new ResponseEntity<>(ResponseEnum.NO_USERS_FOUND, HttpStatus.OK);
		}
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody HashMap<String, String> request) {
		String username = request.get("username");
		if (accountService.findByUsername(username) != null) {
			return new ResponseEntity<>(ResponseEnum.USERNAME_EXISTS, HttpStatus.CONFLICT);
		}
		String email = request.get("email");
		if (accountService.findByUsername(email) != null) {
			return new ResponseEntity<>(ResponseEnum.EMAIL_EXISTS, HttpStatus.CONFLICT);
		}
		String name = request.get("name");
		try {
			AppUser user = accountService.createUser(name, username, email);
			return new ResponseEntity<>(user, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(ResponseEnum.ERROR, HttpStatus.BAD_REQUEST);

		}
	}

	@PostMapping("/update")
	public ResponseEntity<?> updateProfile(@RequestBody HashMap<String, String> request) {
		String id = request.get("id");
		AppUser user = accountService.findById(Long.parseLong(id));
		if (user == null) {
			return new ResponseEntity<>(ResponseEnum.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
		}

		try {
			user = accountService.updateUser(user, request);
			userImageId = user.getId();
			return new ResponseEntity<>(user, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(ResponseEnum.ERROR, HttpStatus.BAD_REQUEST);

		}
	}

	@PostMapping("/photo/upload")
	public ResponseEntity<ResponseEnum> fileUpload(@RequestParam("image") MultipartFile multipartFile) {

		try {
			accountService.saveUserImage(multipartFile, userImageId);
			return new ResponseEntity<>(ResponseEnum.USER_PICTURE_SAVED, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(ResponseEnum.USER_PICTURE_NOT_SAVED, HttpStatus.BAD_REQUEST);

		}
	}

	@PostMapping("/changePassword")
	public ResponseEntity<ResponseEnum> changePassword(@RequestBody HashMap<String, String> request) {

		String username = request.get("username");
		AppUser appUser = accountService.findByUsername(username);
		if (appUser == null) {
			return new ResponseEntity<>(ResponseEnum.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
		}

		String currentPassword = request.get("currentPassword");
		String newPassword = request.get("newPassword");
		String confirmPassword = request.get("confirmPassword");

		if (!newPassword.equals(confirmPassword)) {
			return new ResponseEntity<>(ResponseEnum.PASSWORD_NOT_MATCHED, HttpStatus.BAD_REQUEST);
		}
		String userPassword = appUser.getPassword();
		try {
			if (newPassword != null && !newPassword.isEmpty() && !StringUtils.isEmpty(newPassword))
				if (bCryptPasswordEncoder.matches(currentPassword, userPassword)) {
					accountService.updateUserPassword(appUser, newPassword);
				} else {
					return new ResponseEntity<>(ResponseEnum.PASSWORD_NOT_MATCHED, HttpStatus.BAD_REQUEST);

				}

			return new ResponseEntity<>(ResponseEnum.USER_PICTURE_SAVED, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(ResponseEnum.USER_PICTURE_NOT_SAVED, HttpStatus.BAD_REQUEST);

		}
	}

	@GetMapping("/resetPassword/{email}")
	public ResponseEntity<ResponseEnum> resetPassowrd(@PathVariable String email) {
		AppUser appUser = accountService.findByEmail(email);
		if (appUser == null) {
			return new ResponseEntity<>(ResponseEnum.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		accountService.resetPassword(appUser);
		return new ResponseEntity<>(ResponseEnum.EMAIL_SENT, HttpStatus.OK);
	}
}
