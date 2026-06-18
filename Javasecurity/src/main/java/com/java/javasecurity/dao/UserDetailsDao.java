package com.java.javasecurity.dao;

import java.util.Map;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsDao {

	Map<String, Object> getUserDetails(String username) throws UsernameNotFoundException;

}
