package edu.sru.cpsc.webshopping.repository.market;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import edu.sru.cpsc.webshopping.domain.market.Transaction;
import edu.sru.cpsc.webshopping.domain.user.User;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
	
	Iterable<Transaction> findByBuyer(User user);
	
	Iterable<Transaction> findBySeller(User user);
}
