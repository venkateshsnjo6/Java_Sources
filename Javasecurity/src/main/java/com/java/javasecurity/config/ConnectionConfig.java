package com.java.javasecurity.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class ConnectionConfig {

	@Bean("securityDB")
	public DataSource securityDB() throws Exception {
		return getDataSource("localhost", "3306", "root", "Venk@123", "security");
	}

	private DataSource getDataSource(String ip, String port, String userId, String password, String dbName)
			throws Exception {

		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setJdbcUrl("jdbc:mysql://" + ip + ":" + port + "/" + dbName);
		dataSource.setUsername(userId);
		dataSource.setPassword(password);
		dataSource.setPoolName("Security");
		dataSource.setMaximumPoolSize(2);

		return dataSource;
	}

}
