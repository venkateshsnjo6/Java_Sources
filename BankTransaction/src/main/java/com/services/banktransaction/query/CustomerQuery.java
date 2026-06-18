package com.services.banktransaction.query;

import org.springframework.stereotype.Component;

@Component
public class CustomerQuery {

	public String getCheckAccountNo() {
		StringBuilder bldr = new StringBuilder();
		bldr.append("select * from customers where AccountNumber=? \n ");
		return bldr.toString();
	}

	public String getMaxCustomerId() {
		StringBuilder bldr = new StringBuilder();
		bldr.append(
				"select ifnull(max(substring(customerid,4,length(customerid)-1)),0) + 1 maxid from customers c \n ");
		return bldr.toString();
	}

	public String updateBalance() {
		StringBuilder bldr = new StringBuilder();
		bldr.append("update customers c\n ");
		bldr.append("set Balance=Balance + ? \n ");
		bldr.append("where AccountNumber=? \n ");
		return bldr.toString();
	}

}
