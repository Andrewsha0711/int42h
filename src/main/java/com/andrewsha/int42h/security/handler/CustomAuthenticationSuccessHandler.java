package com.andrewsha.int42h.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.andrewsha.int42h.security.jwt.JwtToken;
import com.andrewsha.int42h.security.jwt.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	@Autowired
	private JwtUtils jwtUtils;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		JwtToken token =
				this.jwtUtils.generateToken((UserDetails) authentication.getPrincipal(), request);
		response.setHeader(HttpHeaders.SET_COOKIE,
				this.jwtUtils.createTokenCookie(token).toString());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.OK.value());
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(response.getOutputStream(), authentication.getPrincipal());
	}
}
