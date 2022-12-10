package com.andrewsha.int42h.domain.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, UUID> {
	Optional<User> findByEmail(String email);
	Optional<User> findByPhoneNumber(String phoneNumber);
}
