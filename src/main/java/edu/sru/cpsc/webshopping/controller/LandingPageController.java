package edu.sru.cpsc.webshopping.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import edu.sru.cpsc.webshopping.domain.market.MarketListing;
import edu.sru.cpsc.webshopping.domain.market.Transaction;
import edu.sru.cpsc.webshopping.domain.user.User;
import edu.sru.cpsc.webshopping.repository.user.UserRepository;
import edu.sru.cpsc.webshopping.repository.widgets.WidgetRepository;
import edu.sru.cpsc.webshopping.repository.market.MarketListingRepository;
import edu.sru.cpsc.webshopping.domain.widgets.Widget;


/**
 * Controller for Home page and searching. Interacts with the widget database and its inheriting classes.
 * @author NICK
 *
 */
@Controller
public class LandingPageController {

	UserRepository userRepository;
	WidgetRepository widgetRepository;
	MarketListingRepository marketListingRepos;
	WidgetController widgetController;
	MarketListingDomainController marketController;
	private String searchText;
	private TransactionController transController;
	private UserController userController;


	LandingPageController(UserRepository userRepository, WidgetRepository widgetRepository, MarketListingRepository marketRepo, WidgetController widgetController, MarketListingDomainController marketController,
			TransactionController transController, UserController userController)
	{
		this.userRepository = userRepository;
		this.widgetRepository = widgetRepository;
		this.marketListingRepos = marketRepo;
		this.widgetController = widgetController;
		this.marketController = marketController;
		this.transController = transController;
		this.userController = userController;
		searchText = "";
	}

	@RequestMapping("homePage")
	public String homePage(Model widgetModel, Model listingModel, String tempSearch){
		User user = userController.getCurrently_Logged_In();
		if(user.getRole().equals("ROLE_ADMIN")|| user.getRole().equals("ROLE_QUALITYCONTROLLER") || 
				user.getRole().equals("ROLE_CUSTOMERSERVICE") || user.getRole().equals("ROLE_SALES") || user.getRole().equals("ROLE_HIRINGMANAGER")) {
			return "redirect:employee";
		}
		widgetModel.addAttribute("widgets", widgetRepository.findAll());
		listingModel.addAttribute("listings", marketListingRepos.findAll());
		Iterable<Transaction> purchases = transController.getUserPurchases(userController.getCurrently_Logged_In());
		listingModel.addAttribute("purchases", purchases);
		Iterable<Transaction> soldTrans = transController.getUserSoldItems(userController.getCurrently_Logged_In());
		listingModel.addAttribute("soldTrans", soldTrans);
		return "homePage";
	}

