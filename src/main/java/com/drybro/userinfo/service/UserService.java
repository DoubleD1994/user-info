package com.drybro.userinfo.service;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.drybro.userinfo.model.UserInfo;
import com.drybro.userinfo.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public Set<UserInfo> returnAllUsersAsSet() {
		final Set<UserInfo> users = new HashSet<>();
		userRepository.findAll().forEach( users::add );
		return users;
	}

	public void saveUserInDatabase( final UserInfo userInfo ) {
		userInfo.setPassword( PasswordGeneratorService.generatePassword() );
		userRepository.save( userInfo );
	}

	public UserInfo findUserByEmail( final String email ) {
		try {
			return userRepository.findUserInfoByEmail( email ).orElseThrow();
		} catch ( final NoSuchElementException nsee ) {
			throw new NoSuchElementException( "No user found with email address: " + email, nsee );
		}
	}

	public UserInfo findUserById( final Long userId ) {
		try {
			return userRepository.findById( userId ).orElseThrow();
		} catch ( final NoSuchElementException nsee ) {
			throw new NoSuchElementException( "User with ID " + userId + "  not found", nsee );
		}
	}

	public void updateUserInfo( final Long userId, final UserInfo updatedUserInfo ) {
		final UserInfo userInfo = findUserById( userId );
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

	public void deleteUserInfo( final Long userId ) {
		userRepository.deleteById( userId );
	}

	public void updateUserInfoEmailPreferences( final Long userId, final Boolean allowsEmail ) {
		final UserInfo user = findUserById( userId );
		user.setAllowsEmail( allowsEmail );
		userRepository.save( user );
	}

}
