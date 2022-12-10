package com.andrewsha.int42h.security.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.andrewsha.int42h.util.ClassnameMapper;

public class CustomPermissionEvaluator implements PermissionEvaluator {
	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject,
			Object permission) {
		if (targetDomainObject == null || permission == null || !(permission instanceof String)) {
			return false;
		}

		if (targetDomainObject instanceof IBinaryTree) {
			if (this.hasAuthority(authentication, ((IBinaryTree) targetDomainObject).getId(),
					targetDomainObject.getClass().getSimpleName(), permission)) {
				return true;
			}
			Map.Entry<String, Serializable> parentMeta =
					((IBinaryTree) targetDomainObject).getParent();
			if (this.hasAuthority(authentication, parentMeta.getValue(),
					targetDomainObject.getClass().getSimpleName(), permission)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId,
			String targetType, Object permission) {

		if (this.hasAuthority(authentication, targetId, targetType, permission)) {
			return true;
		}
		Optional<CrudRepository<Object, Serializable>> repository =
				this.getTargetObjectRepository(targetType);
		if (!repository.isPresent()) {
			return false;
		}
		Optional<Object> optionalTargetObject = repository.get().findById(targetId);

		if (optionalTargetObject.isPresent()) {
			Object targetObject = optionalTargetObject.get();
			if (targetObject instanceof IBinaryTree) {
				Map.Entry<String, Serializable> parentMeta =
						((IBinaryTree) targetObject).getParent();
				if (this.hasAuthority(authentication, parentMeta.getValue(), targetType,
						permission)) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean hasAuthority(Authentication authentication, Serializable targetId,
			String targetType, Object permission) {
		String targetAuthority = "PERMISSION_" + ((String) permission).toUpperCase() + "_"
				+ targetType.toUpperCase() + "_" + targetId.toString();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		for (GrantedAuthority authority : authorities) {
			if (authority.getAuthority().startsWith(targetAuthority)) {
				return true;
			}
		}
		return false;
	}

	private Optional<CrudRepository<Object, Serializable>> getTargetObjectRepository(
			String targetType) {
		try {
			Class<?> type = Class.forName(ClassnameMapper.getClass(targetType).getCanonicalName());
			Repositories repositories = new Repositories(applicationContext);
			Optional<Object> optional = repositories.getRepositoryFor(type);
			if (optional.isPresent()) {
				Object object = optional.get();
				if (object instanceof CrudRepository<?, ?>) {
					return Optional.of((CrudRepository<Object, Serializable>) object);
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO log
		} catch (Exception e) {
			// TODO log
		}
		return null;
	}
}
