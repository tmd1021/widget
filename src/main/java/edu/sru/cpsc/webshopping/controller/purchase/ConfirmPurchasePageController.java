package edu.sru.cpsc.webshopping.controller.purchase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.sru.cpsc.webshopping.controller.MarketListingDomainController;
import edu.sru.cpsc.webshopping.controller.TransactionController;
import edu.sru.cpsc.webshopping.controller.UserController;
import edu.sru.cpsc.webshopping.controller.billing.CardTypeController;
import edu.sru.cpsc.webshopping.domain.billing.PaymentDetails;
import edu.sru.cpsc.webshopping.domain.billing.PaymentDetails_Form;
import edu.sru.cpsc.webshopping.domain.billing.Paypal_Form;
import edu.sru.cpsc.webshopping.domain.billing.ShippingAddress;
import edu.sru.cpsc.webshopping.domain.market.MarketListing;
import edu.sru.cpsc.webshopping.domain.market.Shipping;
import edu.sru.cpsc.webshopping.domain.market.Transaction;
import edu.sru.cpsc.webshopping.repository.market.ShippingRepository;
import edu.sru.cpsc.webshopping.repository.market.TransactionRepository;

/**
 * Manages functionality for the confirmPurchase page
 * This page is used to verify payment details, and to submit the purchase/
 * create the associated transaction
 */
@Controller
public class ConfirmPurchasePageController {
	private Transaction purchase;
	private MarketListing prevListing;
	private PaymentDetails details;
	private ShippingAddress address;
	private Paypal_Form paypal;
	// SQL Controllers
	private TransactionController transController;
	private MarketListingDomainController marketListingController;
	private UserController userController;
	private CardTypeController cardController;
	
	ConfirmPurchasePageController(MarketListingDomainController marketListingController, 
									UserController userController, TransactionController transController,
									CardTypeController cardController) {
		this.marketListingController = marketListingController;
		this.userController = userController;
		this.transController = transController;
		this.cardController = cardController;
	}
	
	/**
	 * Initializes the confirmPurchase page
	 * @param address the ShippingAddress created from the previous page
	 * @param prevListing the MarketListing that the user wishes to purchase from
	 * @param purchaseOrder in progress Transaction to be saved if the user confirms purchase
	 * @param model Page model from previous page
	 * @return String for the confirmPurchase page
	 */
	@RequestMapping("/confirmPurchase")
	public String openConfirmPurchasePage(ShippingAddress address, MarketListing prevListing, Transaction purchaseOrder, Model model) {
		// Setup purchase with total price and profit
		purchase = purchaseOrder;
		BigDecimal salesTaxPercentage = address.getState().getSalesTaxRate()
				.divide(new BigDecimal(100));
		BigDecimal afterSalesTax = purchase.getTotalPriceBeforeTaxes()
				.add(salesTaxPercentage.multiply(purchase.getTotalPriceBeforeTaxes())).setScale(2, RoundingMode.UP);
		purchase.setTotalPriceAfterTaxes(afterSalesTax);
		BigDecimal finalSellerProfit = afterSalesTax.subtract(
				afterSalesTax.multiply(Transaction.WEBSITE_CUT_PERCENTAGE));
		purchase.setSellerProfit(finalSellerProfit);
		
		// Prepare a form for verifying the user's payment details
		details = new PaymentDetails();
		this.prevListing = prevListing;
		this.address = address;
		paypal = new Paypal_Form();
		model.addAttribute("purchase", purchase);
		model.addAttribute("marketListing", prevListing);
		model.addAttribute("widget", prevListing.getWidgetSold());
		model.addAttribute("paymentDetails", details);
		model.addAttribute("cardTypes", cardController.getAllCardTypes());
		model.addAttribute("paypal", paypal);
		model.addAttribute("user", userController.getCurrently_Logged_In());
		return "confirmPurchase";
	}
	
	/**
	 * Attempts to confirm the purchase
	 * If successful, the associated Transaction is saved to the database,
	 * and the number of available items for the MarketListing is decreased by 
	 * the number of purchased items
	 * The variables are passed by dependency injection
	 * @param paymentDetails the PaymentDetails from the form
	 * @param result BindingResult associated with the form
	 * @param model the page model
	 * @exception IllegalStateException if the user is not logged in
	 * @return a redirection to index, if purchase is successful, or the same page, if verification fails
	 */
	@RequestMapping(value = "/confirmPurchase/submitPurchase", method = RequestMethod.POST, params = "submit")
	public String submitPurchase(@Validated @ModelAttribute("paymentDetails") PaymentDetails_Form paymentDetails, BindingResult result, Model model) {
		PaymentDetails currDetails = new PaymentDetails();
		currDetails.buildFromForm(paymentDetails);
		if (this.userController.getCurrently_Logged_In() == null) {
			throw new IllegalStateException("Cannot purchase an item when not logged in.");
		}
		// Test that payment details are valid
		if (this.userController.verifyPaymentDetails(currDetails) && !paymentDetailsInvalid(paymentDetails)) {
			// Update market listing to reflect purchase
			marketListingController.marketListingPurchaseUpdate(prevListing, purchase.getQtyBought());
			// Creates an unfinished shipping label, to be filled out later by the seller
			// Preparing the transaction for posting to the database
			Shipping shipping = new Shipping();
			shipping.setTransaction(purchase);
			shipping.setAddress(address);
			purchase.setShippingEntry(shipping);
			transController.addTransaction(purchase);
			return "redirect:/index";
		}
		// Transaction failed - post error
		else {
			details = new PaymentDetails();
			// Build credit card error message
			for (FieldError item : result.getFieldErrors()) {
				model.addAttribute(item.getField() + "Err", item.getDefaultMessage());		
			}
			if (paymentDetails.getExpirationDate() != null && paymentDetailsExpired(paymentDetails)) {
				model.addAttribute("ccExpired", "The Credit Card has expired.");
			}
			model.addAttribute("purchase", purchase);
			model.addAttribute("marketListing", prevListing);
			model.addAttribute("widget", prevListing.getWidgetSold());
			model.addAttribute("paymentDetails", details);
			model.addAttribute("errMessage", "Payment Details Invalid");
			model.addAttribute("paypal", paypal);
			model.addAttribute("cardTypes", cardController.getAllCardTypes());
			model.addAttribute("user", userController.getCurrently_Logged_In());
			return "confirmPurchase";
		}

	}
	
