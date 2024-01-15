package com.drybro.userinfo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.drybro.userinfo.model.UserInfo;

public interface UserInfoController {

	String USER_INFO_PATH = "/user-info";
	String ALL_USERS_PATH = "/all-users";

	String USER_ID_PATH = "/{id}";

	String USER_EMAIL_PATH = "/{id}/email";

	String USER_EMAIL_PREFERENCES = "/{id}/email/preferences";

	@GetMapping(ALL_USERS_PATH)
	List<UserInfo> getAllUsers();

	@PostMapping()
	void createUser( UserInfo userInfo );

	@GetMapping()
	UserInfo getUserByEmail(String email);

	@GetMapping(USER_ID_PATH)
	UserInfo getUserById(Long userId);

	@PutMapping(USER_ID_PATH)
	void updateUser(Long userId, UserInfo userInfo );

	@DeleteMapping(USER_ID_PATH)
	void deleteUser(Long userId);

	@GetMapping(USER_EMAIL_PATH)
	String getUserEmail(Long userId);

	@GetMapping(USER_EMAIL_PREFERENCES)
	Boolean getUserEmailPreferences(Long userId);

	@PutMapping(USER_EMAIL_PREFERENCES)
	void updateUserEmailPreferenves(Long userId, Boolean allowsEmail);

}
