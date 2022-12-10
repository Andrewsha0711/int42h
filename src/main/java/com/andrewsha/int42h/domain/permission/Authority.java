package com.andrewsha.int42h.domain.permission;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "permissions")
public class Authority {
	@Id
	@GenericGenerator(name = "LongId", strategy = "org.hibernate.id.IncrementGenerator")
	@GeneratedValue(generator = "LongId")
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@JsonIgnore
	@ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
	private Set<AuthoritySet> relatedSets = new HashSet<>();

	public Authority() {}

	public Authority(AuthorityEnum action, Class<?> targetObject) {
		this.name =
				action.toString().toUpperCase() + "_" + targetObject.getSimpleName().toUpperCase();
	}

	public String getAction() {
		return this.name.substring(0, this.name.indexOf("_"));
	}

	public String getTargetObjectType() {
		return (this.name.split("_"))[1];
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<AuthoritySet> getRelatedSets() {
		return this.relatedSets;
	}

	public void setAuthorities(Set<AuthoritySet> authorities) {
		this.relatedSets = authorities;
	}
}
