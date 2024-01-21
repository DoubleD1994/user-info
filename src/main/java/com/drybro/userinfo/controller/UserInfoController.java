package com.drybro.userinfo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.drybro.userinfo.model.UserInfo;

public interface UserInfoController {

	String USER_INFO_PATH = "/user-info";
	String ALL_USERS_PATH = "/all-users";

	String USER_ID_PATH = "/{userId}";

	String USER_EMAIL_PATH = "/{userId}/email";

	String USER_EMAIL_PREFERENCES = "/{userId}/email/preferences";

	@GetMapping(ALL_USERS_PATH)
	List<UserInfo> getAllUsers();

	@PostMapping()
	void createUser( UserInfo userInfo );

	@GetMapping()
	UserInfo getUserByEmail(String email);

	@GetMapping(USER_ID_PATH)
	UserInfo getUserById(@PathVariable Long userId);

	@PutMapping(USER_ID_PATH)
	void updateUser(@PathVariable Long userId, UserInfo userInfo );

	@DeleteMapping(USER_ID_PATH)
	void deleteUser(@PathVariable Long userId);

	@GetMapping(USER_EMAIL_PATH)
	String getUserEmail(@PathVariable Long userId);

	@GetMapping(USER_EMAIL_PREFERENCES)
	Boolean getUserEmailPreferences(@PathVariable Long userId);

	@PutMapping(USER_EMAIL_PREFERENCES)
	void updateUserEmailPreferences(@PathVariable Long userId, Boolean allowsEmail);

}
