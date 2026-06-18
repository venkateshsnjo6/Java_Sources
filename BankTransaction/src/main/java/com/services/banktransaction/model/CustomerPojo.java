package com.services.banktransaction.model;

import lombok.Data;

@Data
public class CustomerPojo {

	private String customerID;
	private String customerName;
	private String accountNumber;
	private double balance;
	private String kYCStatus;

}
