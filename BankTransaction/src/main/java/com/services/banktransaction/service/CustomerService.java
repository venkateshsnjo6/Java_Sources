package com.services.banktransaction.service;

import com.services.banktransaction.model.CustomerPojo;

public interface CustomerService {

	CustomerPojo createCustomer(CustomerPojo customer) throws Exception;

	CustomerPojo updateCustomer(CustomerPojo customer) throws Exception;

	boolean deleteCustomer(CustomerPojo customer) throws Exception;

}
