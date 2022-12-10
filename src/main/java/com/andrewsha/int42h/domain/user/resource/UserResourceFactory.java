package com.andrewsha.int42h.domain.user.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.andrewsha.int42h.domain.user.User;

@Component
public class UserResourceFactory {
	public Iterable<UserResource> getAll(Iterable<User> source) {
		List<UserResource> resource = new ArrayList<>();
		for (User user : source) {
			resource.add(new UserResource(user.getId().toString(), user.getName(), user.getEmail()));
		}
		return resource;
	}

	public UserResource getOne(User source) {
		return new UserResource(source.getId().toString(), source.getName(), source.getEmail());
	}
}
