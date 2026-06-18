package com.services.banktransaction.serviceimpl;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.services.banktransaction.common.Transaction;
import com.services.banktransaction.common.Enums.KYCStatus;
import com.services.banktransaction.dao.CustomerDao;
import com.services.banktransaction.dao.TransactionDao;
import com.services.banktransaction.model.CustomerPojo;
import com.services.banktransaction.service.CustomerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

	private final CustomerDao customerDao;

	private final TransactionDao transactionDao;

	private final DataSource bankdatasource;

	private final Logger log = LogManager.getLogger(getClass());

	@Override
	public CustomerPojo createCustomer(CustomerPojo customer) throws Exception {
		Transaction transaction = null;

		try {

			/*** Validation ***/

			if (customer.getCustomerName().trim().equalsIgnoreCase("")) {
				throw new Exception("Customer Name Should not be Empty...!");
			}

			if (customerDao.getAccountNoDetails(customer.getAccountNumber()) != null) {
				throw new Exception("This Account No Already Exists...!");
			}

			if (!customer.getKYCStatus().equalsIgnoreCase(KYCStatus.Verified.getStatus())
					&& !customer.getKYCStatus().equalsIgnoreCase(KYCStatus.Pending.getStatus())) {
				throw new Exception("""
						Invalid KYC Input...!
						KYC Input Must be in "V" or "P"
						""");
			}

			/*** Save Process ***/

			transaction = new Transaction(bankdatasource);

			customer.setCustomerID("CUS".concat(String.valueOf(customerDao.getMaxCustomerId())));

			CustomerPojo customerPojo = customerDao.createCustomer(customer);

			transaction.commitTransaction();

			log.info("Create Customer=> CustomerId " + customer.getCustomerID() + " Created Successfully");

			return customerPojo;

		} catch (Exception e) {
			log.error("create Customer=> " + e.getMessage());

			if (transaction != null)
				transaction.rollbackTransaction();

			throw e;

		}

	}

	@Override
	public CustomerPojo updateCustomer(CustomerPojo customer) throws Exception {
		Transaction transaction = null;
		try {

			/*** Validation ***/

			if (customer.getCustomerName().trim().equalsIgnoreCase("")) {
				throw new Exception("Customer Name Should not be Empty...!");
			}

			if (!customer.getKYCStatus().equalsIgnoreCase("V") && !customer.getKYCStatus().equalsIgnoreCase("P")) {
				throw new Exception("""
						Invalid KYC Input...!
						KYC Input Must be in "V" or "P"
						""");
			}

			/*** Update Process ***/

			transaction = new Transaction(bankdatasource);

			CustomerPojo customerPojo = customerDao.updateCustomer(customer);

			transaction.commitTransaction();

			log.info("Update Customer=> CustomerId " + customer.getCustomerID() + " Updated Successfully");

			return customerPojo;

		} catch (Exception e) {

			log.error("update Customer=> " + e.getMessage());

			if (transaction != null)
				transaction.rollbackTransaction();

			throw e;
		}

	}

	@Override
	public boolean deleteCustomer(CustomerPojo customer) throws Exception {

		Transaction transaction = null;
		try {

			/*** Validation ***/

			if (customer.getAccountNumber().equalsIgnoreCase("")) {
				throw new Exception("Invalid Inputs...!");
			}

			if (!transactionDao.checkTransactionDetails(customer.getAccountNumber())) {
				throw new Exception("""
						This Account no has an Some Entries
						  Unable to Delete this Customer
						""");
			}

			/*** Delete Process ***/

			transaction = new Transaction(bankdatasource);

			customerDao.deleteCustomer(customer);

			transaction.commitTransaction();

			log.info("Delete Customer=> CustomerId " + customer.getCustomerID() + " Deleted Successfully");

			return true;

		} catch (Exception e) {

			log.error("delete Customer=> " + e.getMessage());

			if (transaction != null)
				transaction.rollbackTransaction();

			throw e;
		}

	}

}
