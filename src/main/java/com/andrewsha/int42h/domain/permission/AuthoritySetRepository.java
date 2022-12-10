package com.andrewsha.int42h.domain.permission;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthoritySetRepository extends JpaRepository<AuthoritySet, UUID> {
	public List<AuthoritySet> findByScope(String scope);
}
