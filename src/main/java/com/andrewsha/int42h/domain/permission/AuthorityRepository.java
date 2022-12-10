package com.andrewsha.int42h.domain.permission;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	public Optional<Authority> findByName(String name);
    //TODO useless
	public boolean existsByName(String name);
}
