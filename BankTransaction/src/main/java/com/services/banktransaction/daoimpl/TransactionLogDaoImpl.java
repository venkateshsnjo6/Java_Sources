package com.services.banktransaction.daoimpl;

import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.services.banktransaction.common.Enums.StoredProcedures;
import com.services.banktransaction.dao.TransactionLogDao;
import com.services.banktransaction.model.SqlServerDetails;
import com.services.banktransaction.model.Transaction_LogPojo;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Repository
@RequiredArgsConstructor
public class TransactionLogDaoImpl implements TransactionLogDao {

	private final DataSource bankdatasource;

	private final SqlServerDetails serverDetails;

	@SuppressWarnings("unchecked")
	@Override
	public boolean saveTransactionLog(Transaction_LogPojo transaction_LogPojo) throws Exception {
		try {
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(bankdatasource)
					.withCatalogName(serverDetails.getDbname())
					.withProcedureName(StoredProcedures.TransactionLogInsert.getSpName());

			Map<String, Object> map = new ObjectMapper().convertValue(transaction_LogPojo, Map.class);

			simpleJdbcCall.execute(map);

			return true;
		} catch (Exception e) {
			if (e.getCause() instanceof SQLException)
				throw new Exception(e.getCause().getMessage());

			throw e;
		}
	}

}
