package com.andrewsha.int42h.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomUsernamePasswordAuthFilter extends UsernamePasswordAuthenticationFilter {
	private AuthenticationFailureHandler authenticationFailureHandler;
	private AuthenticationSuccessHandler authenticationSuccessHandler;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		Authentication authentication = super.attemptAuthentication(request, response);
		return authentication;
	}

	public CustomUsernamePasswordAuthFilter(AuthenticationManager authenticationManager,
			AuthenticationFailureHandler authenticationFailureHandler,
			AuthenticationSuccessHandler authenticationSuccessHandler) {
		super(authenticationManager);
		this.authenticationFailureHandler = authenticationFailureHandler;
		this.authenticationSuccessHandler = authenticationSuccessHandler;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain, Authentication authResult)
			throws IOException, ServletException {
		this.authenticationSuccessHandler.onAuthenticationSuccess(request, response, authResult);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
			throws IOException, ServletException {
		this.authenticationFailureHandler.onAuthenticationFailure(request, response, failed);
	}
}
