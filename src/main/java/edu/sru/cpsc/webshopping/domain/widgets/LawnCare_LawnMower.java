package edu.sru.cpsc.webshopping.domain.widgets;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.lang.NonNull;

import edu.sru.cpsc.webshopping.domain.market.MarketListing;
import edu.sru.cpsc.webshopping.domain.user.User;

/**
 * Inherits from the lawn care class and adds attributes for lawn mowers.
 * @author NICK
 *
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class LawnCare_LawnMower extends Widget_LawnCare{

	@NonNull
	private String brand;
	
	@NonNull
	private String powerSource;
	
	@NonNull
	private String bladeWidth;
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getPowerSource() {
		return powerSource;
	}

	public void setPowerSource(String powerSource) {
		this.powerSource = powerSource;
	}

	public String getBladeWidth() {
		return bladeWidth;
	}

	public void setBladeWidth(String bladeWidth) {
		this.bladeWidth = bladeWidth;
	}
	
	
}
