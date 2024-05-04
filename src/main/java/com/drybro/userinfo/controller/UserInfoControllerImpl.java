package com.drybro.userinfo.controller;

import java.util.ArrayList;
import java.util.Collections;
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
import com.drybro.userinfo.service.UserService;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Validated
@RestController
@AllArgsConstructor
@Slf4j
public class UserInfoControllerImpl implements UserInfoController {

	private final UserService userService;

	@Override
	public ResponseEntity<UserInfoResponse> getAllUsers() {
		final Set<UserInfo> users = userService.returnAllUsersAsSet();
		final UserInfoResponse userInfoResponse = UserInfoResponse.builder()
				.userInfoSet( users )
				.isSuccess( true )
				.build();
		return new ResponseEntity<>( userInfoResponse, HttpStatus.OK );
	}

	@Override
	public ResponseEntity<UserInfoResponse> createUser( final UserInfo userInfo ) {
		userService.saveUserInDatabase( userInfo );
		return new ResponseEntity<>( baseSuccessfulResponse(), HttpStatus.CREATED );
	}

	@Override
	public ResponseEntity<UserInfoResponse> getUserByEmail( final String email ) {
		final UserInfo userInfo = userService.findUserByEmail( email );
		final UserInfoResponse userInfoResponse = UserInfoResponse.builder()
				.userInfo( userInfo )
				.isSuccess( true )
				.build();
		return new ResponseEntity<>( userInfoResponse, HttpStatus.OK );
	}

	@Override
	public ResponseEntity<UserInfoResponse> getUserById( final Long userId ) {
		final UserInfo userInfo = userService.findUserById( userId );
		final UserInfoResponse userInfoResponse = UserInfoResponse.builder()
				.userInfo( userInfo )
				.isSuccess( true )
				.build();
		return new ResponseEntity<>( userInfoResponse, HttpStatus.OK );
	}

	@Override
	public ResponseEntity<UserInfoResponse> updateUser( final Long userId,
			final UserInfo updatedUserInfo ) {
		userService.updateUserInfo( userId, updatedUserInfo );
		return new ResponseEntity<>( baseSuccessfulResponse(), HttpStatus.ACCEPTED );
	}

	@Override
	public ResponseEntity<UserInfoResponse> deleteUser( final Long userId ) {
		userService.deleteUserInfo( userId );
		return new ResponseEntity<>( baseSuccessfulResponse(), HttpStatus.ACCEPTED );
	}

	@Override
	public ResponseEntity<String> getUserEmail( final Long userId ) {
		final String userEmail = userService.findUserById( userId ).getEmail();
		return new ResponseEntity<>( userEmail, HttpStatus.OK );
	}

	@Override
	public ResponseEntity<Boolean> getUserEmailPreferences( final Long userId ) {
		final Boolean userEmailPreferences = userService.findUserById( userId ).getAllowsEmail();
		return new ResponseEntity<>( userEmailPreferences, HttpStatus.OK );
	}

	@Override
	public ResponseEntity<UserInfoResponse> updateUserEmailPreferences( final Long userId,
			final Boolean allowsEmail ) {
		userService.updateUserInfoEmailPreferences( userId, allowsEmail );
		return new ResponseEntity<>( baseSuccessfulResponse(), HttpStatus.ACCEPTED );
	}

	@Override
	public ResponseEntity<UserInfoResponse> handleMethodArgumentNotValidExceptions(
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
	public ResponseEntity<UserInfoResponse> handleConstraintValidationExceptions(
			final ConstraintViolationException constraintViolationException ) {
		final List<String> errorsList = Collections.singletonList(constraintViolationException.getMessage());
		final UserInfoResponse userInfoResponse = UserInfoResponse.builder()
				.isSuccess( false )
				.errors( errorsList )
				.requestDetails( constraintViolationException.getConstraintViolations() )
				.build();

		log.error( "The request was not valid - {}", constraintViolationException.getMessage()
				, constraintViolationException );
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

		log.error( "404 Not Found - {}", errorsList, noSuchElementException );

		return new ResponseEntity<>( userInfoResponse, HttpStatus.NOT_FOUND );
	}


	private UserInfoResponse baseSuccessfulResponse() {
		return UserInfoResponse.builder().isSuccess( true ).build();
	}

}
