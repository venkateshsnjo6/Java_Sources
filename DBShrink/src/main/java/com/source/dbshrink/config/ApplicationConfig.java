package com.source.dbshrink.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class ApplicationConfig {

	public String serverName, userName, passWord;
	public int portNo;

	public HikariDataSource getDataSource(String servername, int portno, String password, String userName,
			String dbName) throws Exception {
		HikariDataSource hikariDataSource = null;
		try {
			hikariDataSource = new HikariDataSource();
			hikariDataSource.setPoolName("DBSHRINK");
			hikariDataSource.setJdbcUrl("jdbc:sqlserver://" + servername + ":" + portno + ";databaseName=" + dbName
					+ ";useSSL=false;encrypt=false; trustServerCertificate=true;autoReconnect=true");
			hikariDataSource.setUsername(userName);
			hikariDataSource.setPassword(password);
			hikariDataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			hikariDataSource.setConnectionInitSql("select 1");
			hikariDataSource.setConnectionTestQuery("select 1");
//			hikariDataSource.setMaximumPoolSize(10);
			hikariDataSource.setConnectionTimeout(30000);
			hikariDataSource.setIdleTimeout(60000);
			hikariDataSource.setMaxLifetime(60000);

			this.serverName = servername;
			this.portNo = portno;
			this.passWord = password;
			this.userName = userName;

			new JdbcTemplate(hikariDataSource).queryForObject("Select 1", Integer.class);

			return hikariDataSource;
		} catch (Exception e) {
			throw e;
		}
	}

	public String getServerName() {
		return serverName;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public int getPortNo() {
		return portNo;
	}

}
