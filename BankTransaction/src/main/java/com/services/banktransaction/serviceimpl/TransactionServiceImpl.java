package com.services.banktransaction.serviceimpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.services.banktransaction.common.Transaction;
import com.services.banktransaction.common.Enums.KYCStatus;
import com.services.banktransaction.common.Enums.TransactionStatus;
import com.services.banktransaction.common.Enums.TransactionType;
import com.services.banktransaction.dao.CustomerDao;
import com.services.banktransaction.dao.TransactionDao;
import com.services.banktransaction.dao.TransactionLogDao;
import com.services.banktransaction.model.CustomerPojo;
import com.services.banktransaction.model.TransactionPojo;
import com.services.banktransaction.model.Transaction_LogPojo;
import com.services.banktransaction.service.TransactionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

	private final CustomerDao customerDao;

	private final TransactionDao transactionDao;

	private final TransactionLogDao transactionLogDao;

	private final DataSource bankdatasource;

	private final Logger log = LogManager.getLogger(getClass());

	@Override
	public String transactionProcess(Map<String, Object> transactionMap) throws Exception {

		try {

			CustomerPojo customer = customerDao.getAccountNoDetails(String.valueOf(transactionMap.get("accountNo")));

			/*** Validation ***/
			transactionValiDation(customer, transactionMap);

			/*** Preparing Data ***/
			TransactionPojo preparedTransactionData = transactionDataPreparation(customer, transactionMap);

			Transaction_LogPojo transaction_LogPojo = transactionLogDataPreparation(preparedTransactionData);

			/*** Save Process ***/
			return saveTransaction(preparedTransactionData, transaction_LogPojo);
		} catch (Exception e) {
			log.error("transactionProcess=> " + e.getMessage());
			throw e;
		}

	}

	private Transaction_LogPojo transactionLogDataPreparation(TransactionPojo preparedTransactionData)
			throws Exception {

		Transaction_LogPojo transaction_LogPojo = new Transaction_LogPojo();

		transaction_LogPojo.setAccountNumber(preparedTransactionData.getAccountNumber());
		transaction_LogPojo.setRemarks(String.valueOf(TransactionStatus.Success));
		transaction_LogPojo.setStatus(TransactionStatus.Success.getStatusCode());

		return transaction_LogPojo;
	}

	private TransactionPojo transactionDataPreparation(CustomerPojo customer, Map<String, Object> transactionMap)
			throws Exception {

		TransactionPojo transactionPojo = new TransactionPojo();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		transactionPojo.setAccountNumber(customer.getAccountNumber());
		transactionPojo.setAmount(Double.parseDouble(String.valueOf(transactionMap.get("amount"))));
		transactionPojo.setTransactionType(String.valueOf(transactionMap.get("transactionType")));
		transactionPojo.setTransactionDate(LocalDateTime.now().format(formatter));

		return transactionPojo;
	}

	private void transactionValiDation(CustomerPojo customer, Map<String, Object> transactionMap) throws Exception {

		/*** Account No Validation ***/
		if (customer == null) {
			throw new Exception("This Account No Not Exists...!");
		}

		/*** TransactionType Validation ***/
		if (!String.valueOf(transactionMap.get("transactionType"))
				.equalsIgnoreCase(TransactionType.Credit.getTrantype())
				&& !String.valueOf(transactionMap.get("transactionType"))
						.equalsIgnoreCase(TransactionType.Debit.getTrantype())) {
			throw new Exception("""
						Invalid TransactionType Inputs
						The inputs must be in "C" and "D"
					""");
		}

		/*** Amount Should be Greater than 0 ***/

		if (Double.parseDouble(String.valueOf(transactionMap.get("amount"))) <= 0) {
			throw new Exception("Amount Should be Greater than 0");
		}

		/***
		 * If Kyc Status is Pending then Transaction Amount Should be Lesser thaan 50000
		 ***/
		if (customer.getKYCStatus().equalsIgnoreCase(KYCStatus.Pending.getStatus())
				&& Double.parseDouble(String.valueOf(transactionMap.get("amount"))) > 50000) {
			throw new Exception("""
						KYC Status for this Account is Pending
							and Amount Should be Lesser thaan 50000
						Transaction Failed
					""");
		}

		/*** Debited Amount Should be Lesser than Balance Amount ***/

		if (String.valueOf(transactionMap.get("transactionType")).equalsIgnoreCase(TransactionType.Debit.getTrantype())
				&& customer.getBalance() < Double.parseDouble(String.valueOf(transactionMap.get("amount")))) {
			throw new Exception("Debited Amount Should be Lesser than or equal to " + customer.getBalance());
		}

		/*** Daily Transaction Limit For an Account Should be Lesser thaan 100000 ***/

		if (String.valueOf(transactionMap.get("transactionType")).equalsIgnoreCase(TransactionType.Debit.getTrantype())
				&& (transactionDao.getDailyTransactionAmount(customer.getAccountNumber())
						+ Double.parseDouble(String.valueOf(transactionMap.get("amount")))) > 100000) {
			throw new Exception("Daily Transaction Limit is Lesser than <100000");
		}

	}

	private String saveTransaction(TransactionPojo preparedTransactionData, Transaction_LogPojo transaction_LogPojo)
			throws Exception {
		Transaction transaction = null;
		try {

			transaction = new Transaction(bankdatasource);

			transactionDao.saveTransaction(preparedTransactionData);

			customerDao.updateBalance(preparedTransactionData);

			transactionLogDao.saveTransactionLog(transaction_LogPojo);

			transaction.commitTransaction();

			log.info("Save Transaction=> Transaction Completed Successfully \n Account No :"
					+ preparedTransactionData.getAccountNumber() + " \n Amount : "
					+ preparedTransactionData.getAmount());

			return "Transaction Completed Successfully";
		} catch (Exception e) {
			log.error("transaction Save=> " + e.getMessage());
			if (transaction != null)
				transaction.rollbackTransaction();

			transaction_LogPojo.setRemarks(String.valueOf(TransactionStatus.Failure) + " : " + e.getMessage());
			transaction_LogPojo.setStatus(TransactionStatus.Failure.getStatusCode());
			transactionLogDao.saveTransactionLog(transaction_LogPojo);

			throw e;
		}

	}

	@Override
	public List<Map<String, Object>> getTransactionDetails(String acctcode) throws Exception {

		return transactionDao.getTransactionDetails(acctcode);
	}

}
