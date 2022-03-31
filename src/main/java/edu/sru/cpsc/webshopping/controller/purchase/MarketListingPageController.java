package edu.sru.cpsc.webshopping.controller.purchase;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.sru.cpsc.webshopping.controller.EmailController;
import edu.sru.cpsc.webshopping.controller.MarketListingDomainController;
import edu.sru.cpsc.webshopping.controller.MessageDomainController;
import edu.sru.cpsc.webshopping.controller.TransactionController;
import edu.sru.cpsc.webshopping.controller.UserController;
import edu.sru.cpsc.webshopping.controller.WidgetController;
import edu.sru.cpsc.webshopping.domain.market.MarketListing;
import edu.sru.cpsc.webshopping.domain.market.Transaction;
import edu.sru.cpsc.webshopping.domain.user.Message;
import edu.sru.cpsc.webshopping.domain.user.User;
import edu.sru.cpsc.webshopping.domain.widgets.Appliance_Dryers;
import edu.sru.cpsc.webshopping.domain.widgets.Appliance_Microwave;
import edu.sru.cpsc.webshopping.domain.widgets.Appliance_Refrigerator;
import edu.sru.cpsc.webshopping.domain.widgets.Appliance_Washers;
import edu.sru.cpsc.webshopping.domain.widgets.Electronics_Computers;
import edu.sru.cpsc.webshopping.domain.widgets.Electronics_VideoGames;
import edu.sru.cpsc.webshopping.domain.widgets.LawnCare_LawnMower;
import edu.sru.cpsc.webshopping.domain.widgets.Vehicle_Car;
import edu.sru.cpsc.webshopping.domain.widgets.Widget;
import edu.sru.cpsc.webshopping.domain.widgets.Widget_Appliance;
import edu.sru.cpsc.webshopping.domain.widgets.Widget_Electronics;
import edu.sru.cpsc.webshopping.domain.widgets.Widget_LawnCare;
import edu.sru.cpsc.webshopping.domain.widgets.Widget_Vehicles;
import edu.sru.cpsc.webshopping.repository.market.ShippingRepository;
import edu.sru.cpsc.webshopping.repository.market.TransactionRepository;

/**
 * Manages the functionality of the viewMarketListing page
 * This page is used for viewing and interacting with market listings
 */
@Controller
public class MarketListingPageController {
	MarketListingDomainController marketListingController;
	PurchaseShippingAddressPageController shippingPage;
	MarketListing heldListing;
	UserController userController;
	MessageDomainController msgcontrol;
	EmailController emailController;
	// Repositories passed to ConfirmPurchasePage
	TransactionController transController;
	WidgetController widgetController;
	private String page;
	private Widget tempWidget;
	public MarketListingPageController(MarketListingDomainController marketListingController,
			TransactionController transController, UserController userController, PurchaseShippingAddressPageController shippingPage,MessageDomainController msgcontrol,EmailController emailController, WidgetController widgetController) {
		this.marketListingController = marketListingController;
		this.transController = transController;
		this.userController = userController;
		this.shippingPage = shippingPage;
		this.msgcontrol = msgcontrol;
		this.emailController = emailController;
		this.widgetController = widgetController;
	}
	
