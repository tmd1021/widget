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
 * Inherits from the widget class and adds attributes for electronics.
 * @author NICK
 *
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)

public class Widget_Electronics extends Widget {

	@NonNull
	private String officeUse;
	
	@NonNull
	private String entertainmentUse;

	public String getOfficeUse() {
		return officeUse;
	}

	public void setOfficeUse(String officeUse) {
		this.officeUse = officeUse;
	}

	public String getEntertainmentUse() {
		return entertainmentUse;
	}

	public void setEntertainmentUse(String entertainmentUse) {
		this.entertainmentUse = entertainmentUse;
	}
	
	
}
