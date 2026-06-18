package com.services.banktransaction.model;

import lombok.Data;

@Data
public class TransactionPojo {
	private int transactionID;
	private String accountNumber;
	private double amount;
	private String transactionType;
	private String transactionDate;

}
