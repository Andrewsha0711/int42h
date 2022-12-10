package com.andrewsha.int42h.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.andrewsha.int42h.exception.CustomExceptionBody;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			org.springframework.security.core.AuthenticationException exception)
			throws IOException, ServletException {

		HttpStatus status = HttpStatus.FORBIDDEN;
		response.setStatus(status.value());
		CustomExceptionBody appException = new CustomExceptionBody(
				exception.getClass().getSimpleName(), exception.getMessage(), status);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(response.getOutputStream(), appException);
	}
}
