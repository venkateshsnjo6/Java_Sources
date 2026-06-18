package com.source.svnsourcedownloader.connection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.jora.encodedecode.common.EncryptionDecryption;

import jakarta.annotation.PostConstruct;

@Configuration
@PropertySource("classpath:application.properties")
public class ConnectionConfig {
	@Value("${companyid}")
	private String companyID;
	@Value("${servername}")
	private String serverName;
	@Value("${portno}")
	private String portNo;
	@Value("${userid}")
	private String userId;
	@Value("${password}")
	private String password;

	@Value("${svnurl}")
	private String svnurl;
	@Value("${destinationurl}")
	private String destinationurl;
	@Value("${svnuserid}")
	private String svnuserid;
	@Value("${svnpassword}")
	private String svnpassword;

	@Value("${basedon.data:N}")
	private String basedon;

	@Value("${othersource.backupNeed:N}")
	private String otherSrcBackupNeed;

	@Value("${othersource.pathDetails:}")
	private String otherSrcPath;

	private static String svnUrl, destinationUrl, svnUser, svnPassword, backupTime, basedOn, otherSourceBakNeed,
			otherSourcePath;

	@Bean("dbconn")
	@ConditionalOnProperty(prefix = "basedon", value = "data", havingValue = "D", matchIfMissing = false)
	@Primary
	public DataSource getHoMasterConn() throws Exception {
		DriverManagerDataSource dataSource = getConnection(EncryptionDecryption.decrypt(serverName),
				EncryptionDecryption.decrypt(portNo), EncryptionDecryption.decrypt(companyID),
				EncryptionDecryption.decrypt(userId), EncryptionDecryption.decrypt(password));
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.queryForObject("Select 1", Integer.class);
		return dataSource;
	}

	public DriverManagerDataSource getConnection(String serverName, String portNo, String dbName, String userName,
			String password) {
		try {

			DriverManagerDataSource dataSource = new DriverManagerDataSource();
			dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			dataSource.setUrl("jdbc:sqlserver://" + serverName + ":" + portNo + ";databaseName=" + dbName
					+ ";instance=SQLSERVER;useSSL=false;trustServerCertificate=true;encrypt=false");
			dataSource.setUsername(userName);
			dataSource.setPassword(password);
			return dataSource;
		} catch (Exception e) {
			throw e;
		}

	}

	@PostConstruct
	public void init() {
		svnUrl = svnurl;
		destinationUrl = destinationurl;
		svnUser = svnuserid;
		svnPassword = svnpassword;
		basedOn = basedon;
		otherSourceBakNeed = otherSrcBackupNeed;
		otherSourcePath = otherSrcPath;
	}

	public static String getSvnUrl() {
		return svnUrl;
	}

	public static String getDestinationUrl() {
		return destinationUrl;
	}

	public static String getSvnUserid() {
		return svnUser;
	}

	public static String getSvnPassword() {
		return svnPassword;
	}

	public static String getBackUpTime() {
		return backupTime;
	}

	public static String getBasedOn() {
		return basedOn;
	}

	public static String getOtherSourceBakNeed() {
		return otherSourceBakNeed;
	}

	public static String getOtherSourcePath() {
		return otherSourcePath;
	}

}
