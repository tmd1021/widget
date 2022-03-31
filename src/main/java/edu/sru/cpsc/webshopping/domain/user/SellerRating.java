package edu.sru.cpsc.webshopping.domain.user;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.lang.NonNull;

// This Entity is used to describe how good of a seller a particular user is
// RatingName will be a one or two word description of the user's rating
@Entity
public class SellerRating {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NonNull
	private String RatingName;
	
	@OneToMany(mappedBy="sellerRating")
	private Set<User> users;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRatingName() {
		return RatingName;
	}

	public void setRatingName(String ratingName) {
		RatingName = ratingName;
	}
}
