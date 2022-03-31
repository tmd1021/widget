package edu.sru.cpsc.webshopping.repository.billing;

import org.springframework.data.repository.CrudRepository;

import edu.sru.cpsc.webshopping.domain.billing.PaymentDetails;

public interface PaymentDetailsRepository extends CrudRepository<PaymentDetails, Long> {

}
