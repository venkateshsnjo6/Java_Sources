package com.java.javasecurity.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.java.javasecurity.serviceImpl.CustomUserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter {

	private final JWTUtil jwtUtil;
	private final CustomUserDetailsServiceImpl customUserDetailsServiceImpl;

	public JWTFilter(JWTUtil jwtUtil, CustomUserDetailsServiceImpl customUserDetailsServiceImpl) {
		this.jwtUtil = jwtUtil;
		this.customUserDetailsServiceImpl = customUserDetailsServiceImpl;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header = request.getHeader("Authorization");

		if (header == null || !header.contains("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			String token = header.substring(7);

			String tokenSubject = jwtUtil.extractSubjectName(token);

			if (tokenSubject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(tokenSubject);
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.getWriter().write("Invalid Token");
			return;
		}
		filterChain.doFilter(request, response);
	}

}
