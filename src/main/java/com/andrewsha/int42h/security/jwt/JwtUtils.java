package com.andrewsha.int42h.security.jwt;

import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtils {
	@Value("${app.security.jwt.secret}")
	private String jwtSecret;
	@Value("${app.security.jwt.token.expired}")
	private String tokenExpired;
	@Value("${app.security.jwt.cookie.name}")
	private String tokenName;
	@Value("${app.security.jwt.cookie.expiry}")
	private String jwtCookieExpiry;

	public Algorithm getAlgorithm() {
		return Algorithm.HMAC256(this.jwtSecret.getBytes());
	}

	public JwtToken getToken(HttpServletRequest request) {
		Cookie tokenCookie = WebUtils.getCookie(request, this.tokenName);
		if (tokenCookie != null) {
			return new JwtToken(this.tokenName, tokenCookie.getValue());
		}
		return null;
	}

	public DecodedJWT validateJwt(JwtToken token) throws TokenExpiredException {
		try {
			JWTVerifier jwtVerifier = JWT.require(this.getAlgorithm()).build();
			return jwtVerifier.verify(token.getValue());
		} catch (TokenExpiredException e) {
			return null;
		}
	}

	public JwtToken generateToken(UserDetails userDetails, HttpServletRequest request) {
		String token = JWT.create().withSubject(userDetails.getUsername())
				.withExpiresAt(
						new Date(System.currentTimeMillis() + Long.valueOf(this.tokenExpired)))
				.withIssuer(request.getRequestURL().toString())
				.withClaim("authorities", userDetails.getAuthorities().stream()
						.map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.sign(this.getAlgorithm());
		return new JwtToken(this.tokenName, token);
	}

	public ResponseCookie createTokenCookie(JwtToken token) {
		return ResponseCookie.from(token.getName(), token.getValue()).domain("").httpOnly(true)
				.path("")
				// .secure(true)
				.sameSite("None").maxAge(Long.parseLong(this.jwtCookieExpiry)).build();
	}
}
