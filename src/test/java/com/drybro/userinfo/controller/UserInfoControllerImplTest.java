package com.drybro.userinfo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import com.drybro.userinfo.model.UserInfo;
import com.drybro.userinfo.repository.UserRepository;

import jakarta.validation.ConstraintViolationException;

@SpringBootTest
public class UserInfoControllerImplTest {

	@MockBean
	private UserRepository userRepository;

	@Autowired
	private UserInfoController userInfoController;
	private final static List<UserInfo> userInfoList = new ArrayList<>();
	private static UserInfo userInfoOne;
	private static UserInfo userInfoTwo;
	private static UserInfo userInfoThree;

	@BeforeAll
	public static void beforeAll() {
		userInfoOne = new UserInfo( 1l, "user", "one", "userone@email.com", "password", true );
		userInfoTwo = new UserInfo( 2l, "user", "two", "usertwo@email.com", "password", true );
		userInfoThree = new UserInfo( 3l, "user", "three", "userthree@email.com", "password",
				false );

		userInfoList.add( userInfoOne );
		userInfoList.add( userInfoTwo );
		userInfoList.add( userInfoThree );
	}

	@Test
	void contextLoads() {
		assertThat( userInfoController ).isNotNull();
	}

	@Test
	void getAllUsers_HappyPath() {
		when( userRepository.findAll() ).thenReturn( userInfoList );
		final Set<UserInfo> returnedUserInfoList = userInfoController.getAllUsers().getBody()
				.getUserInfoSet();
		assertThat( returnedUserInfoList.size() ).isEqualTo( 3 );
		assertThat( returnedUserInfoList ).contains( userInfoOne );
		assertThat( returnedUserInfoList ).contains( userInfoTwo );
		assertThat( returnedUserInfoList ).contains( userInfoThree );
	}

	@Test
	void getAllUsers_HappyPathEmptyList() {
		when( userRepository.findAll() ).thenReturn( new ArrayList<>() );
		final Set<UserInfo> returnedUserInfoList = userInfoController.getAllUsers().getBody()
				.getUserInfoSet();
		assertThat( returnedUserInfoList.size() ).isEqualTo( 0 );
	}

	@Test
	void createUser_HappyPath() {
		final boolean isSuccess = userInfoController.createUser( userInfoOne ).getBody().isSuccess();
		verify( userRepository, times( 1 ) ).save( userInfoOne );
		assertTrue( isSuccess );
	}

	@Test
	void createUser_NullUserThrowsException() {
		assertThrows( NullPointerException.class,
				() -> userInfoController.createUser( null ) );
	}

	@Test
	void createUser_NullUserFirstNameThrowsExcpetion() {
		assertThrows( ConstraintViolationException.class, () -> userInfoController.createUser(
				new UserInfo( 1l, null, "one", "userone@email.com", "password", true ) ) );
	}

	@Test
	void getUserByEmail_HappyPath() {
		when( userRepository.findUserInfoByEmail( userInfoOne.getEmail() ) ).thenReturn(
				Optional.of( userInfoOne ) );
		final UserInfo returnedUser = userInfoController.getUserByEmail( userInfoOne.getEmail() ).getBody()
				.getUserInfo();
		verify( userRepository, times(1) ).findUserInfoByEmail( userInfoOne.getEmail() );
		assertThat( returnedUser.getId() ).isEqualTo( userInfoOne.getId() );
	}

	@Test
	void getUserByEmail_UserNotFoundThrowsResponseStatusException() {
		assertThrows( NoSuchElementException.class,
				() -> userInfoController.getUserByEmail( "notauser@email.com" ) );
	}

	@Test
	void getUserByEmail_NullPassedForEmailThrowsResponseStatusException() {
		assertThrows( ConstraintViolationException.class,
				() -> userInfoController.getUserByEmail( null ) );
	}

	@Test
	void getUserByEmail_EmptyStringForEmailThrowsResponseStatusException() {
		assertThrows( ConstraintViolationException.class,
				() -> userInfoController.getUserByEmail( "" ) );
	}

