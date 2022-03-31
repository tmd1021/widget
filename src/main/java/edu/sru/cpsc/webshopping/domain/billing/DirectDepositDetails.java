package edu.sru.cpsc.webshopping.domain.billing;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.lang.NonNull;


/**
 * Holds the direct deposit information of a seller
 * Money given to the seller by the website should be sent here
 */
@Entity
public class DirectDepositDetails {
	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NonNull
	private String accountholderName;
	
	@NonNull
	private String routingNumber;
	
	@NonNull
	private String accountNumber;

	@NonNull
	private String bankName;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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

	// Sets the non-id fields of the calling DirectDepositDetails to 
	// match that of the passed DirectDepositDetails
	public void transferFields(DirectDepositDetails other) {
		this.accountholderName = other.accountholderName;
		this.routingNumber = other.routingNumber;
		this.accountNumber = other.accountNumber;
		this.bankName = other.bankName;
	}

	public void buildFromForm(DirectDepositDetails_Form other) {
		this.accountholderName = other.getAccountholderName();
		this.routingNumber = other.getRoutingNumber();
		this.accountNumber = other.getAccountNumber();
		this.bankName = other.getBankName();
	}
	
}
