package com.services.banktransaction.common;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Component
public class Transaction {
	private DefaultTransactionDefinition defaultTransactionDefinition;
	private TransactionStatus tranStatus = null;
	private PlatformTransactionManager platformTransactionManager;

	public Transaction() {

	}

	public Transaction(DataSource dataSource) {
		platformTransactionManager = new DataSourceTransactionManager(dataSource);
		defaultTransactionDefinition = new DefaultTransactionDefinition();
		defaultTransactionDefinition.setIsolationLevel(DefaultTransactionDefinition.ISOLATION_READ_COMMITTED);
		tranStatus = platformTransactionManager.getTransaction(defaultTransactionDefinition);
	}

	public Transaction(JdbcTemplate jdbcTemplate) {
		platformTransactionManager = new DataSourceTransactionManager(jdbcTemplate.getDataSource());
		defaultTransactionDefinition = new DefaultTransactionDefinition();
		defaultTransactionDefinition.setIsolationLevel(DefaultTransactionDefinition.ISOLATION_READ_COMMITTED);
		tranStatus = platformTransactionManager.getTransaction(defaultTransactionDefinition);
	}

	public void commitTransaction() {
		if (null != tranStatus) {
			platformTransactionManager.commit(tranStatus);
			tranStatus = null;
		}
	}

	public void rollbackTransaction() {
		if (null != tranStatus) {
			platformTransactionManager.rollback(tranStatus);
			tranStatus = null;
		}
	}
}