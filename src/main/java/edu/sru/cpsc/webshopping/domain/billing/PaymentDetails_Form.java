package edu.sru.cpsc.webshopping.domain.billing;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;
import org.springframework.lang.NonNull;

/**
 * A mirror of PaymentDetails used for form submission
 */
public class PaymentDetails_Form {
	// Card processor, i.e. Discover Card, Visa
	@NotEmpty(message = "Card Type cannot be empty.")
	private String cardType;
	
	@NotEmpty(message = "Cardholder Name cannot be empty.")
	private String cardholderName;
	
	@Size(min = 16, max = 16, message = "Card Number must be 16 characters.")
	private String cardNumber;
	
	@NotEmpty(message = "Expiration cannot be empty.")
	private String expirationDate;
	
	@NonNull
	@Size(min = 3, max = 4, message = "Security Code must be between 3 and 4 characters.")
	private String securityCode;
	
	@NonNull
	@Size(min = 5, max = 5, message = "Postal Code must be 5 characters")
	private String postalCode;

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
}