	@RequestMapping("search")
	public String search(@RequestParam("searchString") String searchString, @RequestParam("operator") String operator, @RequestParam("price") String price, @RequestParam("category") String category , @RequestParam("subCategory")String subCategory,Model model, Model listingModel, Widget tempWidget, Model searchModel, MarketListing tempListing)
	{
		BigDecimal bigPrice = new BigDecimal("0.00");
		try {
			System.out.println(searchString);
		}
		catch(Exception e) {
			
		}
		try {
			System.out.println(operator);
		}
		catch(Exception e) {
			
		}
		try {
			bigPrice = BigDecimal.valueOf(Double.valueOf(price));
		}
		catch (Exception e) {
			
		}

		if(category.contentEquals("all"))
		{
			if(searchString.isBlank() && price.isBlank())
			{
				System.out.println("only category");
				model.addAttribute("widgets", widgetRepository.findAll());
				listingModel.addAttribute("listings", marketListingRepos.findAll());
				int id = 0;
				List<Widget> widgets = new ArrayList<Widget>();
				List<Widget> allWidgets = new ArrayList<Widget>();
				allWidgets = (List<Widget>) widgetController.getAllWidgets();
				tempWidget = allWidgets.get(id);
				while(tempWidget.getName()!=null)
				{
					widgets.add(tempWidget);
					id++;
					try {
						tempWidget = allWidgets.get(id);
					}
					catch(Exception e)
					{
						break;
					}
				}
				try {
					searchModel.addAttribute("searchWidgets", widgets);
				}
				catch(Exception e){
					System.out.println("No widgets");
				}
			}
			if(!searchString.isBlank() && price.isBlank())
			{
				System.out.println("only search string");
				model.addAttribute("searchString", searchString);
				model.addAttribute("widgets", widgetRepository.findAll());
				listingModel.addAttribute("listings", marketListingRepos.findAll());
				int id = 0;
				List<Widget> widgets = new ArrayList<Widget>();
				List<Widget> allWidgets = new ArrayList<Widget>();
				allWidgets = (List<Widget>) widgetController.getAllWidgets();
				tempWidget = allWidgets.get(id);
				
				while(tempWidget.getName()!=null)
				{
					tempWidget = allWidgets.get(id);
					if(tempWidget.getName().contains(searchString) || tempWidget.getName().toUpperCase().contains(searchString) || tempWidget.getName().toLowerCase().contains(searchString)
							|| tempWidget.getDescription().contains(searchString) || tempWidget.getDescription().toUpperCase().contains(searchString) || tempWidget.getDescription().toLowerCase().contains(searchString))
					{
						widgets.add(tempWidget);
					}

					id++;
					try {
						tempWidget = allWidgets.get(id);
					}
					catch(Exception e)
					{
						break;
					}

				}
				try {
					searchModel.addAttribute("searchWidgets", widgets);
				}
				catch(Exception e){
					System.out.println("No widgets");
				}
				
			}
			else if(searchString.isBlank() && !price.isBlank())
			{
				System.out.println("only price");
				model.addAttribute("operator", operator);
				model.addAttribute("price", price);
				model.addAttribute("widgets", widgetRepository.findAll());
				listingModel.addAttribute("listings", marketListingRepos.findAll());
				int id = 0;
				List<Widget> widgets = new ArrayList<Widget>();
				List<Widget> allWidgets = new ArrayList<Widget>();
				List<MarketListing> listings = new ArrayList<MarketListing>();
				List<MarketListing> allListings = new ArrayList<MarketListing>();
				allWidgets = (List<Widget>) widgetController.getAllWidgets();
				allListings = (List<MarketListing>) marketController.getAllListings();
				tempListing = allListings.get(id);
				tempWidget = tempListing.getWidgetSold();
				
				while(tempWidget != null)
				{
					System.out.println("in loop");
					tempListing = allListings.get(id);
					tempWidget = tempListing.getWidgetSold();
					int res = tempListing.getPricePerItem().compareTo(bigPrice);
					System.out.println(res);
					System.out.println(bigPrice);
					if(operator.contentEquals("greater") && res == 1)
					{
						widgets.add(tempWidget);
						System.out.println("should be added");
					}
					else if(operator.contentEquals("lesser") && res == -1)
					{
						widgets.add(tempWidget);
						System.out.println("should be added");
					}
					id++;
					try
					{
						tempListing = allListings.get(id);
						tempWidget = tempListing.getWidgetSold();
					}
					catch(Exception e)
					{
						break;
					}
					
				}
				searchModel.addAttribute("searchWidgets", widgets);
			}
			else if(!searchString.isBlank() && !price.isBlank())
			{
				System.out.println("both");
				model.addAttribute("searchString", searchString);
				model.addAttribute("operator", operator);
				model.addAttribute("price", price);
				model.addAttribute("widgets", widgetRepository.findAll());
				listingModel.addAttribute("listings", marketListingRepos.findAll());
				int id = 0;
				List<Widget> widgets = new ArrayList<Widget>();
				List<Widget> allWidgets = new ArrayList<Widget>();
				List<MarketListing> listings = new ArrayList<MarketListing>();
				List<MarketListing> allListings = new ArrayList<MarketListing>();
				allWidgets = (List<Widget>) widgetController.getAllWidgets();
				allListings = (List<MarketListing>) marketController.getAllListings();
				tempListing = allListings.get(id);
				tempWidget = tempListing.getWidgetSold();
				
				while(tempWidget != null)
				{
					tempListing = allListings.get(id);
					tempWidget = tempListing.getWidgetSold();
					if(tempWidget.getName().contains(searchString) || tempWidget.getName().toUpperCase().contains(searchString) || tempWidget.getName().toLowerCase().contains(searchString)
							|| tempWidget.getDescription().contains(searchString) || tempWidget.getDescription().toUpperCase().contains(searchString) || tempWidget.getDescription().toLowerCase().contains(searchString))
					{
						int res = tempListing.getPricePerItem().compareTo(bigPrice);
						if(operator.contentEquals("greater") && (res == 1))
						{
							widgets.add(tempWidget);
							System.out.println("should be added");
						}
						else if(operator.contentEquals("lesser") && (res == -1))
						{
							widgets.add(tempWidget);
							System.out.println("should be added");
						}
					}
					id++;
					try
					{
						tempListing = allListings.get(id);
						tempWidget = tempListing.getWidgetSold();
					}
					catch(Exception e)
					{
						break;
					}
					
				}
				searchModel.addAttribute("searchWidgets", widgets);
			}
		}
		
		if(category.contentEquals("appliance"))
		{
			if(subCategory.contentEquals("washer"))
			{
				
			}
			if(subCategory.contentEquals("dryer"))
			{
				
			}
			if(subCategory.contentEquals("microwave"))
			{
				
			}
			if(subCategory.contentEquals("fridge"))
			{
				
			}
			if(searchString.isBlank() && price.isBlank())
			{
				System.out.println("only category");
				model.addAttribute("widgets", widgetRepository.findAll());
				listingModel.addAttribute("listings", marketListingRepos.findAll());
				int id = 0;
				List<Widget> widgets = new ArrayList<Widget>();
				List<Widget> allWidgets = new ArrayList<Widget>();
				allWidgets = (List<Widget>) widgetController.getAllWidgets();
				tempWidget = allWidgets.get(id);
				while(tempWidget.getName()!=null)
				{
					if(tempWidget.getCategory().contains(category))
					{
						widgets.add(tempWidget);
					}
					
					id++;
					try {
						tempWidget = allWidgets.get(id);
					}
					catch(Exception e)
					{
						break;
					}
				}
				try {
					searchModel.addAttribute("searchWidgets", widgets);
				}
				catch(Exception e){
					System.out.println("No widgets");
				}
			}
			if(!searchString.isBlank() && price.isBlank())
			{
				System.out.println("only search string");
				model.addAttribute("searchString", searchString);
				model.addAttribute("widgets", widgetRepository.findAll());
				listingModel.addAttribute("listings", marketListingRepos.findAll());
				int id = 0;
				List<Widget> widgets = new ArrayList<Widget>();
				List<Widget> allWidgets = new ArrayList<Widget>();
				allWidgets = (List<Widget>) widgetController.getAllWidgets();
				tempWidget = allWidgets.get(id);
				System.out.println(tempWidget.getName());
				
				while(tempWidget!=null)
				{
					tempWidget = allWidgets.get(id);
					if((tempWidget.getName().contains(searchString) || tempWidget.getName().toUpperCase().contains(searchString) || tempWidget.getName().toLowerCase().contains(searchString)
							|| tempWidget.getDescription().contains(searchString) || tempWidget.getDescription().toUpperCase().contains(searchString) || tempWidget.getDescription().toLowerCase().contains(searchString)) && tempWidget.getCategory().contentEquals(category))
					{
						widgets.add(tempWidget);
					}


					id++;
					try {
						tempWidget = allWidgets.get(id);
					}
					catch(Exception e)
					{
						break;
					}

				}

				searchModel.addAttribute("searchWidgets", widgets);
			}
			else if(searchString.isBlank() && !price.isBlank())
			{
				System.out.println("only price");
				model.addAttribute("operator", operator);
				model.addAttribute("price", price);
				model.addAttribute("widgets", widgetRepository.findAll());
				listingModel.addAttribute("listings", marketListingRepos.findAll());
				int id = 0;
				List<Widget> widgets = new ArrayList<Widget>();
				List<Widget> allWidgets = new ArrayList<Widget>();
				List<MarketListing> listings = new ArrayList<MarketListing>();
				List<MarketListing> allListings = new ArrayList<MarketListing>();
				allWidgets = (List<Widget>) widgetController.getAllWidgets();
				allListings = (List<MarketListing>) marketController.getAllListings();
				tempListing = allListings.get(id);
				tempWidget = tempListing.getWidgetSold();
				
				while(tempWidget != null)
				{
					System.out.println("in loop");
					tempListing = allListings.get(id);
					tempWidget = tempListing.getWidgetSold();
					int res = tempListing.getPricePerItem().compareTo(bigPrice);
					System.out.println(res);
					if(operator.contentEquals("greater") && res == 1 && tempWidget.getCategory().contentEquals("appliance"))
					{
						widgets.add(tempWidget);
						System.out.println("should be added");
					}
					else if(operator.contentEquals("lesser") && res == -1 && tempWidget.getCategory().contentEquals("appliance"))
					{
						widgets.add(tempWidget);
						System.out.println("should be added");
					}
					id++;
					try
					{
						tempListing = allListings.get(id);
						tempWidget = tempListing.getWidgetSold();
					}
					catch(Exception e)
					{
						break;
					}
					
				}
				searchModel.addAttribute("searchWidgets", widgets);
			}
			else if(!searchString.isBlank() && !price.isBlank())
			{
				System.out.println("both");
				model.addAttribute("searchString", searchString);
				model.addAttribute("operator", operator);
				model.addAttribute("price", price);
				model.addAttribute("widgets", widgetRepository.findAll());
				listingModel.addAttribute("listings", marketListingRepos.findAll());
				int id = 0;
				List<Widget> widgets = new ArrayList<Widget>();
				List<Widget> allWidgets = new ArrayList<Widget>();
				List<MarketListing> listings = new ArrayList<MarketListing>();
				List<MarketListing> allListings = new ArrayList<MarketListing>();
				allWidgets = (List<Widget>) widgetController.getAllWidgets();
				allListings = (List<MarketListing>) marketController.getAllListings();
				tempListing = allListings.get(id);
				tempWidget = tempListing.getWidgetSold();
				
				while(tempWidget != null)
				{
					System.out.println("in loop");
					tempListing = allListings.get(id);
					tempWidget = tempListing.getWidgetSold();
					
					if((tempWidget.getName().contains(searchString) || tempWidget.getName().toUpperCase().contains(searchString) || tempWidget.getName().toLowerCase().contains(searchString)
							|| tempWidget.getDescription().contains(searchString) || tempWidget.getDescription().toUpperCase().contains(searchString) || tempWidget.getDescription().toLowerCase().contains(searchString))&& tempWidget.getCategory().contentEquals(category))
					{
						System.out.println("in if");
						int res = tempListing.getPricePerItem().compareTo(bigPrice);
						System.out.println(res);
						if(operator.contentEquals("greater") && (res == 1))
						{
							widgets.add(tempWidget);
							System.out.println("should be added");
						}
						else if(operator.contentEquals("lesser") && (res == -1))
						{
							widgets.add(tempWidget);
							System.out.println("should be added");
						}
					}
					id++;
					try
					{
						tempListing = allListings.get(id);
						tempWidget = tempListing.getWidgetSold();
					}
					catch(Exception e)
					{
						break;
					}
					
				}
				searchModel.addAttribute("searchWidgets", widgets);
			}
		}
		
		if(category.contentEquals("electronic"))
		{
			if(subCategory.contentEquals("videoGame"))
			{
				
			}
			if(subCategory.contentEquals("computer"))
			{
				
			}
			if(searchString.isBlank() && price.isBlank())
			{
				System.out.println("only category");
				model.addAttribute("widgets", widgetRepository.findAll());
				listingModel.addAttribute("listings", marketListingRepos.findAll());
				int id = 0;
				List<Widget> widgets = new ArrayList<Widget>();
				List<Widget> allWidgets = new ArrayList<Widget>();
				allWidgets = (List<Widget>) widgetController.getAllWidgets();
				tempWidget = allWidgets.get(id);
				while(tempWidget.getName()!=null)
				{
					if(tempWidget.getCategory().contains(category))
					{
						widgets.add(tempWidget);
					}
					
					id++;
					try {
						tempWidget = allWidgets.get(id);
					}
					catch(Exception e)
					{
						break;
					}
				}
				try {
					searchModel.addAttribute("searchWidgets", widgets);
				}
				catch(Exception e){
					System.out.println("No widgets");
				}
			}
			if(!searchString.isBlank() && price.isBlank())
			{
				System.out.println("only search string");
				model.addAttribute("searchString", searchString);
				model.addAttribute("widgets", widgetRepository.findAll());
				listingModel.addAttribute("listings", marketListingRepos.findAll());
				int id = 0;
				List<Widget> widgets = new ArrayList<Widget>();
				List<Widget> allWidgets = new ArrayList<Widget>();
				allWidgets = (List<Widget>) widgetController.getAllWidgets();
				tempWidget = allWidgets.get(id);
				
				while(tempWidget!=null)
				{
					tempWidget = allWidgets.get(id);
					if((tempWidget.getName().contains(searchString) || tempWidget.getName().toUpperCase().contains(searchString) || tempWidget.getName().toLowerCase().contains(searchString)
							|| tempWidget.getDescription().contains(searchString) || tempWidget.getDescription().toUpperCase().contains(searchString) || tempWidget.getDescription().toLowerCase().contains(searchString)) && tempWidget.getCategory().contentEquals(category))
					{
						widgets.add(tempWidget);
					}


					id++;
					try {
						tempWidget = allWidgets.get(id);
					}
					catch(Exception e)
					{
						break;
					}

				}

				searchModel.addAttribute("searchWidgets", widgets);
			}
			else if(searchString.isBlank() && !price.isBlank())
			{
				System.out.println("only price");
				model.addAttribute("operator", operator);
				model.addAttribute("price", price);
				model.addAttribute("widgets", widgetRepository.findAll());
				listingModel.addAttribute("listings", marketListingRepos.findAll());
				int id = 0;
				List<Widget> widgets = new ArrayList<Widget>();
				List<Widget> allWidgets = new ArrayList<Widget>();
				List<MarketListing> listings = new ArrayList<MarketListing>();
				List<MarketListing> allListings = new ArrayList<MarketListing>();
				allWidgets = (List<Widget>) widgetController.getAllWidgets();
				allListings = (List<MarketListing>) marketController.getAllListings();
				tempListing = allListings.get(id);
				tempWidget = tempListing.getWidgetSold();
				
				while(tempWidget != null)
				{
					System.out.println("in loop");
					tempListing = allListings.get(id);
					tempWidget = tempListing.getWidgetSold();
					int res = tempListing.getPricePerItem().compareTo(bigPrice);
					System.out.println(res);
					if(operator.contentEquals("greater") && res == 1 && tempWidget.getCategory().contentEquals("electronic"))
					{
						widgets.add(tempWidget);
						System.out.println("should be added");
					}
					else if(operator.contentEquals("lesser") && res == -1 && tempWidget.getCategory().contentEquals("electronic"))
					{
						widgets.add(tempWidget);
						System.out.println("should be added");
					}
					id++;
					try
					{
						tempListing = allListings.get(id);
						tempWidget = tempListing.getWidgetSold();
					}
					catch(Exception e)
					{
						break;
					}
					
				}
				searchModel.addAttribute("searchWidgets", widgets);
			}
			else if(!searchString.isBlank() && !price.isBlank())
			{
				System.out.println("both");
				model.addAttribute("searchString", searchString);
				model.addAttribute("operator", operator);
				model.addAttribute("price", price);
				model.addAttribute("widgets", widgetRepository.findAll());
				listingModel.addAttribute("listings", marketListingRepos.findAll());
				int id = 0;
				List<Widget> widgets = new ArrayList<Widget>();
				List<Widget> allWidgets = new ArrayList<Widget>();
				List<MarketListing> listings = new ArrayList<MarketListing>();
				List<MarketListing> allListings = new ArrayList<MarketListing>();
				allWidgets = (List<Widget>) widgetController.getAllWidgets();
				allListings = (List<MarketListing>) marketController.getAllListings();
				tempListing = allListings.get(id);
				tempWidget = tempListing.getWidgetSold();
				
				while(tempWidget != null)
				{
					tempListing = allListings.get(id);
					tempWidget = tempListing.getWidgetSold();
					if((tempWidget.getName().contains(searchString) || tempWidget.getName().toUpperCase().contains(searchString) || tempWidget.getName().toLowerCase().contains(searchString)
							|| tempWidget.getDescription().contains(searchString) || tempWidget.getDescription().toUpperCase().contains(searchString) || tempWidget.getDescription().toLowerCase().contains(searchString))&& tempWidget.getCategory().contentEquals(category))
					{
						int res = tempListing.getPricePerItem().compareTo(bigPrice);
						if(operator.contentEquals("greater") && (res == 1))
						{
							widgets.add(tempWidget);
							System.out.println("should be added");
						}
						else if(operator.contentEquals("lesser") && (res == -1))
						{
							widgets.add(tempWidget);
							System.out.println("should be added");
						}
					}
					id++;
					try
					{
						tempListing = allListings.get(id);
						tempWidget = tempListing.getWidgetSold();
					}
					catch(Exception e)
					{
						break;
					}
					
				}
				searchModel.addAttribute("searchWidgets", widgets);
			}
		}
		
		if(category.contentEquals("lawnCare"))
		{
			if(subCategory.contentEquals("lawnMower"))
			{
				
			}
			if(searchString.isBlank() && price.isBlank())
			{
				System.out.println("only category");
				model.addAttribute("widgets", widgetRepository.findAll());
				listingModel.addAttribute("listings", marketListingRepos.findAll());
				int id = 0;
				List<Widget> widgets = new ArrayList<Widget>();
				List<Widget> allWidgets = new ArrayList<Widget>();
				allWidgets = (List<Widget>) widgetController.getAllWidgets();
				tempWidget = allWidgets.get(id);
				while(tempWidget.getName()!=null)
				{
					if(tempWidget.getCategory().contains(category))
					{
						widgets.add(tempWidget);
					}
					
					id++;
					try {
						tempWidget = allWidgets.get(id);
					}
					catch(Exception e)
					{
						break;
					}
				}
				try {
					searchModel.addAttribute("searchWidgets", widgets);
				}
				catch(Exception e){
					System.out.println("No widgets");
				}
			}
			if(!searchString.isBlank() && price.isBlank())
			{
				System.out.println("only search string");
				model.addAttribute("searchString", searchString);
				model.addAttribute("widgets", widgetRepository.findAll());
				listingModel.addAttribute("listings", marketListingRepos.findAll());
				int id = 0;
				List<Widget> widgets = new ArrayList<Widget>();
				List<Widget> allWidgets = new ArrayList<Widget>();
				allWidgets = (List<Widget>) widgetController.getAllWidgets();
				tempWidget = allWidgets.get(id);
				
				while(tempWidget!=null)
				{
					tempWidget = allWidgets.get(id);
					if((tempWidget.getName().contains(searchString) || tempWidget.getName().toUpperCase().contains(searchString) || tempWidget.getName().toLowerCase().contains(searchString)
							|| tempWidget.getDescription().contains(searchString) || tempWidget.getDescription().toUpperCase().contains(searchString) || tempWidget.getDescription().toLowerCase().contains(searchString)) && tempWidget.getCategory().contentEquals(category))
					{
						try {
							widgets.add(tempWidget);
						}
						catch(Exception e)
						{
							
						}
					}


					id++;
					try {
						tempWidget = allWidgets.get(id);
					}
					catch(Exception e)
					{
						break;
					}

				}

				searchModel.addAttribute("searchWidgets", widgets);
			}
			else if(searchString.isBlank() && !price.isBlank())
			{
				System.out.println("only price");
				model.addAttribute("operator", operator);
				model.addAttribute("price", price);
				model.addAttribute("widgets", widgetRepository.findAll());
				listingModel.addAttribute("listings", marketListingRepos.findAll());
				int id = 0;
				List<Widget> widgets = new ArrayList<Widget>();
				List<Widget> allWidgets = new ArrayList<Widget>();
				List<MarketListing> listings = new ArrayList<MarketListing>();
				List<MarketListing> allListings = new ArrayList<MarketListing>();
				allWidgets = (List<Widget>) widgetController.getAllWidgets();
				allListings = (List<MarketListing>) marketController.getAllListings();
				tempListing = allListings.get(id);
				tempWidget = tempListing.getWidgetSold();
				
				while(tempWidget != null)
				{
					System.out.println("in loop");
					tempListing = allListings.get(id);
					tempWidget = tempListing.getWidgetSold();
					int res = tempListing.getPricePerItem().compareTo(bigPrice);
					System.out.println(res);
					if(operator.contentEquals("greater") && res == 1 && tempWidget.getCategory().contentEquals("lawnCare"))
					{
						widgets.add(tempWidget);
						System.out.println("should be added");
					}
					else if(operator.contentEquals("lesser") && res == -1 && tempWidget.getCategory().contentEquals("lawnCare"))
					{
						widgets.add(tempWidget);
						System.out.println("should be added");
					}
					id++;
					try
					{
						tempListing = allListings.get(id);
						tempWidget = tempListing.getWidgetSold();
					}
					catch(Exception e)
					{
						break;
					}
					
				}
				searchModel.addAttribute("searchWidgets", widgets);
			}
			else if(!searchString.isBlank() && !price.isBlank())
			{
				System.out.println("both");
				model.addAttribute("searchString", searchString);
				model.addAttribute("operator", operator);
				model.addAttribute("price", price);
				model.addAttribute("widgets", widgetRepository.findAll());
				listingModel.addAttribute("listings", marketListingRepos.findAll());
				int id = 0;
				List<Widget> widgets = new ArrayList<Widget>();
				List<Widget> allWidgets = new ArrayList<Widget>();
				List<MarketListing> listings = new ArrayList<MarketListing>();
				List<MarketListing> allListings = new ArrayList<MarketListing>();
				allWidgets = (List<Widget>) widgetController.getAllWidgets();
				allListings = (List<MarketListing>) marketController.getAllListings();
				tempListing = allListings.get(id);
				tempWidget = tempListing.getWidgetSold();
				
				while(tempWidget != null)
				{
					tempListing = allListings.get(id);
					tempWidget = tempListing.getWidgetSold();
					if((tempWidget.getName().contains(searchString) || tempWidget.getName().toUpperCase().contains(searchString) || tempWidget.getName().toLowerCase().contains(searchString)
							|| tempWidget.getDescription().contains(searchString) || tempWidget.getDescription().toUpperCase().contains(searchString) || tempWidget.getDescription().toLowerCase().contains(searchString))&& tempWidget.getCategory().contentEquals(category))
					{
						int res = tempListing.getPricePerItem().compareTo(bigPrice);
						if(operator.contentEquals("greater") && (res == 1))
						{
							widgets.add(tempWidget);
							System.out.println("should be added");
						}
						else if(operator.contentEquals("lesser") && (res == -1))
						{
							widgets.add(tempWidget);
							System.out.println("should be added");
						}
					}
					id++;
					try
					{
						tempListing = allListings.get(id);
						tempWidget = tempListing.getWidgetSold();
					}
					catch(Exception e)
					{
						break;
					}
					
				}
				searchModel.addAttribute("searchWidgets", widgets);
			}
		}
		
		if(category.contentEquals("vehicle"))
		{
			if(subCategory.contentEquals("car"))
			{
				
			}
			if(searchString.isBlank() && price.isBlank())
			{
				System.out.println("only category");
				model.addAttribute("widgets", widgetRepository.findAll());
				listingModel.addAttribute("listings", marketListingRepos.findAll());
				int id = 0;
				List<Widget> widgets = new ArrayList<Widget>();
				List<Widget> allWidgets = new ArrayList<Widget>();
				allWidgets = (List<Widget>) widgetController.getAllWidgets();
				tempWidget = allWidgets.get(id);
				while(tempWidget.getName()!=null)
				{
					if(tempWidget.getCategory().contains(category))
					{
						widgets.add(tempWidget);
					}
					
					id++;
					try {
						tempWidget = allWidgets.get(id);
					}
					catch(Exception e)
					{
						break;
					}
				}
				try {
					searchModel.addAttribute("searchWidgets", widgets);
				}
				catch(Exception e){
					System.out.println("No widgets");
				}
			}
			if(!searchString.isBlank() && price.isBlank())
			{
				System.out.println("only search string");
				model.addAttribute("searchString", searchString);
				model.addAttribute("widgets", widgetRepository.findAll());
				listingModel.addAttribute("listings", marketListingRepos.findAll());
				int id = 0;
				List<Widget> widgets = new ArrayList<Widget>();
				List<Widget> allWidgets = new ArrayList<Widget>();
				allWidgets = (List<Widget>) widgetController.getAllWidgets();
				tempWidget = allWidgets.get(id);
				
				while(tempWidget!=null)
				{
					tempWidget = allWidgets.get(id);
					if((tempWidget.getName().contains(searchString) || tempWidget.getName().toUpperCase().contains(searchString) || tempWidget.getName().toLowerCase().contains(searchString)
							|| tempWidget.getDescription().contains(searchString) || tempWidget.getDescription().toUpperCase().contains(searchString) || tempWidget.getDescription().toLowerCase().contains(searchString)) && tempWidget.getCategory().contentEquals("vehicle"))
					{
						widgets.add(tempWidget);
					}


					id++;
					try {
						tempWidget = allWidgets.get(id);
					}
					catch(Exception e)
					{
						break;
					}

				}

				searchModel.addAttribute("searchWidgets", widgets);
			}
			else if(searchString.isBlank() && !price.isBlank())
			{
				System.out.println("only price");
				model.addAttribute("operator", operator);
				model.addAttribute("price", price);
				model.addAttribute("widgets", widgetRepository.findAll());
				listingModel.addAttribute("listings", marketListingRepos.findAll());
				int id = 0;
				List<Widget> widgets = new ArrayList<Widget>();
				List<Widget> allWidgets = new ArrayList<Widget>();
				List<MarketListing> listings = new ArrayList<MarketListing>();
				List<MarketListing> allListings = new ArrayList<MarketListing>();
				allWidgets = (List<Widget>) widgetController.getAllWidgets();
				allListings = (List<MarketListing>) marketController.getAllListings();
				tempListing = allListings.get(id);
				tempWidget = tempListing.getWidgetSold();
				
				while(tempWidget != null)
				{
					System.out.println("in loop");
					tempListing = allListings.get(id);
					tempWidget = tempListing.getWidgetSold();
					int res = tempListing.getPricePerItem().compareTo(bigPrice);
					System.out.println(res);
					if(operator.contentEquals("greater") && res == 1 && tempWidget.getCategory().contentEquals("vehicle"))
					{
						widgets.add(tempWidget);
						System.out.println("should be added");
					}
					else if(operator.contentEquals("lesser") && res == -1 && tempWidget.getCategory().contentEquals("vehicle"))
					{
						widgets.add(tempWidget);
						System.out.println("should be added");
					}
					id++;
					try
					{
						tempListing = allListings.get(id);
						tempWidget = tempListing.getWidgetSold();
					}
					catch(Exception e)
					{
						break;
					}
					
				}
				searchModel.addAttribute("searchWidgets", widgets);
			}
			else if(!searchString.isBlank() && !price.isBlank())
			{
				System.out.println("both");
				model.addAttribute("searchString", searchString);
				model.addAttribute("operator", operator);
				model.addAttribute("price", price);
				model.addAttribute("widgets", widgetRepository.findAll());
				listingModel.addAttribute("listings", marketListingRepos.findAll());
				int id = 0;
				List<Widget> widgets = new ArrayList<Widget>();
				List<Widget> allWidgets = new ArrayList<Widget>();
				List<MarketListing> listings = new ArrayList<MarketListing>();
				List<MarketListing> allListings = new ArrayList<MarketListing>();
				allWidgets = (List<Widget>) widgetController.getAllWidgets();
				allListings = (List<MarketListing>) marketController.getAllListings();
				tempListing = allListings.get(id);
				tempWidget = tempListing.getWidgetSold();
				
				while(tempWidget != null)
				{
					tempListing = allListings.get(id);
					tempWidget = tempListing.getWidgetSold();
					if((tempWidget.getName().contains(searchString) || tempWidget.getName().toUpperCase().contains(searchString) || tempWidget.getName().toLowerCase().contains(searchString)
							|| tempWidget.getDescription().contains(searchString) || tempWidget.getDescription().toUpperCase().contains(searchString) || tempWidget.getDescription().toLowerCase().contains(searchString))&& tempWidget.getCategory().contentEquals(category))
					{
						int res = tempListing.getPricePerItem().compareTo(bigPrice);
						if(operator.contentEquals("greater") && (res == 1))
						{
							widgets.add(tempWidget);
							System.out.println("should be added");
						}
						else if(operator.contentEquals("lesser") && (res == -1))
						{
							widgets.add(tempWidget);
							System.out.println("should be added");
						}
					}
					id++;
					try
					{
						tempListing = allListings.get(id);
						tempWidget = tempListing.getWidgetSold();
					}
					catch(Exception e)
					{
						break;
					}
					
				}
				searchModel.addAttribute("searchWidgets", widgets);
			}
		}
		
		return "searchResult";
	}
	

}