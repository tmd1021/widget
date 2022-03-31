package edu.sru.cpsc.webshopping.domain.billing;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import org.springframework.lang.NonNull;


/**
 * Holds the payment information of a buyer
 * Money spent on purchases should be withdrawn from this account
 */
@Entity
public class PaymentDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	// Card processor, i.e. Discover Card, Visa
	@NonNull
	private String cardType;
	
	@NonNull
	private String cardholderName;
	
	@NonNull
	private String cardNumber;
	
	@NonNull
	private String expirationDate;
	
	@NonNull
	private String securityCode;
	
	@NonNull
	private String postalCode;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardholderName() {
		return cardholderName;
	}

	public void setCardholderName(String cardholderName) {
		this.cardholderName = cardholderName;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	// Sets the non-id fields of the calling PaymentDetails to match that of the passed PaymentDetails
	public void transferFields(PaymentDetails other) {
		this.cardType = other.cardType;
		this.cardholderName = other.cardholderName;
		this.cardNumber = other.cardNumber;
		this.expirationDate = other.expirationDate;
		this.securityCode = other.securityCode;
		this.postalCode = other.postalCode;
	}
	
	public void buildFromForm(PaymentDetails_Form form) {
		this.cardType = form.getCardType();
		this.cardholderName = form.getCardholderName();
		this.cardNumber = form.getCardNumber();
		this.expirationDate = form.getExpirationDate();
		this.securityCode = form.getSecurityCode();
		this.postalCode = form.getPostalCode();
	}
}
