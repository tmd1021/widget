package edu.sru.cpsc.webshopping.domain.billing;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/** 
 * A mirror of DirectDepositDetails used for validating form input
 */
public class DirectDepositDetails_Form {
	
	@NotEmpty(message = "The Account Holder Name cannot be empty.")
	private String accountholderName;
	
	@Size(min = 9, max = 9, message = "The Routing Number must have 9 numbers.")
	private String routingNumber;
	
	@Size(min = 1, max = 17, message = "The Account Number must be between 1 and 17 numbers.")
	private String accountNumber;
	
	@Size(min = 1, message = "The Bank Name must be at least 1 character.")
	private String bankName;

	public String getAccountholderName() {
		return accountholderName;
	}

	public void setAccountholderName(String accountholderName) {
		this.accountholderName = accountholderName;
	}

	public String getRoutingNumber() {
		return routingNumber;
	}

	public void setRoutingNumber(String routingNumber) {
		this.routingNumber = routingNumber;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
}
