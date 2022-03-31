package edu.sru.cpsc.webshopping.domain.billing;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.lang.NonNull;


/**
 * Holds information on available states, and their associated sales tax
 * Generated using: https://worldpopulationreview.com/state-rankings/sales-tax-by-state
 */
@Entity
public class StateDetails {
	@Id
	private String stateName;
	
	@NonNull
	@Column(precision = 10, scale = 2, columnDefinition="DECIMAL(10, 2)")
	private BigDecimal salesTaxRate;

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public BigDecimal getSalesTaxRate() {
		return salesTaxRate;
	}

	public void setSalesTaxRate(BigDecimal salesTaxRate) {
		this.salesTaxRate = salesTaxRate;
	}
}
