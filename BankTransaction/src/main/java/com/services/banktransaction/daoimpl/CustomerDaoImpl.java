package com.services.banktransaction.daoimpl;

import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.services.banktransaction.common.Enums.StoredProcedures;
import com.services.banktransaction.common.Enums.TransactionType;
import com.services.banktransaction.dao.CustomerDao;
import com.services.banktransaction.model.CustomerPojo;
import com.services.banktransaction.model.SqlServerDetails;
import com.services.banktransaction.model.TransactionPojo;
import com.services.banktransaction.query.CustomerQuery;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Repository
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class CustomerDaoImpl implements CustomerDao {

	private final CustomerQuery customerQuery;

	private final DataSource bankdatasource;

	private final SqlServerDetails serverDetails;

	@Override
	public CustomerPojo getAccountNoDetails(String accountNumber) throws Exception {
		try {

			return new JdbcTemplate(bankdatasource).queryForObject(customerQuery.getCheckAccountNo(),
					BeanPropertyRowMapper.newInstance(CustomerPojo.class), accountNumber);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			if (e.getCause() instanceof SQLException)
				throw new Exception(e.getCause().getMessage());

			throw e;
		}
	}

	@Override
	public int getMaxCustomerId() throws Exception {
		try {

			return new JdbcTemplate(bankdatasource).queryForObject(customerQuery.getMaxCustomerId(), Integer.class);
		} catch (EmptyResultDataAccessException e) {
			return 1;
		} catch (Exception e) {
			if (e.getCause() instanceof SQLException)
				throw new Exception(e.getCause().getMessage());

			throw e;
		}
	}

	@Override
	public CustomerPojo createCustomer(CustomerPojo customer) throws Exception {
		try {
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(bankdatasource)
					.withCatalogName(serverDetails.getDbname())
					.withProcedureName(StoredProcedures.CustomerInsert.getSpName());

			Map<String, Object> map = new ObjectMapper().convertValue(customer, Map.class);

			simpleJdbcCall.execute(map);

			return customer;
		} catch (Exception e) {
			if (e.getCause() instanceof SQLException)
				throw new Exception(e.getCause().getMessage());

			throw e;
		}

	}

	@Override
	public CustomerPojo updateCustomer(CustomerPojo customer) throws Exception {
		try {
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(bankdatasource)
					.withCatalogName(serverDetails.getDbname())
					.withProcedureName(StoredProcedures.CustomerUpdate.getSpName());

			Map<String, Object> map = new ObjectMapper().convertValue(customer, Map.class);

			simpleJdbcCall.execute(map);

			return customer;
		} catch (Exception e) {
			if (e.getCause() instanceof SQLException)
				throw new Exception(e.getCause().getMessage());

			throw e;
		}
	}

	@Override
	public void deleteCustomer(CustomerPojo customer) throws Exception {
		try {
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(bankdatasource)
					.withCatalogName(serverDetails.getDbname())
					.withProcedureName(StoredProcedures.CustomerDelete.getSpName());

			Map<String, Object> map = new ObjectMapper().convertValue(customer, Map.class);

			simpleJdbcCall.execute(map);

		} catch (Exception e) {
			if (e.getCause() instanceof SQLException)
				throw new Exception(e.getCause().getMessage());

			throw e;
		}

	}

	@Override
	public boolean updateBalance(TransactionPojo transactionData) throws Exception {
		try {

			new JdbcTemplate(bankdatasource).update(customerQuery.updateBalance(),
					transactionData.getTransactionType().equalsIgnoreCase(TransactionType.Credit.getTrantype())
							? transactionData.getAmount()
							: (transactionData.getAmount() * -1),
					transactionData.getAccountNumber());

			return true;

		} catch (Exception e) {
			if (e.getCause() instanceof SQLException)
				throw new Exception(e.getCause().getMessage());

			throw e;
		}

	}

}
