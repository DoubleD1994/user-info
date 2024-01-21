package com.drybro.userinfo.controller;

import static com.drybro.userinfo.controller.UserInfoController.USER_INFO_PATH;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
import org.springframework.web.server.ResponseStatusException;

import com.drybro.userinfo.model.UserInfo;
import com.drybro.userinfo.repository.UserRepository;
import com.drybro.userinfo.service.PasswordGeneratorService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Validated
@RestController
@RequestMapping(path = USER_INFO_PATH, produces = "application/json")
@AllArgsConstructor
@Slf4j
public class UserInfoControllerImpl implements UserInfoController {

	private final UserRepository userRepository;

	@Override
	@GetMapping(value = ALL_USERS_PATH)
	public List<UserInfo> getAllUsers() {
		final List<UserInfo> users = new ArrayList<>();
		userRepository.findAll().forEach( users::add );
		return users;
	}

	@Override
	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public void createUser( @RequestBody final UserInfo userInfo ) {
		userInfo.setPassword( PasswordGeneratorService.generatePassword() );
		userRepository.save( userInfo );
		log.info( "USER CREATED: {}", userInfo );
	}

	@Override
	@GetMapping()
	public UserInfo getUserByEmail(@RequestParam final String email) {
		return findUserByEmail(email);
	}

	@Override
	@GetMapping(value = USER_ID_PATH)
	public UserInfo getUserById( @PathVariable final Long userId ) {
		return findUserById( userId );
	}

	@Override
	@PutMapping(USER_ID_PATH)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void updateUser( @PathVariable final Long userId,
			@RequestBody final UserInfo updatedUserInfo ) {
		UserInfo user = findUserById( userId );
		updateUserInfo( user, updatedUserInfo );
		log.info( "USER WITH ID {} UPDATED", userId );
	}

	@Override
	@DeleteMapping(USER_ID_PATH)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser( @PathVariable final Long userId ) {
		userRepository.deleteById( userId );
		log.info( "USER DELETED {}", userId );
	}

	@Override
	@GetMapping(value = USER_EMAIL_PATH)
	public String getUserEmail( @PathVariable final Long userId ) {
		return findUserById( userId ).getEmail();
	}

	@Override
	@GetMapping(value = USER_EMAIL_PREFERENCES)
	public Boolean getUserEmailPreferences( @PathVariable final Long userId ) {
		return findUserById( userId ).getAllowsEmail();
	}

	@Override
	@PutMapping(value = USER_EMAIL_PREFERENCES)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void updateUserEmailPreferenves( @PathVariable final Long userId,
			@RequestParam final Boolean allowsEmail ) {
		UserInfo user = findUserById( userId );
		user.setAllowsEmail( allowsEmail );
		userRepository.save( user );
		log.info( "USER WITH ID {} EMAIL PREFERENCES UPDATED: {}", userId, allowsEmail );
	}

	private UserInfo findUserById(final Long userId) {
		try {
			return userRepository.findById( userId ).orElseThrow();
		} catch ( NoSuchElementException nsee ) {
			throw new ResponseStatusException( HttpStatus.NOT_FOUND,
					"User with ID " + userId + "  not found", nsee );
		}
	}

	private UserInfo findUserByEmail(final String email) {
		try {
			return userRepository.findUserInfoByEmail( email ).orElseThrow();
		} catch ( NoSuchElementException nsee ) {
			throw new ResponseStatusException( HttpStatus.NOT_FOUND,
					"No user found with email address: " + email, nsee );
		}
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
