package com.services.banktransaction.daoimpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.services.banktransaction.common.Enums.StoredProcedures;
import com.services.banktransaction.dao.TransactionDao;
import com.services.banktransaction.model.SqlServerDetails;
import com.services.banktransaction.model.TransactionPojo;
import com.services.banktransaction.query.TransactionQuery;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Repository
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class TransactionDaoImpl implements TransactionDao {

	private final TransactionQuery transactionQuery;

	private final DataSource bankdatasource;

	private final SqlServerDetails serverDetails;

	@Override
	public boolean checkTransactionDetails(String accountNumber) throws Exception {
		try {

			return new JdbcTemplate(bankdatasource).queryForObject(transactionQuery.checkTransactionDetails(),
					Integer.class, accountNumber) > 0 ? false : true;
		} catch (Exception e) {
			if (e.getCause() instanceof SQLException)
				throw new Exception(e.getCause().getMessage());

			throw e;
		}
	}

	@Override
	public double getDailyTransactionAmount(String accountNumber) throws Exception {
		try {

			return new JdbcTemplate(bankdatasource).queryForObject(transactionQuery.getDailyTransactionAmount(),
					Double.class, accountNumber);
		} catch (Exception e) {
			if (e.getCause() instanceof SQLException)
				throw new Exception(e.getCause().getMessage());

			throw e;
		}
	}

	@Override
	public boolean saveTransaction(TransactionPojo preparedTransactionData) throws Exception {
		try {
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(bankdatasource)
					.withCatalogName(serverDetails.getDbname())
					.withProcedureName(StoredProcedures.TransactionInsert.getSpName());

			Map<String, Object> map = new ObjectMapper().convertValue(preparedTransactionData, Map.class);

			simpleJdbcCall.execute(map);

			return true;
		} catch (Exception e) {
			if (e.getCause() instanceof SQLException)
				throw new Exception(e.getCause().getMessage());

			throw e;
		}
	}

	@Override
	public List<Map<String, Object>> getTransactionDetails(String acctcode) throws Exception {
		try {

			return new JdbcTemplate(bankdatasource).queryForList(transactionQuery.getTransactionDetails(), acctcode);
		} catch (Exception e) {
			if (e.getCause() instanceof SQLException)
				throw new Exception(e.getCause().getMessage());

			throw e;
		}
	}

}