	/**
	 * Cancels the purchase
	 * The user is returned to the viewMarketListing page they attempted to purchase from
	 * No changes are made
	 * @param paymentDetails passed by the form
	 * @return a redirection to the viewMarketListing page associated with the listing they attempted to purchase from
	 */
	@RequestMapping(value = "/confirmPurchase/submitPurchase", method = RequestMethod.POST, params = "cancel")
	public String cancelPurchase(@Validated @ModelAttribute("paymentDetails") PaymentDetails paymentDetails) {
		return "redirect:/viewMarketListing/" + prevListing.getId();
	}
	
	/**
	 * Attempts to submit the purchase via Paypal
	 * @param paypal Validated Paypal form
	 * @param result BindingResult associated with Paypal form
	 * @param model page model
	 * @return Returns to the index if successful, reloads the page if failed
	 */
	@RequestMapping(value = "/confirmPurchase/submitPurchasePaypal", method = RequestMethod.POST, params = "submit")
	public String submitPurchasePaypal(@Validated @ModelAttribute("paypal") Paypal_Form paypal, BindingResult result, Model model) {
		if (this.userController.getCurrently_Logged_In() == null) {
			throw new IllegalStateException("Cannot purchase an item when not logged in.");
		}
		else if (result.hasErrors()) { // Transaction failed - show errors
			details = new PaymentDetails();
			// Build credit card error message
			for (FieldError item : result.getFieldErrors()) {
				model.addAttribute(item.getField() + "Err", item.getDefaultMessage());		
			}
			model.addAttribute("purchase", purchase);
			model.addAttribute("marketListing", prevListing);
			model.addAttribute("widget", prevListing.getWidgetSold());
			model.addAttribute("paymentDetails", details);
			model.addAttribute("errMessage", "Paypal Details Invalid");
			model.addAttribute("paypal", paypal);
			model.addAttribute("cardTypes", cardController.getAllCardTypes());
			model.addAttribute("user", userController.getCurrently_Logged_In());
			return "confirmPurchase";
		}
		// Test that payment details are valid
		else {
			// Update market listing to reflect purchase
			marketListingController.marketListingPurchaseUpdate(prevListing, purchase.getQtyBought());
			// Creates an unfinished shipping label, to be filled out later by the seller
			// Preparing the transaction for posting to the database
			Shipping shipping = new Shipping();
			shipping.setTransaction(purchase);
			shipping.setAddress(address);
			purchase.setShippingEntry(shipping);
			transController.addTransaction(purchase);
			return "redirect:/index";
		}
	}
	
	/**
	 * Attempts to cancel the purchase
	 * @param paypal Validated Paypal form
	 * @return the MarketListing that the user attempted to purchase from
	 */
	@RequestMapping(value = "/confirmPurchase/submitPurchasePaypal", method = RequestMethod.POST, params = "cancel")
	public String cancelPurchasePaypal(@Validated @ModelAttribute("paypal") Paypal_Form paypal, BindingResult result, Model model) {
		System.out.println("cancel purchase paypal");
		return "redirect:/viewMarketListing/" + prevListing.getId();
	}
	
	/**
	 * Attempts to cancel the purchase
	 * @param paypal Validated Paypal form
	 * @return the MarketListing that the user attempted to purchase from
	 */
	@RequestMapping(value = "/cancel-purchase", method = RequestMethod.POST)
	public String cancelPurchase() {
		System.out.println("cancel purchase paypal");
		return "redirect:/viewMarketListing/" + prevListing.getId();
	}
	
	/**
	 * Returns true if the PaymentDetails fails constraints that are not represented by
	 * the Spring Validation annotations
	 * These constraints are: expiration date must be before the current date
	 * @param form A completed PaymentDetails_Form
	 */
	public boolean paymentDetailsInvalid(PaymentDetails_Form form) {
		return paymentDetailsExpired(form);
	}
	
	/**
	 * Returns true if the passed PaymentDetails are expired
	 * @param form The PaymentDetails_Form to check is expired
	 * @return true if the form is expired, false otherwise, and false if the expiration date String is null or empty
	 */
	public boolean paymentDetailsExpired(PaymentDetails_Form form) {
		if (form.getExpirationDate() == null || form.getExpirationDate().length() == 0)
			return false;
		// Check if current date is on or past the expiration date
		System.out.println("before parse");
		System.out.println(form.getExpirationDate());
		LocalDate expirDate = LocalDate.parse(form.getExpirationDate());
		System.out.println("after parse");
		if (expirDate.compareTo(LocalDate.now()) < 0)
			return true;
		
		// No errors found
		return false;
	}
}
