package com.java.javasecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.java.javasecurity.security.JWTFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JWTFilter jwtFilter;

	public SecurityConfig(JWTFilter jwtFilter) {
		this.jwtFilter = jwtFilter;
	}

	@Bean
	public DefaultSecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(
				a -> a.requestMatchers("/userLogin/login").permitAll().requestMatchers("/**").authenticated())
				.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).csrf(c -> c.disable());

		return http.build();

	}

	@Bean
	public PasswordEncoder passwordEncoder() throws Exception {
		return new BCryptPasswordEncoder();
	}

	@Bean("authenticationManager")
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
		return authenticationConfiguration.getAuthenticationManager();
	}

}
