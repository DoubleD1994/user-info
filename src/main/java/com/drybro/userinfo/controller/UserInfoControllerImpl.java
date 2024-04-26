package com.drybro.userinfo.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestController;

import com.drybro.userinfo.model.UserInfo;
import com.drybro.userinfo.model.UserInfoResponse;
import com.drybro.userinfo.repository.UserRepository;
import com.drybro.userinfo.service.PasswordGeneratorService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Validated
@RestController
@AllArgsConstructor
@Slf4j
public class UserInfoControllerImpl implements UserInfoController {

	private final UserRepository userRepository;

	@Override
	public ResponseEntity<UserInfoResponse> getAllUsers() {
		final Set<UserInfo> users = new HashSet<>();
		userRepository.findAll().forEach( users::add );
		final UserInfoResponse userInfoResponse = UserInfoResponse.builder()
				.userInfoSet( users )
				.isSuccess( true )
				.build();
		return new ResponseEntity<>( userInfoResponse, HttpStatus.OK );
	}

	@Override
	public ResponseEntity<UserInfoResponse> createUser( final UserInfo userInfo ) {
		userInfo.setPassword( PasswordGeneratorService.generatePassword() );
		userRepository.save( userInfo );
		return new ResponseEntity<>( baseSuccessfulResponse(), HttpStatus.CREATED );
	}

	@Override
	public ResponseEntity<UserInfoResponse> getUserByEmail( final String email ) {
		final UserInfo userInfo = findUserByEmail( email );
		final UserInfoResponse userInfoResponse = UserInfoResponse.builder()
				.userInfo( userInfo )
				.isSuccess( true )
				.build();
		return new ResponseEntity<>( userInfoResponse, HttpStatus.OK );
	}

	@Override
	public ResponseEntity<UserInfoResponse> getUserById( final Long userId ) {
		final UserInfo userInfo = findUserById( userId );
		final UserInfoResponse userInfoResponse = UserInfoResponse.builder()
				.userInfo( userInfo )
				.isSuccess( true )
				.build();
		return new ResponseEntity<>( userInfoResponse, HttpStatus.OK );
	}

	@Override
	public ResponseEntity<UserInfoResponse> updateUser( final Long userId,
			final UserInfo updatedUserInfo ) {
		final UserInfo user = findUserById( userId );
		updateUserInfo( user, updatedUserInfo );
		return new ResponseEntity<>( baseSuccessfulResponse(), HttpStatus.ACCEPTED );
	}

	@Override
	public ResponseEntity<UserInfoResponse> deleteUser( final Long userId ) {
		userRepository.deleteById( userId );
		return new ResponseEntity<>( baseSuccessfulResponse(), HttpStatus.ACCEPTED );
	}

	@Override
	public ResponseEntity<String> getUserEmail( final Long userId ) {
		final String userEmail = findUserById( userId ).getEmail();
		return new ResponseEntity<>( userEmail, HttpStatus.OK );
	}

	@Override
	public ResponseEntity<Boolean> getUserEmailPreferences( final Long userId ) {
		final Boolean userEmailPreferences = findUserById( userId ).getAllowsEmail();
		return new ResponseEntity<>( userEmailPreferences, HttpStatus.OK );
	}

	@Override
	public ResponseEntity<UserInfoResponse> updateUserEmailPreferences( final Long userId,
			final Boolean allowsEmail ) {
		final UserInfo user = findUserById( userId );
		user.setAllowsEmail( allowsEmail );
		userRepository.save( user );
		return new ResponseEntity<>( baseSuccessfulResponse(), HttpStatus.ACCEPTED );
	}

	@Override
	public ResponseEntity<UserInfoResponse> handleValidationExceptions(
			final MethodArgumentNotValidException methodArgumentNotValidException ) {
		final List<String> errorsList = new ArrayList<>();

		methodArgumentNotValidException.getBindingResult()
				.getAllErrors()
				.forEach( error -> errorsList.add( error.getDefaultMessage() ) );

		final UserInfoResponse userInfoResponse = UserInfoResponse.builder()
				.isSuccess( false )
				.errors( errorsList )
				.requestDetails( methodArgumentNotValidException.getTarget() )
				.build();

		log.error( "The request {} was not valid - {}", methodArgumentNotValidException.getTarget(),
				errorsList, methodArgumentNotValidException );

		return new ResponseEntity<>( userInfoResponse, HttpStatus.BAD_REQUEST );
	}

	@Override
	public ResponseEntity<UserInfoResponse> handleNotFoundExceptions(
			final NoSuchElementException noSuchElementException ) {
		final List<String> errorsList = Collections.singletonList(noSuchElementException.getMessage());

		final UserInfoResponse userInfoResponse = UserInfoResponse.builder()
				.isSuccess( false )
				.errors( errorsList )
				.build();

		return new ResponseEntity<>( userInfoResponse, HttpStatus.NOT_FOUND );
	}


	private UserInfo findUserById( final Long userId ) {
		try {
			return userRepository.findById( userId ).orElseThrow();
		} catch ( final NoSuchElementException nsee ) {
			throw new NoSuchElementException( "User with ID " + userId + "  not found", nsee );
		}
	}

	private UserInfo findUserByEmail( final String email ) {
		try {
			return userRepository.findUserInfoByEmail( email ).orElseThrow();
		} catch ( final NoSuchElementException nsee ) {
			throw new NoSuchElementException( "No user found with email address: " + email, nsee );
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

	private UserInfoResponse baseSuccessfulResponse() {
		return UserInfoResponse.builder().isSuccess( true ).build();
	}

}
