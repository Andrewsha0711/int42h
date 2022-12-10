package com.andrewsha.int42h.domain.permission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.andrewsha.int42h.domain.user.User;
import com.andrewsha.int42h.security.util.CustomGrantedAuthority;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "permission_sets")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AuthoritySet {
	@Id
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@GeneratedValue(generator = "UUID")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Column(name = "scope", nullable = false, updatable = false)
	private String scope;

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_permissions", joinColumns = @JoinColumn(name = "permission_set_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> users = new HashSet<>();

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "permission_sets_permissions",
			joinColumns = @JoinColumn(name = "permission_set_id"),
			inverseJoinColumns = @JoinColumn(name = "permission_id"))
	private Set<Authority> authorities = new HashSet<>();

	@JsonIgnore
	public Collection<CustomGrantedAuthority> getAuthorities() {
		Collection<CustomGrantedAuthority> authorities = new ArrayList<>();
		for (Authority authority : this.authorities) {
			authorities.add(new CustomGrantedAuthority(authority, this.scope));
		}
		return authorities;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getScope() {
		return scope;
	}

	/*
	 * public Set<Authority> getAuthorities() { return authorities; }
	 */

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public void addAuthorities(Collection<Authority> authorities) {
		this.authorities.addAll(authorities);
	}

	public void addUser(User user) {
		this.users.add(user);
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
}
