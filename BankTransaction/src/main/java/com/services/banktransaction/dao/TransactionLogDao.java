package com.services.banktransaction.dao;

import com.services.banktransaction.model.Transaction_LogPojo;

public interface TransactionLogDao {

	boolean saveTransactionLog(Transaction_LogPojo transaction_LogPojo) throws Exception;

}
