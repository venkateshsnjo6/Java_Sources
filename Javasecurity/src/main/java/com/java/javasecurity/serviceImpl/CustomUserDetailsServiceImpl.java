package com.java.javasecurity.serviceImpl;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.java.javasecurity.dao.UserDetailsDao;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

	private final UserDetailsDao userDetailsDao;

	public CustomUserDetailsServiceImpl(UserDetailsDao userDetailsDao) {
		this.userDetailsDao = userDetailsDao;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Map<String, Object> userDetailsMap = userDetailsDao.getUserDetails(username);
		return new User(String.valueOf(userDetailsMap.get("name")), String.valueOf(userDetailsMap.get("password")),
				Collections.singleton(new SimpleGrantedAuthority("user")));
	}

}
