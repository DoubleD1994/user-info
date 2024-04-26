package com.drybro.userinfo.controller;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.servers.Server;

import com.drybro.userinfo.model.UserInfo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

@OpenAPIDefinition(info = @Info(title = "User Info API",
		version = "1.0",
		description = "Service that holds user information"),
		servers = { @Server(url = "/", description = "Default server url") })
@RequestMapping("/user-info")
public interface UserInfoController {

	String APPLICATION_JSON = "application/json";
	String ALL_USERS_PATH = "/get-all-users";

	String CREATE_USER_PATH = "/create-user";

	String GET_USER_BY_EMAIL_PATH = "/get-user/email";

	String USER_BY_ID_PATH = "/get-user/userId";

	String GET_USER_EMAIL_PATH = "/get-user-email";

	String USER_EMAIL_PREFERENCES = GET_USER_BY_EMAIL_PATH + "/preferences";

	@Operation(operationId = "getAllUsers", summary = "Returns a list of all users")
	@ApiResponse(responseCode = "200")
	@GetMapping(value = ALL_USERS_PATH, produces = { APPLICATION_JSON })
	List<UserInfo> getAllUsers();

	@Operation(operationId = "createUser", summary = "Creates a users")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "User created"),
							@ApiResponse(responseCode = "400",
									description = "The user info provided was not valid") })
	@PostMapping(CREATE_USER_PATH)
	void createUser( @Valid @RequestBody UserInfo userInfo ) throws BadRequestException;

	@Operation(operationId = "getUserByEmail", summary = "Gets a user by their email address")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "User found"),
							@ApiResponse(responseCode = "400",
									description = "The email provided was not a valid email"),
							@ApiResponse(responseCode = "404",
									description = "No user found with the email provided") })
	@GetMapping(value = GET_USER_BY_EMAIL_PATH, produces = { APPLICATION_JSON })
	UserInfo getUserByEmail( @RequestParam @Email(message = "Email is not valid",
			regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$") @NotEmpty String email );

	@Operation(operationId = "getUserById", summary = "Gets a user by their id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "User found"),
							@ApiResponse(responseCode = "400",
									description = "The user ID provided was not a valid ID"),
							@ApiResponse(responseCode = "404",
									description = "No user found with the ID provided") })
	@GetMapping(value = USER_BY_ID_PATH, produces = { APPLICATION_JSON })
	UserInfo getUserById( @RequestParam @Positive Long userId );

	@Operation(operationId = "updateUser", summary = "Updates a users details")
	@ApiResponses(value = { @ApiResponse(responseCode = "202", description = "User updated"),
							@ApiResponse(responseCode = "400",
									description = "The request provided was not valid"),
							@ApiResponse(responseCode = "404",
									description = "No user found with the ID provided") })
	@PutMapping(USER_BY_ID_PATH)
	void updateUser( @RequestParam @Positive Long userId, @Valid @RequestBody UserInfo userInfo );

	@Operation(operationId = "deleteUser", summary = "Deletes a user")
	@ApiResponses(value = { @ApiResponse(responseCode = "202", description = "User deleted"),
							@ApiResponse(responseCode = "400",
									description = "The user ID provided was not valid"),
							@ApiResponse(responseCode = "404",
									description = "No user found with the ID provided") })
	@DeleteMapping(USER_BY_ID_PATH)
	void deleteUser( @RequestParam @Positive Long userId );

	@Operation(operationId = "getUserEmail", summary = "Gets a users email")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found users email"),
							@ApiResponse(responseCode = "400",
									description = "The user ID provided was not valid"),
							@ApiResponse(responseCode = "404",
									description = "No user found with the ID provided") })
	@GetMapping(value = GET_USER_EMAIL_PATH, produces = { APPLICATION_JSON })
	String getUserEmail( @RequestParam @Positive Long userId );

	@Operation(operationId = "getUserEmailPreferences", summary = "Gets a users email preferences")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found users email preferences"),
							@ApiResponse(responseCode = "400",
									description = "The user ID provided was not valid"),
							@ApiResponse(responseCode = "404",
									description = "No user found with the ID provided") })
	@GetMapping(value = USER_EMAIL_PREFERENCES, produces = { APPLICATION_JSON })
	Boolean getUserEmailPreferences( @RequestParam @Positive Long userId );

	@Operation(operationId = "updateUserEmailPreferences", summary = "Updates a users email preferences")
	@ApiResponses(value = { @ApiResponse(responseCode = "202", description = "Found users email preferences"),
							@ApiResponse(responseCode = "400",
									description = "The request provided was not valid"),
							@ApiResponse(responseCode = "404",
									description = "No user found with the ID provided") })
	@PutMapping(USER_EMAIL_PREFERENCES)
	void updateUserEmailPreferences( @RequestParam @Positive Long userId, @RequestBody Boolean allowsEmail );

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException methodArgumentNotValidException);

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NoResourceFoundException.class)
	ResponseEntity<?> handleNotFoundExceptions(NoResourceFoundException noResourceFoundException);
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BadRequestException.class)
	ResponseEntity<?> handleBadRequestExceptions(BadRequestException badRequestException);
}
