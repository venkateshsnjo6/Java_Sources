package com.services.banktransaction.dao;

import com.services.banktransaction.model.CustomerPojo;
import com.services.banktransaction.model.TransactionPojo;

public interface CustomerDao {

	CustomerPojo getAccountNoDetails(String accountNumber) throws Exception;

	int getMaxCustomerId() throws Exception;

	CustomerPojo createCustomer(CustomerPojo customer) throws Exception;

	CustomerPojo updateCustomer(CustomerPojo customer) throws Exception;

	void deleteCustomer(CustomerPojo customer) throws Exception;

	boolean updateBalance(TransactionPojo transactionData) throws Exception;

}
