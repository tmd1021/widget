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
 * Inherits from the widget class and adds attributes of vehicles.
 * @author NICK
 *
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Widget_Vehicles extends Widget {

	@NonNull
	private String terrain;
	
	@NonNull
	private String roadSafe;
	
	@NonNull
	private String itemCondition;

	public String getTerrain() {
		return terrain;
	}

	public void setTerrain(String terrain) {
		this.terrain = terrain;
	}

	public String getRoadSafe() {
		return roadSafe;
	}

	public void setRoadSafe(String roadSafe) {
		this.roadSafe = roadSafe;
	}

	public String getCondition() {
		return itemCondition;
	}

	public void setCondition(String condition) {
		this.itemCondition = condition;
	}

	public String getItemCondition() {
		return itemCondition;
	}

	public void setItemCondition(String itemCondition) {
		this.itemCondition = itemCondition;
	}
	
	
}
