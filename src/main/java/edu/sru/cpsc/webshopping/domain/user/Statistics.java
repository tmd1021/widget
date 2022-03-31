package edu.sru.cpsc.webshopping.domain.user;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Statistics {
	public Statistics() {
		
	}
	
	Category category;
	public Statistics(Category cat, float value) {
		this.category = cat;
		this.value = value;
		this.setDate(LocalDateTime.now());
		this.setHour();
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private float value;
	private LocalDateTime date;
	private int hour;
	public enum Category{
		SALE,
		ACCOUNTCREATION,
		USERLOGIN,
		ACCOUNTDELETED,
		MESSAGESENT,
		WIDGETCREATED;
		
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	public int getHour() {
		return hour;
	}
	public void setHour() {
		this.hour = date.getHour();
	}




}