	/**
	 * Opens the viewMarketListingPage
	 * @param marketListingId the id of the marketListing to view
	 * @param model The page model - passed by dependency injection
	 * @return the viewMarketListing page string
	 */
	@RequestMapping({"/viewMarketListing/{marketListingId}"})
	public String viewMarketListingPage(@PathVariable("marketListingId") long marketListingId, Model model) {
		heldListing = marketListingController.getMarketListing(marketListingId);
		// TODO: Open an error page
		// TODO: Set user status by reading from a User server
		if (heldListing.isDeleted()) {
			throw new IllegalArgumentException("Attempted to access an invalid Market Listing");
		}
		MarketListing currentListing = marketListingController.getMarketListing(marketListingId);
		tempWidget = currentListing.getWidgetSold();
		System.out.print(tempWidget.getId());
		System.out.println(tempWidget.getCategory());
		System.out.println(tempWidget.getSubCategory());
		if(tempWidget.getCategory().contentEquals("appliance"))
		{
			Widget_Appliance appliance = widgetController.getAppliance(tempWidget.getId());
			model.addAttribute("appliance", appliance);
			if(tempWidget.getSubCategory().contentEquals("washer"))
			{
				Appliance_Washers washer = widgetController.getWasher(tempWidget.getId());
				model.addAttribute("washer", washer);
			}
			if(tempWidget.getSubCategory().contentEquals("dryer"))
			{
				Appliance_Dryers dryer = widgetController.getDryer(tempWidget.getId());
				model.addAttribute("dryer", dryer);
			}
			if(tempWidget.getSubCategory().contentEquals("fridge"))
			{
				Appliance_Refrigerator fridge = widgetController.getRefrigerator(tempWidget.getId());
				model.addAttribute("fridge", fridge);
			}
			if(tempWidget.getSubCategory().contentEquals("microwave"))
			{
				Appliance_Microwave microwave = widgetController.getMicrowave(tempWidget.getId());
				model.addAttribute("microwave", microwave);
			}
		}
		if(tempWidget.getCategory().contentEquals("electronic"))
		{
			Widget_Electronics electronic = widgetController.getElectronic(tempWidget.getId());
			System.out.println(electronic.getName());
			model.addAttribute("electronic", electronic);
			if(tempWidget.getSubCategory().contentEquals("videoGame"))
			{
				Electronics_VideoGames videoGame = widgetController.getVideoGame(tempWidget.getId());
				model.addAttribute("videoGame", videoGame);
			}
			if(tempWidget.getSubCategory().contentEquals("computer"))
			{
				Electronics_Computers computer = widgetController.getComputer(tempWidget.getId());
				System.out.println(computer.getName());
				model.addAttribute("computer", computer);
			}
		}
		if(tempWidget.getCategory().contentEquals("vehicle"))
		{
			Widget_Vehicles vehicle = widgetController.getVehicle(tempWidget.getId());
			model.addAttribute("vehicle", vehicle);
			if(tempWidget.getSubCategory().contentEquals("car"))
			{
				Vehicle_Car car = widgetController.getCar(tempWidget.getId());
				model.addAttribute("car", car);
			}
		}
		if(tempWidget.getCategory().contentEquals("lawnCare"))
		{
			Widget_LawnCare lawnCare = widgetController.getLawnCare(tempWidget.getId());
			model.addAttribute("lawnCare", lawnCare);
			if(tempWidget.getSubCategory().contentEquals("lawnMower"))
			{
				LawnCare_LawnMower mower = widgetController.getMower(tempWidget.getId());
				model.addAttribute("mower", mower);
			}
		}
		model.addAttribute("currListing", currentListing);
		model.addAttribute("widget", currentListing.getWidgetSold());
		
		// Check if User is the owner of the market listing
		User user = userController.getCurrently_Logged_In();
		// If user is the seller or an admin, give them access to modify the listing
		if (user.getId() == currentListing.getSeller().getId() || user.getRole().equals("ROLE_ADMIN")) 
			model.addAttribute("isBuyer", false);
		else {
			model.addAttribute("isBuyer", true);
			Transaction newTrans = new Transaction();
			model.addAttribute("newTransaction", newTrans);
		}
		// Check if the user is able to purchase widgets
		if (user.getPaymentDetails() == null)
			model.addAttribute("canBuy", false);
		else
			model.addAttribute("canBuy", true);
		
		return "viewMarketListing";
	}
	
