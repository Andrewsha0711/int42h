package com.andrewsha.int42h.exception;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

public class CustomExceptionBody {
	private final String title;
	private final String detail;
	private final HttpStatus httpStatus;
	private final String timestamp;
	
	public CustomExceptionBody(String title, String detail, HttpStatus httpStatus, ZonedDateTime timestamp) {
		super();
		this.title = title;
		this.detail = detail;
		this.httpStatus = httpStatus;
		this.timestamp = timestamp.toString();
	}
	
	public CustomExceptionBody(String title, String detail, HttpStatus httpStatus) {
		super();
		this.title = title;
		this.detail = detail;
		this.httpStatus = httpStatus;
		this.timestamp = ZonedDateTime.now().toString();
	}

	public String getTitle() {
		return title;
	}

	public String getDetail() {
		return detail;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public String getTimestamp() {
		return timestamp;
	}
}
