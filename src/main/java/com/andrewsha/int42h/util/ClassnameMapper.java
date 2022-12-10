package com.andrewsha.int42h.util;

import java.util.Map;

import com.andrewsha.int42h.domain.user.User;

public class ClassnameMapper {
	private static final Map<String, Class<?>> classnameMap = Map.of(
			"User", User.class);
	
	public static Class<?> getClass(String className) {
		return classnameMap.get(className);
	}
}