	/**
	 * Attempts to purchase from a MarketListing
	 * Opens the next page in the purchasing pipeline - submitting the
	 * shipping address
	 * @param newTransaction a partially filled out Transaction holding the number of items to buy
	 * @param model the page model - passed by dependency injection
	 * @return the confirmShippingPage
	 */
	@PostMapping({"/viewMarketListing/attemptPurchase"})
	public String attemptPurchase(@Validated Transaction newTransaction, BindingResult result, Model model) {
		// Initial validation - validation must also be done when updating the database
		if (newTransaction.getQtyBought() > heldListing.getQtyAvailable()) {
			System.out.println("failure 1");
			result.addError(new FieldError("newTransaction", "qtyBought", "Cannot buy more items than the Market Listing quantity."));
		}
		// Errors found - refresh page
		if (result.hasErrors()) {
			System.out.println("err");
			model.addAttribute("currListing", heldListing);
			model.addAttribute("widget", heldListing.getWidgetSold());
			model.addAttribute("isBuyer", true);
			model.addAttribute("newTransaction", newTransaction);
			model.addAttribute("canBuy", true);
			return "viewMarketListing";
		}
		Transaction purchaseAttempt = new Transaction();
		purchaseAttempt.setBuyer(userController.getCurrently_Logged_In());
		purchaseAttempt.setSeller(heldListing.getSeller());
		purchaseAttempt.setMarketListing(heldListing);
		purchaseAttempt.setQtyBought(newTransaction.getQtyBought());
		purchaseAttempt.setTotalPriceBeforeTaxes(heldListing.getPricePerItem().multiply(BigDecimal.valueOf(newTransaction.getQtyBought())));
		// Add shipping entry if user confirms purchase on next page
		purchaseAttempt.setShippingEntry(null);
		return shippingPage.openConfirmShippingPage(heldListing, purchaseAttempt, model);
	}
	
	/**
	 * Adds the current item to the User's wishlist
	 * @return the current viewMarketListing page
	 */
	@PostMapping({"/viewMarketListing/wishlistItem"})
	public String wishlistItem() {
		userController.addToWishlist(heldListing.getWidgetSold());
		return "redirect:/viewMarketListing/" + heldListing.getId();
	}
	
	/**
	 * Return to market listings
	 * @return the homePage
	 */
	@PostMapping({"/viewMarketListing/returnToListings"})
	public String returnToListings() {
		return "redirect:/homePage";
	}
	
	/** 
	 * Administrative/seller functionality
	 * Edits the number of items, or its price
	 * @param marketListing holding the new MarketListing values
	 * @return returns to the index
	 */
	@PostMapping({"/viewMarketListing/editListing"}) 
	public String editListing(@Validated MarketListing marketListing) {
		marketListing.setSeller(heldListing.getSeller());
		marketListing.setTransactions(heldListing.getTransactions());
		marketListing.setWidgetSold(heldListing.getWidgetSold());
		marketListingController.editMarketListing(marketListing);
		return "redirect:/index";
	}
	

