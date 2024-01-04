package com.drybro.userinfo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.drybro.userinfo.model.UserInfo;
import com.drybro.userinfo.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Validated
@RestController
@RequestMapping(path = "/user-info", produces = "application/json")
@AllArgsConstructor
@Slf4j
public class UserInfoControllerImpl implements UserInfoController {

	//TODO: Add appropriate response on error

	UserRepository userRepository;

	@Override
	@GetMapping(value = ALL_USERS_PATH)
	public List<UserInfo> getAllUsers() {
		List<UserInfo> users = new ArrayList<>();
		userRepository.findAll().forEach( user -> users.add( user ) );
		return users;
	}

	@Override
	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public void createUser( @RequestBody final UserInfo userInfo ) {
		userRepository.save( userInfo );
		log.info( "USER CREATED: {}", userInfo );
	}

	@Override
	@GetMapping()
	public UserInfo getUserByEmail(@RequestParam("email") String email) {
		return userRepository.findUserInfoByEmail( email ).orElseThrow();
	}

	@Override
	@GetMapping(value = USER_ID_PATH)
	public UserInfo getUserById( @PathVariable("id") final Long userId ) {
		return userRepository.findById( userId ).orElseThrow();
	}

	@Override
	@PutMapping(USER_ID_PATH)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void updateUser( @PathVariable("id") final Long userId,
			@RequestBody final UserInfo updatedUserInfo ) {
		UserInfo user = userRepository.findById( userId ).orElseThrow();
		updateUserInfo( user, updatedUserInfo );
		log.info( "USER WITH ID {} UPDATED", userId );
	}

	@Override
	@DeleteMapping(USER_ID_PATH)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser( @PathVariable("id") final Long userId ) {
		userRepository.deleteById( userId );
		log.info( "USER DELETED {}", userId );
	}

	@Override
	@GetMapping(value = USER_EMAIL_PATH)
	public String getUserEmail( @PathVariable("id") final Long userId ) {
		return userRepository.findById( userId ).orElseThrow().getEmail();
	}

	@Override
	@GetMapping(value = USER_EMAIL_PREFERENCES)
	public Boolean getUserEmailPreferences( @PathVariable("id") final Long userId ) {
		return userRepository.findById( userId ).orElseThrow().getAllowsEmail();
	}

	@Override
	@PutMapping(value = USER_EMAIL_PREFERENCES)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void updateUserEmailPreferenves( @PathVariable("id") final Long userId,
			@RequestParam("allowsEmail") final Boolean allowsEmail ) {
		UserInfo user = userRepository.findById( userId ).orElseThrow();
		user.setAllowsEmail( allowsEmail );
		userRepository.save( user );
		log.info( "USER WITH ID {} EMAIL PREFERENCES UPDATED: {}", userId, allowsEmail );
	}

	private void updateUserInfo( final UserInfo userInfo, final UserInfo updatedUserInfo ) {
		if ( !updatedUserInfo.getFirstName().isBlank() ) {
			userInfo.setFirstName( updatedUserInfo.getFirstName() );
		}
		if ( !updatedUserInfo.getSurname().isBlank() ) {
			userInfo.setSurname( updatedUserInfo.getSurname() );
		}
		if ( !updatedUserInfo.getEmail().isBlank() ) {
			userInfo.setEmail( updatedUserInfo.getEmail() );
		}
		if ( !updatedUserInfo.getPassword().isBlank() ) {
			userInfo.setPassword( updatedUserInfo.getPassword() );
		}
		if ( !updatedUserInfo.getAllowsEmail().toString().isBlank() ) {
			userInfo.setAllowsEmail( updatedUserInfo.getAllowsEmail() );
		}
		userRepository.save( userInfo );
	}

}
