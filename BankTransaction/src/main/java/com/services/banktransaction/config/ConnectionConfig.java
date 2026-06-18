package com.services.banktransaction.config;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.services.banktransaction.model.SqlServerDetails;
import com.zaxxer.hikari.HikariDataSource;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ConnectionConfig {

	@Value("${spring.application.name}")
	private String appName;

	private final SqlServerDetails serverDetails;

	private final Logger log = LogManager.getLogger(getClass());

	@Bean("bankdatasource")
	public DataSource getBankDataSource() throws Exception {

		return getDataSource(serverDetails.getServerName(), serverDetails.getPortNo(), serverDetails.getPassword(),
				serverDetails.getUserId(), serverDetails.getDbname());
	}

	public HikariDataSource getDataSource(String servername, String portno, String password, String userName,
			String dbName) {
		HikariDataSource hikariDataSource = null;
		try {
			hikariDataSource = new HikariDataSource();
			hikariDataSource.setPoolName(appName);

			StringBuilder bldr = new StringBuilder();
			bldr.append("jdbc:mysql://" + servername + ":" + portno + "/" + dbName);
			bldr.append(
					"?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=CONVERT_TO_NULL&connectionTimeZone=SERVER&forceConnectionTimeZoneToSession=true&preserveInstants=false&useSSL=false&rewriteBatchedStatements=true&useServerPrepStmts=false&connectionAttributes=program_name:")
					.append(appName).append("&allowPublicKeyRetrieval=true");
			hikariDataSource.setJdbcUrl(bldr.toString());
			hikariDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
			hikariDataSource.setUsername(userName);
			hikariDataSource.setPassword(password);
			hikariDataSource.setConnectionInitSql("select 1");
			hikariDataSource.setConnectionTestQuery("select 1");
			hikariDataSource.setMaximumPoolSize(10);
			hikariDataSource.setConnectionTimeout(30000);
			hikariDataSource.setIdleTimeout(60000);
			hikariDataSource.setMaxLifetime(60000);

			new JdbcTemplate(hikariDataSource).queryForObject("Select 1", Integer.class);
		} catch (Exception e) {
			log.error("Connection=> " + e.getMessage());
		}
		return hikariDataSource;
	}

}
