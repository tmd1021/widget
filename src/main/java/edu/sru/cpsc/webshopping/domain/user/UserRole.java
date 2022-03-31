package edu.sru.cpsc.webshopping.domain.user;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.lang.NonNull;

// This Entity is for holding a representation of what website features a user has access to
// This is used to distinguish from a regular user, from an administrator, where the latter should be able
// to perform actions such as deleting users
@Entity
public class UserRole implements GrantedAuthority {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String authorityName;

	
	@OneToMany(mappedBy="userRole")
	private Set<User> users;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	// The application should not be capable of altering a UserRole authority String, so no mutator method is included
	@Override
	public String getAuthority() {
		return authorityName;
	}
}