	@Test
	void getUserById_HappyPath() {
		when( userRepository.findById( userInfoOne.getId() ) ).thenReturn(
				Optional.of( userInfoOne ) );
		final UserInfo returnedUser = userInfoController.getUserById( userInfoOne.getId() ).getBody()
				.getUserInfo();
		verify( userRepository, times(1) ).findById( userInfoOne.getId() );
		assertThat( returnedUser.getId() ).isEqualTo( userInfoOne.getId() );
	}

	@Test
	void getUserById_UserNotFoundThrowsResponseStatusException() {
		assertThrows( NoSuchElementException.class,
				() -> userInfoController.getUserById( 5l ) );
	}

	@Test
	void getUserById_NullValueThrowsResponseStatusException() {
		assertThrows( NoSuchElementException.class,
				() -> userInfoController.getUserById( null ) );
	}

	@Test
	void updateUser_HappyPath() {
		when( userRepository.findById( userInfoOne.getId() ) ).thenReturn(
				Optional.of( userInfoOne ) );

		final UserInfo updateUserInfo = userInfoOne;
		updateUserInfo.setFirstName( "Updated" );
		updateUserInfo.setSurname( "Name" );

		userInfoController.updateUser(userInfoOne.getId(), updateUserInfo);

		verify( userRepository, times(1) ).findById( userInfoOne.getId() );
		verify( userRepository, times( 1 ) ).save( updateUserInfo );
	}

	@Test
	void updateUser_UserNotFoundThrowsResponseStatusException() {
		assertThrows( ConstraintViolationException.class,
				() -> userInfoController.updateUser( 5l, new UserInfo() ) );
	}

	@Test
	void deleteUser_HappyPath() {
		userInfoController.deleteUser( userInfoOne.getId() );
		verify( userRepository, times( 1 ) ).deleteById( userInfoOne.getId() );
	}

	@Test
	void getUserEmail_HappyPath() {
		when(userRepository.findById( userInfoOne.getId() )).thenReturn( Optional.of( userInfoOne ) );

		final String returnedEmail = userInfoController.getUserEmail( userInfoOne.getId() ).getBody();
		assertThat( returnedEmail ).isEqualTo( userInfoOne.getEmail() );
	}

	@Test
	void getUserEmail_UserNotFoundThrowsResponseStatusException() {
		assertThrows( NoSuchElementException.class,
				() -> userInfoController.getUserEmail( 5l ) );
	}

	@Test
	void getUserEmailPreferences_HappyPath() {
		when(userRepository.findById( userInfoOne.getId() )).thenReturn( Optional.of( userInfoOne ) );

		final boolean returnedEmailPreferences = userInfoController.getUserEmailPreferences( userInfoOne.getId() ).getBody();
		assertThat( returnedEmailPreferences ).isEqualTo( userInfoOne.getAllowsEmail() );
	}

	@Test
	void getUserEmailPreferences_UserNotFoundThrowsResponseStatusException() {
		assertThrows( NoSuchElementException.class,
				() -> userInfoController.getUserEmailPreferences( 5l ) );
	}

	@Test
	void updateUserEmailPreferences_HappyPath() {
		when(userRepository.findById( userInfoOne.getId() )).thenReturn( Optional.of( userInfoOne ) );

		final UserInfo updatedUserInfo = userInfoOne;
		updatedUserInfo.setAllowsEmail( false );

		userInfoController.updateUserEmailPreferences(userInfoOne.getId(), false);

		verify( userRepository, times(1) ).findById( userInfoOne.getId() );
		verify( userRepository, times( 1 ) ).save( updatedUserInfo );
	}

	@Test
	void updateUserEmailPreferences_UserNotFoundThrowsResponseStatusException() {
		assertThrows( NoSuchElementException.class,
				() -> userInfoController.updateUserEmailPreferences( 5l, false ) );
	}

}
