package com.andrewsha.int42h.domain.user.request;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.andrewsha.int42h.config.Config;

public class CreateUserForm {
	@NotEmpty(message = "name cannot be empty")
	@Pattern(regexp = Config.nameRegexp, message = "wrong name format")
	private String name;
	
	@Email(message = "wrong email format")
	private String email;
	
	@Pattern(regexp = Config.phoneNumberRegexp, message = "wrong phone number format")
	private String phoneNumber;
	
	@NotEmpty(message = "password cannot be empty")
	@Pattern(regexp = Config.passwordRegexp, message = "wrong password format")
	private String password;
	
	@Pattern(regexp = Config.imageUrlRegexp, message = "cannot resolve to image url")
	private String profileIcon;

	@NotNull(message = "date of birth is required")
	private LocalDate dob;
	
	@AssertTrue(message = "email or phone number is required")
	private boolean isEmailOrPhoneNumberExists() {
		return this.email != null || this.phoneNumber != null;
	}
	
	@AssertTrue(message = "age must be greater than 14 and less than 110")
	private boolean isDateOfBirthValid() {
		if(this.dob != null) {
			long age = this.dob.until(LocalDate.now(), ChronoUnit.YEARS);
			if(age < 14 || age > 100) {
				return false;
			}
		}
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProfileIcon() {
		return profileIcon;
	}

	public void setProfileIcon(String profileIcon) {
		this.profileIcon = profileIcon;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}
}