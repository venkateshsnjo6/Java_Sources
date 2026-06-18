package com.java.javasecurity.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {
	private final String secretKeyString = "46d83f5b4a105e670dc44412c6690fece06d9535b0277b13bc0e1ff23170e58c";

	private final SecretKey secretKeys = Keys.hmacShaKeyFor(secretKeyString.getBytes());

	public String tokenGenerator(UserDetails userDetails) throws Exception {
		return Jwts.builder().subject(userDetails.getUsername()).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() * 1000 * 60 * 60)).signWith(secretKeys, Jwts.SIG.HS256)
				.compact();
	}

	public boolean validateToken(String tokenSubject,UserDetails userDetails) throws Exception {
		return userDetails.getUsername().equals(tokenSubject);
	}
	
	public String extractSubjectName(String token) throws Exception {

		return Jwts.parser().verifyWith(secretKeys).build().parseSignedClaims(token).getPayload().getSubject();
	}

}
