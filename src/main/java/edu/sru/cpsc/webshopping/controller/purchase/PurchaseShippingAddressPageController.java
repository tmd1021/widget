package edu.sru.cpsc.webshopping.controller.purchase;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.sru.cpsc.webshopping.controller.TransactionController;
import edu.sru.cpsc.webshopping.controller.billing.StateDetailsController;
import edu.sru.cpsc.webshopping.domain.billing.PaymentDetails;
import edu.sru.cpsc.webshopping.domain.billing.ShippingAddress;
import edu.sru.cpsc.webshopping.domain.billing.ShippingAddress_Form;
import edu.sru.cpsc.webshopping.domain.billing.StateDetails;
import edu.sru.cpsc.webshopping.domain.market.MarketListing;
import edu.sru.cpsc.webshopping.domain.market.Shipping;
import edu.sru.cpsc.webshopping.domain.market.Transaction;

/**
 * Manages the functionality of the confirmShipping page
 * This is used to get the shipping address of the user when they attempt
 * to make a purchase
 */
@Controller
public class PurchaseShippingAddressPageController {
	private StateDetailsController stateDetailsController;
	private ShippingAddress_Form shippingAddress;
	private MarketListing prevListing;
	private Transaction purchaseOrder;
	private Iterable<StateDetails> states;
	private ConfirmPurchasePageController purchasePageController;
	
	public PurchaseShippingAddressPageController(StateDetailsController stateDetailsController,
												ConfirmPurchasePageController purchasePageController) {
		this.stateDetailsController = stateDetailsController;
		this.purchasePageController = purchasePageController;
	}
	

	/**
	 * Opens the confirmShipping page
	 * @param prevListing the MarketListing the User is attempting to buy from
	 * @param purchaseOrder the partially filled out transaction from the viewMarketListing page
	 * @param model the page Model from the viewMarketListings page
	 * @param transController a TransactionController
	 * @return the confirmShippingAddressPage page
	 */
	@RequestMapping("/confirmShipping")
	public String openConfirmShippingPage(MarketListing prevListing, Transaction purchaseOrder, Model model) {
		System.out.println("shipping test");
		this.shippingAddress = new ShippingAddress_Form();
		this.prevListing = prevListing;
		this.purchaseOrder = purchaseOrder;
		model.addAttribute("shippingAddress", shippingAddress);
		states = stateDetailsController.getAllStates();
		model.addAttribute("states", states);
		return "confirmShippingAddressPage";
	}
	
	/**
	 * This function saves the ShippingAddress, and then opens the page for
	 * verifying PaymentDetails for the purchase
	 * @param address address from the page form
	 * @param stateId the state name of the state the user is attempting to ship to
	 * @param model the page model, passed by dependency injection
	 * @return the confirmPurchasePage
	 */
	@RequestMapping(value = "/confirmShipping/submitAddress", method = RequestMethod.POST, params = "submit")
	public String submitAddress(
			@Validated @ModelAttribute("shippingAddress") ShippingAddress_Form address,
			BindingResult result,
			@RequestParam("stateId") String stateId,
			Model model) {
		// If there are errors, then refresh the page
		if (result.hasErrors()) {
			model.addAttribute("shippingAddress", shippingAddress);
			model.addAttribute("states", states);	
			// Add errors to model
			for (FieldError error : result.getFieldErrors()) {
				model.addAttribute(error.getField() + "Err", error.getDefaultMessage());
			}
			return "confirmShippingAddressPage";
		}
		System.out.println(stateId);
		StateDetails selectedState = stateDetailsController.getState(stateId);
		address.setState(selectedState);
		System.out.println(address.getState().getStateName());
		ShippingAddress validatedAddress = new ShippingAddress();
		validatedAddress.buildFromForm(address);
		return this.purchasePageController.openConfirmPurchasePage(validatedAddress, prevListing, purchaseOrder, model);
	}
	
	/**
	 * Cancels the purchase, returns user to the viewMarketListing page
	 * that they came from
	 * @param address address from the page form
	 * @param stateId the state name of the state the user has selected on the form
	 * @return viewMarketListing page
	 */
	@RequestMapping(value = "/confirmShipping/submitAddress", method = RequestMethod.POST, params = "cancel")
	public String cancelAddress(
			@Validated @ModelAttribute("shippingAddress") ShippingAddress_Form address,
			BindingResult result,
			@RequestParam("stateId") String statId,
			Model model) {
		return "redirect:/viewMarketListing/" + prevListing.getId();
	}
}
