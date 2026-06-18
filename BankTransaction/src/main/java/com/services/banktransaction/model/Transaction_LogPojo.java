package com.services.banktransaction.model;

import lombok.Data;

@Data
public class Transaction_LogPojo {

	private String accountNumber;
	private String status;
	private String remarks;

}
