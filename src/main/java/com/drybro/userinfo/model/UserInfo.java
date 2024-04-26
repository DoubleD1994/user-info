package com.drybro.userinfo.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfo {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Positive(message = "ID must be a positive value")
	@Column(name = "id")
	private Long id;

	@NotBlank(message = "First Name must be provided")
	@Column(name = "first_name")
	private String firstName;

	@NotBlank(message = "Surname must be provided")
	@Column(name = "surname")
	private String surname;

	@NotBlank(message = "Email must be provided")
	@Column(name = "email", unique = true)
	@Pattern( regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Not a valid email")
	private String email;

	@Column(name = "password")
	private String password;

	@NotNull(message = "Allows email must be provided")
	@Column(name = "allows_email")
	private Boolean allowsEmail;

}
