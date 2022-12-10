package com.andrewsha.int42h.security.util;

import org.springframework.security.core.GrantedAuthority;
import com.andrewsha.int42h.domain.permission.Authority;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class CustomGrantedAuthority implements GrantedAuthority {
	private final String scope;
	private final String authority;

	private static final long serialVersionUID = 3709905622125984076L;

	@Override
	public String getAuthority() {
		// TODO rename
		return "PERMISSION_" + this.authority.toUpperCase() + "_" + this.scope;
	}

	public CustomGrantedAuthority(Authority authority, String scope) {
		super();
		this.scope = scope;
		this.authority = authority.getName();
	}

	@JsonIgnore
	public String getScope() {
		return scope;
	}

	/*
	 * @JsonIgnore public String getAuthority() { return this.authority; }
	 */
}
