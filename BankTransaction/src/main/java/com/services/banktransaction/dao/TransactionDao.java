package com.services.banktransaction.dao;

import java.util.List;
import java.util.Map;

import com.services.banktransaction.model.TransactionPojo;

public interface TransactionDao {

	boolean checkTransactionDetails(String accountNumber) throws Exception;

	double getDailyTransactionAmount(String accountNumber) throws Exception;

	boolean saveTransaction(TransactionPojo preparedTransactionData) throws Exception;

	List<Map<String, Object>> getTransactionDetails(String acctcode) throws Exception;

}
