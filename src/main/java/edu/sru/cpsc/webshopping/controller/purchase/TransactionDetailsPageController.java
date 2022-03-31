package edu.sru.cpsc.webshopping.controller.purchase;
import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.sru.cpsc.webshopping.controller.ShippingDomainController;
import edu.sru.cpsc.webshopping.controller.TransactionController;
import edu.sru.cpsc.webshopping.controller.UserController;
import edu.sru.cpsc.webshopping.domain.market.Shipping;
import edu.sru.cpsc.webshopping.domain.market.Transaction;

/**
 * Manages the transactionDetails page
 * This is used for viewing information about a particular transaction, and for allowing shipping information to be added by
 * the seller
 */
@Controller
public class TransactionDetailsPageController {
	private UserController userController;
	private TransactionController transController;
	private ShippingDomainController shippingController;
	private Shipping origEntry;
	private Transaction trans;
	
	TransactionDetailsPageController(UserController userController, TransactionController transController,
			ShippingDomainController shippingController) {
		this.userController = userController;
		this.transController = transController;
		this.shippingController = shippingController;
	}
	
	/**
	 * Initializes the viewTransaction page
	 * @param transId the Id of the Transaction to view
	 * @param model the View model, added by dependency injection
	 * @return the transactionDetails page string
	 */
	@RequestMapping({"/viewTransactionDetails/{transId}"})
	public String purchaseDetails(@PathVariable("transId") long transId, Model model) {
		trans = transController.getTransaction(transId);
		origEntry = shippingController.getShippingEntry(trans.getShippingEntry().getId());
		model.addAttribute("trans", trans);
		model.addAttribute("user", userController.getCurrently_Logged_In());
		model.addAttribute("shipping", new Shipping());
		model.addAttribute("currentDate", LocalDate.now());
		model.addAttribute("canDelete", canDeleteTransaction());
		return "transactionDetails";
	}
	
	/**
	 * Returns to homePage
	 * @return redirection string to homepPage
	 */
	@RequestMapping({"viewTransactionDetails/returnToHome"})
	public String returnToHome() {
		return "redirect:/homePage";
	}
	
	/**
	 * Submits updates to shipping information
	 * @return redirection string to homePage
	 */
	@RequestMapping({"/viewTransactionDetails/submitShippingUpdate"})
	public String submitShippingUpdate(@Validated Shipping form, BindingResult result, Model model) {
		// Form error or server-side validation error found
		if (result.hasErrors() || ShippingConstraintsInvalid(form)) {
			model.addAttribute("trans", trans);
			model.addAttribute("user", userController.getCurrently_Logged_In());
			model.addAttribute("shipping", new Shipping());
			// Produce error messages
			System.out.println(form.getCarrier().length());
			if (result.hasErrors()  || ShippingConstraintsInvalid(form)) {
				model.addAttribute("errMessage", "Form must be fully filled out before submission.");
				if (form.getCarrier() == null || form.getCarrier().length() == 0) 
					model.addAttribute("carrierErr", "Carrier field cannot be empty.");
				if (form.getShippingDate() == null)
					model.addAttribute("shippingDateErr", "Shipping Date cannot be empty.");
				if (form.getArrivalDate() == null)
					model.addAttribute("arrivalDateErr", "Arrival Date cannot be empty.");
			}
			else if (form.getArrivalDate().compareTo(form.getShippingDate()) < 0) {
				model.addAttribute("errMessage", "Arrival Date must be after the Shipping Date.");
			}
			return "transactionDetails";
		}
		// Add items to form that the seller cannot modify
		form.setId(origEntry.getId());
		form.setAddress(origEntry.getAddress());
		shippingController.editShippingEntry(form);
		return "redirect:/homePage";
	}
	
	/**
	 * Tests if a form is invalid, using constraints that cannot be performed using Validation annotations
	 * @param form The Shipping form to evaluate
	 * @return true if the form is invalid, false if it is valid
	 */
	public boolean ShippingConstraintsInvalid(Shipping form) {
		return (form.getArrivalDate().compareTo(form.getShippingDate()) < 0)
				 || form.getArrivalDate() == null || form.getShippingDate() == null
				 || form.getCarrier() == null || form.getCarrier().length() == 0;
	}
	
	/**
	 * Tests if the loaded Transaction can be cancelled
	 * @return true if the loaded Transaction can be cancelled, false otherwise
	 */
	public boolean canDeleteTransaction() {
		if (trans.getShippingEntry().hasShipped())
			return false;
		return true;
	}
	
	/**
	 * Attempts to delete the associated Transaction
	 * @return returns true if the Transaction is deleted, false otherwise
	 */
	@RequestMapping({"/viewTransactionDetails/deleteTransaction"})
	public String deleteTransaction(Model model) {
		if (canDeleteTransaction()) {
			boolean cancelSuccess = transController.cancelTransaction(trans);
			if (cancelSuccess)
				return "redirect:/homePage";
		}
		// Failed, so we reload the page
		model.addAttribute("errMessage", "Failed to cancel transaction.");
		model.addAttribute("trans", trans);
		model.addAttribute("user", userController.getCurrently_Logged_In());
		model.addAttribute("shipping", new Shipping());
		model.addAttribute("currentDate", LocalDate.now());
		model.addAttribute("canDelete", canDeleteTransaction());
		return "transactionDetails";
	}
}
