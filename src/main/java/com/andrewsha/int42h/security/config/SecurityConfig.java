package com.andrewsha.int42h.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.andrewsha.int42h.security.filter.CustomUsernamePasswordAuthFilter;
import com.andrewsha.int42h.security.filter.TokenFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private AccessDeniedHandler accessDeniedHandler;
	@Autowired
	private TokenFilter tokenFilter;
	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;
	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;
	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http,
			PasswordEncoder passwordEncoder, UserDetailsService userDetailsService)
			throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder).and()
				.build();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http,
			AuthenticationManager authenticationManager) throws Exception {
		CustomUsernamePasswordAuthFilter usernamePasswordAuthFilter =
				new CustomUsernamePasswordAuthFilter(authenticationManager,
						authenticationFailureHandler, authenticationSuccessHandler);
		usernamePasswordAuthFilter.setFilterProcessesUrl("/api/login");
		http.csrf().disable();
		http.cors();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.authorizeRequests().antMatchers("/api/login/**").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/user").permitAll();
		// TODO delete
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/users/").permitAll();

		http.authorizeRequests().anyRequest().authenticated();

		http.addFilter(usernamePasswordAuthFilter);
		http.addFilterBefore(this.tokenFilter, UsernamePasswordAuthenticationFilter.class);

		http.exceptionHandling().accessDeniedHandler(this.accessDeniedHandler);
		http.exceptionHandling().authenticationEntryPoint(this.authenticationEntryPoint);
		return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.debug(true).ignoring().antMatchers("/css/**", "/js/**", "/img/**",
				"/lib/**", "/favicon.ico");
	}
}
