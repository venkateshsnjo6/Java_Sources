package com.java.javasecurity.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.java.javasecurity.security.JWTUtil;

@RestController
@CrossOrigin("*")
@RequestMapping("/userLogin")
public class LoginController {

	private final AuthenticationManager authenticationManager;

	private final JWTUtil jwtUtil;

	public LoginController(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

	@GetMapping("/login") 
	public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> userMap) {
		try {
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					String.valueOf(userMap.get("name")), String.valueOf(userMap.get("password"))));

			UserDetails userDetails = (UserDetails) authentication.getPrincipal();

			String token = jwtUtil.tokenGenerator(userDetails);

			return ResponseEntity.ok(Map.of("token", token));

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		}
	}

	@GetMapping("/userDetails")
	public ResponseEntity<List<Map<String, Object>>> getUserDetails() {
		try {

			return ResponseEntity.ok(
					List.of(Map.of("name", "venkat", "password", "123"), Map.of("name", "sara", "password", "456")));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(List.of(Map.of("error", e.getMessage())));
		}
	}
}
