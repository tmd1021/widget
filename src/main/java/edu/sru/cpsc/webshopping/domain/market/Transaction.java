package edu.sru.cpsc.webshopping.domain.market;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.NonNull;

import edu.sru.cpsc.webshopping.domain.user.User;



/**
 * Holds information on a purchase a User has made
 */
@Entity
public class Transaction {
	public final static BigDecimal WEBSITE_CUT_PERCENTAGE = new BigDecimal(0.15);
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NonNull
	@Min(value=1, message="Must have at least one item purchased.")
	private int qtyBought;
	
	@NonNull
	@Column(precision = 10, scale = 2, columnDefinition="DECIMAL(10, 2)")
	private BigDecimal totalPriceBeforeTaxes;
	
	@NonNull
	@Column(precision = 10, scale = 2, columnDefinition="DECIMAL(10, 2)")
	private BigDecimal totalPriceAfterTaxes;
	
	@NonNull
	@Column(precision = 10, scale = 2, columnDefinition="DECIMAL(10, 2)")
	private BigDecimal sellerProfit;
	
	@NonNull
	@CreationTimestamp
	@Temporal(TemporalType.DATE)
	private Date purchaseDate;
	
	@NonNull
	@ManyToOne
	private User seller;
	
	@NonNull
	@ManyToOne
	private User buyer;

	@NonNull
	@ManyToOne(cascade = CascadeType.MERGE)
	private MarketListing marketListing;
	
	@OneToOne
	private Shipping shippingEntry;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getQtyBought() {
		return qtyBought;
	}

	public void setQtyBought(int qtyBought) {
		this.qtyBought = qtyBought;
	}

	public BigDecimal getTotalPriceBeforeTaxes() {
		return totalPriceBeforeTaxes;
	}

	public void setTotalPriceBeforeTaxes(BigDecimal totalPrice) {
		this.totalPriceBeforeTaxes = totalPrice;
	}

	public BigDecimal getTotalPriceAfterTaxes() {
		return totalPriceAfterTaxes;
	}

	public void setTotalPriceAfterTaxes(BigDecimal totalPriceAfterTaxes) {
		this.totalPriceAfterTaxes = totalPriceAfterTaxes;
	}

	public BigDecimal getSellerProfit() {
		return sellerProfit;
	}

	public void setSellerProfit(BigDecimal sellerProfit) {
		this.sellerProfit = sellerProfit;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public User getBuyer() {
		return buyer;
	}

	public void setBuyer(User buyer) {
		this.buyer = buyer;
	}
	
	public User getSeller() {
		return seller;
	}

	public void setSeller(User seller) {
		this.seller = seller;
	}

	public Shipping getShippingEntry() {
		return shippingEntry;
	}

	public void setShippingEntry(Shipping shippingEntry) {
		this.shippingEntry = shippingEntry;
	}

	public MarketListing getMarketListing() {
		return marketListing;
	}

	public void setMarketListing(MarketListing marketListing) {
		this.marketListing = marketListing;
	}
}
