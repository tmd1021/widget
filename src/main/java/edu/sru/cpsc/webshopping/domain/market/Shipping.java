package edu.sru.cpsc.webshopping.domain.market;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.lang.NonNull;

import edu.sru.cpsc.webshopping.domain.billing.ShippingAddress;

/**
 * Associated with a Transaction
 * This holds the shipping information associated with the purchase
 */
@Entity
public class Shipping {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String carrier;
	
	private Date shippingDate;
	
	private Date arrivalDate;
	
	@OneToOne
	@NonNull
	private ShippingAddress address;
	
	@OneToOne
	private Transaction transaction;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public Date getShippingDate() {
		return shippingDate;
	}

	public void setShippingDate(Date shippingDate) {
		this.shippingDate = shippingDate;
	}

	public Date getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public ShippingAddress getAddress() {
		return address;
	}

	public void setAddress(ShippingAddress address) {
		this.address = address;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
	
	/**
	 * Checks if the Shipping label says that the item has been shipped
	 * @return true if the item is shipped, false otherwise
	 */
	public boolean hasShipped() {
		// A shipped item must have both a shipping date and an arrival date
		return shippingDate != null && arrivalDate != null;
	}
	
	/**
	 * Checks if the Shipping label says that the item has arrived
	 * @return true if the item has arrived, false otherwise
	 */
	public boolean hasArrived() {
		return hasShipped() && (LocalDate.now().compareTo(arrivalDate.toLocalDate()) >= 0);
	}
}
