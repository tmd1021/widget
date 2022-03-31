package edu.sru.cpsc.webshopping.domain.billing;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.springframework.lang.NonNull;


/**
 * The address to ship a purchase to
 */
@Entity
public class ShippingAddress {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NonNull
	private String recipient;
	
	@NonNull
	private String streetAddress;
	
	@NonNull
	private String postalCode;
	
	@NonNull
	@OneToOne
	private StateDetails state;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public StateDetails getState() {
		return state;
	}

	public void setState(StateDetails state) {
		this.state = state;
	}
	
	public boolean buildFromForm(ShippingAddress_Form other) {
		this.recipient = other.getRecipient();
		this.streetAddress = other.getStreetAddress();
		this.postalCode = other.getPostalCode();
		this.state = other.getState();
		return true;
	}
}
