package edu.sru.cpsc.webshopping.controller.billing;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.web.bind.annotation.RestController;

import edu.sru.cpsc.webshopping.domain.billing.PaymentDetails;
import edu.sru.cpsc.webshopping.repository.billing.PaymentDetailsRepository;

/**
 * Controller for handling PaymentDetails in database
 */
@RestController
public class PaymentDetailsController {
	@PersistenceContext
	private EntityManager entityManager;
	private PaymentDetailsRepository paymentDetailsRepository;
	
	public PaymentDetailsController(PaymentDetailsRepository paymentDetailsRepository) {
		this.paymentDetailsRepository = paymentDetailsRepository;
	}
	
	/**
	 * Deletes the passed PaymentDetails from the database
	 * @param details details to delete
	 */
	public void deletePaymentDetails(PaymentDetails details) {
		PaymentDetails dbDetails = entityManager.find(PaymentDetails.class, details.getId());
		if (dbDetails != null)
			paymentDetailsRepository.deleteById(dbDetails.getId());
	}
}
