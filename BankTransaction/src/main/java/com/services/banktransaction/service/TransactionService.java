package com.services.banktransaction.service;

import java.util.List;
import java.util.Map;

public interface TransactionService {

	String transactionProcess(Map<String, Object> transactionMap) throws Exception;

	List<Map<String, Object>> getTransactionDetails(String acctcode) throws Exception;

}