	/**
	 * Disables visibility of the MarketListing
	 * @return returns to the index
	 */
	@PostMapping({"/viewMarketListing/deleteListing"}) 
	public String deleteListing() {
		heldListing.setDeleted(true);
		marketListingController.editMarketListing(heldListing);
		// temporary redirection - change to return to market listings search page later on
		return "redirect:/index";
	}
	@RequestMapping({"/viewMarketListing/openMessage"})
	public String openMessagePane(Model model) {
		setPage("openMessage");
		User user = userController.getCurrently_Logged_In();
		// If user is the seller or an admin, give them access to modify the listing
		if (user.getId() == heldListing.getSeller().getId() || user.getRole().equals("ROLE_ADMIN")) 
			model.addAttribute("isBuyer", false);
		else {
			model.addAttribute("isBuyer", true);
			Transaction newTrans = new Transaction();
			model.addAttribute("newTransaction", newTrans);
		}
		// Check if the user is able to purchase widgets
		if (user.getPaymentDetails() == null)
			model.addAttribute("canBuy", false);
		else
			model.addAttribute("canBuy", true);
		String seller = heldListing.getSeller().getUsername();
		model.addAttribute("seller",seller);
		model.addAttribute("currListing", heldListing);
		model.addAttribute("widget", heldListing.getWidgetSold());
		model.addAttribute("page", page);	
		return "viewMarketListing";
	}
	@RequestMapping({"/viewMarketListing/closeMessage"})
	public String closeMessagePane(Model model) {
		setPage("fail");
		User user = userController.getCurrently_Logged_In();
		// If user is the seller or an admin, give them access to modify the listing
		if (user.getId() == heldListing.getSeller().getId() || user.getRole().equals("ROLE_ADMIN")) 
			model.addAttribute("isBuyer", false);
		else {
			model.addAttribute("isBuyer", true);
			Transaction newTrans = new Transaction();
			model.addAttribute("newTransaction", newTrans);
		}
		// Check if the user is able to purchase widgets
		if (user.getPaymentDetails() == null)
			model.addAttribute("canBuy", false);
		else
			model.addAttribute("canBuy", true);
		String seller = heldListing.getSeller().getUsername();
		model.addAttribute("seller",seller);
		model.addAttribute("currListing", heldListing);
		model.addAttribute("widget", heldListing.getWidgetSold());
		model.addAttribute("page", page);	
		return "viewMarketListing";
	}
	   @RequestMapping({"/marketSendMail"})
	   public String marketSendMessage(@RequestParam("message") String content2,@RequestParam("subject") String subject2,Model model) {

			
			User user = userController.getCurrently_Logged_In();
			User receiver = heldListing.getSeller();
				setPage("Success");
				model.addAttribute("page",page);
				model.addAttribute("view",user.getViewMessage());
			   
			    if(subject2.length() == 0 || content2.length() == 0)
			    {
			    	setPage("fail");
			    	model.addAttribute("page",page);
			    	if (user.getId() == heldListing.getSeller().getId() || user.getRole().equals("ROLE_ADMIN")) 
						model.addAttribute("isBuyer", false);
					else {
						model.addAttribute("isBuyer", true);
						Transaction newTrans = new Transaction();
						model.addAttribute("newTransaction", newTrans);
					}
					// Check if the user is able to purchase widgets
					if (user.getPaymentDetails() == null)
						model.addAttribute("canBuy", false);
					else
						model.addAttribute("canBuy", true);
					String seller = heldListing.getSeller().getUsername();
					model.addAttribute("seller",seller);
					model.addAttribute("currListing", heldListing);
					model.addAttribute("widget", heldListing.getWidgetSold());
					model.addAttribute("page", page);	
					return "viewMarketListing";
			    }
			
				Message message = new Message();
				message.setOwner(user);
				message.setSender(user.getUsername());
				message.setContent(content2);
				message.setSubject(subject2);
				message.setMsgDate();
				message.setReceiverName(receiver.getUsername());
				message.setReceiver(receiver);
				msgcontrol.addMessage(message);
				emailController.messageEmail(receiver,user,message);
				if (user.getId() == heldListing.getSeller().getId() || user.getRole().equals("ROLE_ADMIN")) 
					model.addAttribute("isBuyer", false);
				else {
					model.addAttribute("isBuyer", true);
					Transaction newTrans = new Transaction();
					model.addAttribute("newTransaction", newTrans);
				}
				// Check if the user is able to purchase widgets
				if (user.getPaymentDetails() == null)
					model.addAttribute("canBuy", false);
				else
					model.addAttribute("canBuy", true);
				String seller = heldListing.getSeller().getUsername();
				model.addAttribute("seller",seller);
				model.addAttribute("currListing", heldListing);
				model.addAttribute("widget", heldListing.getWidgetSold());
				model.addAttribute("page", page);	
				return "viewMarketListing";
	   }
	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}
}
