package edu.sru.cpsc.webshopping.controller.billing;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.web.bind.annotation.RestController;

import edu.sru.cpsc.webshopping.domain.billing.DirectDepositDetails;
import edu.sru.cpsc.webshopping.domain.billing.PaymentDetails;
import edu.sru.cpsc.webshopping.repository.billing.DirectDepositDetailsRepository;


/**
 * For managing DirectDeposit items in the database
 */
@RestController
public class DirectDepositController {
	@PersistenceContext
	private EntityManager entityManager;
	private DirectDepositDetailsRepository directDepositDetailsRepository;
	
	public DirectDepositController(DirectDepositDetailsRepository directDepositDetailsRepository) {
		this.directDepositDetailsRepository = directDepositDetailsRepository;
	}
	
	/**
	 * Deletes the passed DirectDepositDetails from the database
	 * @param details details to delete
	 */
	public void deleteDirectDepositDetails(DirectDepositDetails details) {
		DirectDepositDetails dbDetails = entityManager.find(DirectDepositDetails.class, details.getId());
		if (dbDetails != null)
			directDepositDetailsRepository.deleteById(dbDetails.getId());
	}
}
