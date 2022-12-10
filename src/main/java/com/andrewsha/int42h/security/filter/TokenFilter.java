package com.andrewsha.int42h.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.andrewsha.int42h.exception.JwtUtilsException;
import com.andrewsha.int42h.security.jwt.JwtToken;
import com.andrewsha.int42h.security.jwt.JwtUtils;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class TokenFilter extends OncePerRequestFilter {
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private JwtUtils jwtUtils;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		try {
			JwtToken token = this.jwtUtils.getToken(request);
			if (token != null) {
				UserDetails userDetails;
				DecodedJWT decodedToken = this.jwtUtils.validateJwt(token);
				if (decodedToken != null) {
					String username = decodedToken.getSubject();
					userDetails = userDetailsService.loadUserByUsername(username);
				} else {
					throw new JwtUtilsException("token expired");
				}
				UsernamePasswordAuthenticationToken authenticationToken =
						new UsernamePasswordAuthenticationToken(userDetails, null,
								userDetails.getAuthorities());
				authenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContext context = SecurityContextHolder.createEmptyContext();
				context.setAuthentication(authenticationToken);
				SecurityContextHolder.setContext(context);
			}
		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
		}
		response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
		// TODO add origin url
		response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "");
		filterChain.doFilter(request, response);
	}
}
