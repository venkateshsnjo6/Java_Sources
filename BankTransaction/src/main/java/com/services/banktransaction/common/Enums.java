package com.services.banktransaction.common;

public class Enums {

	public enum StoredProcedures {

		CustomerInsert("SP_InsertCustomer"), CustomerUpdate("SP_UpdateCustomer"), CustomerDelete("SP_DeleteCustomer"),
		TransactionInsert("SP_InsertTransaction"), TransactionByAccount("sp_GetTransactionsByAccount"),
		TransactionLogInsert("SP_InsertTransactionLog");

		String spName;

		StoredProcedures(String sp) {
			this.spName = sp;
		}

		public String getSpName() {
			return spName;
		}

	}

	public enum TransactionStatus {
		Success("S"), Failure("F");

		String statusCode;

		private TransactionStatus(String status) {
			this.statusCode = status;
		}

		public String getStatusCode() {
			return statusCode;
		}

	}

	public enum TransactionType {
		Credit("C"), Debit("D");

		String trantype;

		private TransactionType(String code) {
			this.trantype = code;
		}

		public String getTrantype() {
			return trantype;
		}

	}

	public enum KYCStatus {
		Verified("V"), Pending("P");

		String status;

		private KYCStatus(String code) {
			this.status = code;
		}

		public String getStatus() {
			return status;
		}

	}

}
