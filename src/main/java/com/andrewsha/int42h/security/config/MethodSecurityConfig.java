package com.andrewsha.int42h.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import com.andrewsha.int42h.security.util.CustomPermissionEvaluator;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		DefaultMethodSecurityExpressionHandler expressionHandler =
				new DefaultMethodSecurityExpressionHandler();
		expressionHandler.setPermissionEvaluator(this.permissionEvaluator());
		return expressionHandler;
	}

	@Bean
	PermissionEvaluator permissionEvaluator() {
		return new CustomPermissionEvaluator();
	}
}
