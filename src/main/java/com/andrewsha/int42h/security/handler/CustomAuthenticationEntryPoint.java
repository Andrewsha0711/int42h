package com.andrewsha.int42h.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.andrewsha.int42h.exception.CustomExceptionBody;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			org.springframework.security.core.AuthenticationException authException)
			throws IOException, ServletException {
		// TODO object mapper

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(response.getOutputStream(),
				new CustomExceptionBody("unauthorized", null, HttpStatus.UNAUTHORIZED));

		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
	}
}
