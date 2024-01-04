package com.drybro.userinfo.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id")
	private Long id;

	@Nonnull
	@Column(name = "first_name")
	private String firstName;

	@Nonnull
	@Column(name = "surname")
	private String surname;

	@Nonnull
	@Column(name = "email", unique = true)
	@Pattern( regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
	private String email;

	@Nonnull
	@Column(name = "password")
	private String password;

	@Nonnull
	@Column(name = "allows_email")
	private Boolean allowsEmail;

}
