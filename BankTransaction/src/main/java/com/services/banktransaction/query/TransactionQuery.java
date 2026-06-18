package com.services.banktransaction.query;

import org.springframework.stereotype.Component;

import com.services.banktransaction.common.Enums.StoredProcedures;

@Component
public class TransactionQuery {

	public String checkTransactionDetails() {
		StringBuilder builder = new StringBuilder();
		builder.append("select Count(1) cnt from Transactions where AccountNumber=?;");
		return builder.toString();
	}

	public String getDailyTransactionAmount() {
		StringBuilder bldr = new StringBuilder();
		bldr.append("select ifnull(sum(Amount),0) dailytransaction from transactions \n ");
		bldr.append("where TransactionType='D' and cast(TransactionDate as date)=curdate()	\n ");
		bldr.append("and AccountNumber=? ;\n ");
		return bldr.toString();
	}

	public String getTransactionDetails() {
		StringBuilder builder = new StringBuilder();
		builder.append("CALL ").append(StoredProcedures.TransactionByAccount.getSpName()).append("(?);");
		return builder.toString();
	}

}
