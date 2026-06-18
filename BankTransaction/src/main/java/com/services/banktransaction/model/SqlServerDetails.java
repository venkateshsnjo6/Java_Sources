package com.services.banktransaction.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "sqlserver")
public class SqlServerDetails {

	private String serverName;

	private String portNo;

	private String userId;

	private String password;

	private String dbname;

}
