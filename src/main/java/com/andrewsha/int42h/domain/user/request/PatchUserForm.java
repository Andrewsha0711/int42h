package com.andrewsha.int42h.domain.user.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import com.andrewsha.int42h.config.Config;

public class PatchUserForm {
	@Pattern(regexp = Config.nameRegexp, message = "wrong name format")
	private String name;

	@Email(message = "wrong email format")
	private String email;
	
	@Pattern(regexp = Config.phoneNumberRegexp, message = "wrong phone number format")
	private String phoneNumber;

	@Pattern(regexp = Config.passwordRegexp, message = "wrong password format")
	private String password;

	@Pattern(regexp = Config.imageUrlRegexp, message = "cannot resolve to image url")
	private String profileIcon;

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
}
