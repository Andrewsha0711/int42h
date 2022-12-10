package com.andrewsha.int42h.domain.permission;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService {
	@Autowired
	private AuthorityRepository authRepository;
	@Autowired
	private AuthoritySetRepository authSetRepository;

	public Set<Authority> getAuthorities(Set<AuthorityEnum> actions, Class<? extends Object> type) {
		Set<Authority> permissions = new HashSet<>();
		for (AuthorityEnum action : actions) {
			Authority permission = new Authority(action, type);
			Optional<Authority> optional = this.authRepository.findByName(permission.getName());
			if (!optional.isPresent()) {
				permissions.add(this.authRepository.save(permission));
			} else {
				permissions.add(optional.get());
			}
		}
		return permissions;
	}

	public AuthoritySet save(AuthoritySet authoritySet) {
		return this.authSetRepository.save(authoritySet);
	}

	public AuthoritySet getAuthoritySet(String scope, Set<Authority> authorities) {
		List<AuthoritySet> authList = this.authSetRepository.findByScope(scope);
		if (!authList.isEmpty()) {
			for (AuthoritySet set : authList) {
				if (set.getAuthorities().equals(authorities)) {
					return set;
				}
			}
		}
		AuthoritySet newSet = new AuthoritySet();
		newSet.setScope(scope);
		newSet.setAuthorities(authorities);
		return this.authSetRepository.save(newSet);
	}
}
