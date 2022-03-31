package edu.sru.cpsc.webshopping.repository.user;

import org.springframework.data.repository.CrudRepository;
import edu.sru.cpsc.webshopping.domain.user.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	User findByUsername(String username);
	User findByEmail(String email);
	
}
	