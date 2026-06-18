package com.java.javasecurity.daoImpl;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import com.java.javasecurity.dao.UserDetailsDao;
import com.java.javasecurity.query.UserDetailsQuery;

@Repository
public class UserDetailsDaoImpl implements UserDetailsDao {

	private final UserDetailsQuery userDetailsQuery;

	private final DataSource securityDB;

	public UserDetailsDaoImpl(UserDetailsQuery detailsQuery, DataSource securityDB) {
		this.userDetailsQuery = detailsQuery;
		this.securityDB = securityDB;
	}

	@Override
	public Map<String, Object> getUserDetails(String username) throws UsernameNotFoundException {
		try {

			return new JdbcTemplate(securityDB).queryForMap(userDetailsQuery.getUserDetails(), username);
		} catch (EmptyResultDataAccessException e) {
			throw new UsernameNotFoundException("User Details Not Found");
		}
	}

}
