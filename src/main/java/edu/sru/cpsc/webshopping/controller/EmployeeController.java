package edu.sru.cpsc.webshopping.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.sru.cpsc.webshopping.domain.market.MarketListing;
import edu.sru.cpsc.webshopping.domain.user.Applicant;
import edu.sru.cpsc.webshopping.domain.user.Message;
import edu.sru.cpsc.webshopping.domain.user.User;
import edu.sru.cpsc.webshopping.domain.widgets.Widget;
import edu.sru.cpsc.webshopping.repository.applicant.ApplicantRepository;
import edu.sru.cpsc.webshopping.repository.user.UserRepository;

@Controller
public class EmployeeController {
	private UserController userController;
	private MarketListingDomainController market;
	private MessageDomainController msgcontrol;
	private ApplicantRepository appRepo;
	private String page;
	private String mailboxPage;
	private String edit;
	private List<User> allUsers = new ArrayList<>();
	private List<Applicant> allApplicants = new ArrayList<>();
	public int add;
	private User searchedUser;
	public Widget searchedWidget = new Widget();
	public MarketListing searchedMarket = new MarketListing();
	public String search;
	 List<String> roleList;
	 @ModelAttribute
	 public void preLoad(Model model){
      roleList = new ArrayList<>();
      roleList.add("ROLE_USER");
      roleList.add("ROLE_ADMIN");
      roleList.add("ROLE_QUALITYCONTROLLER");
      roleList.add("ROLE_CUSTOMERSERVICE");
      roleList.add("ROLE_SALES");
      roleList.add("ROLE_HIRINGMANAGER");
      
	 }

	public EmployeeController(UserController userController,MarketListingDomainController market,MessageDomainController msgcontrol,ApplicantRepository appRepo) {
		this.userController = userController;
		this.market = market;
		this.msgcontrol = msgcontrol;
		this.appRepo = appRepo;
	}
    @RequestMapping({"/employee"})
    public String showEmployeePage(Model model) {
    	
    	
		User user = new User();
		User useradd = new User();
		user = userController.getCurrently_Logged_In();
		
	
		model.addAttribute("user", user);
		model.addAttribute("useradd", useradd);
		model.addAttribute("add", add);
		model.addAttribute("roleList", roleList);
		model.addAttribute("searchedUser", getSearchedUser());
        return "employee";
    }
    
    @PostMapping({"/createEmployee"})
    public String createEmp(@Valid User useradd,@RequestParam String role, BindingResult result, Model model) {
		User usertemp = useradd;
	    if(userController.getUserByUsername(usertemp.getUsername()) != null) {
	    	result.addError(
	    			new FieldError("username", "username", "Username is already taken."));	    	
	       }
	    	if (result.hasErrors()) {
	    	 return "redirect:employee" ;
	    	}
				setPage("null");
				model.addAttribute("page", page);
	    		useradd.setEnabled(true);
				userController.addUser(useradd, result);
			return "redirect:employee";
   
    }
    @RequestMapping({"/createButton"})
    public String createButton(Model model) {
		setPage("create");
		User user = userController.getCurrently_Logged_In();
		User useradd = new User();
		model.addAttribute("roleList", roleList);
		model.addAttribute("useradd", useradd);
		model.addAttribute("user", user);
		model.addAttribute("page", page);
			return "employee";
   
    }
    @RequestMapping({"/cancelButton"})
    public String cancelButton(Model model) {
		setPage("null");
		User user = userController.getCurrently_Logged_In();
		
		User useradd = new User();
		model.addAttribute("useradd", useradd);
		model.addAttribute("user", user);
		model.addAttribute("page", page);
			return "employee";
   
    }
    @RequestMapping({"/searchButton"})
    public String searchButton(Model model) {
		setPage("search");
		User user = userController.getCurrently_Logged_In();
		Iterable<User> allUsersIterator = userController.getAllUsers();
		
		
		allUsersIterator.iterator().forEachRemaining(u -> getAllUsers().add(u));

		User useradd = new User();
		model.addAttribute("roleList", roleList);
		model.addAttribute("users", getAllUsers());
		model.addAttribute("useradd", useradd);
		model.addAttribute("user", user);
		model.addAttribute("page", page);
			return "employee";
   
    }
    @RequestMapping({"/searchMessageButton"})
    public String searchMessageButton(Model model) {
		setPage("searchMessage");
		User user = userController.getCurrently_Logged_In();
		Iterable<User> allUsersIterator = userController.getAllUsers();
		
		
		allUsersIterator.iterator().forEachRemaining(u -> getAllUsers().add(u));

		User useradd = new User();
		model.addAttribute("useradd", useradd);
		model.addAttribute("roleList", roleList);
		model.addAttribute("user", user);
		model.addAttribute("users", getAllUsers());
		model.addAttribute("page", page);
			return "employee";
   
    }

@GetMapping({"/viewSpecificUserInbox/{id}"})
public String viewUserInbox(@PathVariable("id") int id,Model model) {

		User user2 = userController.getUser(id, model);
		Message[] inbox = msgcontrol.getUserInbox(user2);
		
		User user = userController.getCurrently_Logged_In();
		setMailboxPage("inboxPage");
		
		User useradd = new User();
		
		model.addAttribute("mailpage", getMailboxPage());
		model.addAttribute("roleList", roleList);
		model.addAttribute("inbox", inbox);
		model.addAttribute("user", user);
		model.addAttribute("useradd", useradd);
		model.addAttribute("users", getAllUsers());
		model.addAttribute("page", getPage());
		model.addAttribute("searchedUser", getSearchedUser());

    return "employee";
}
@GetMapping({"/viewSpecificUserOutbox/{id}"})
public String viewUserOutbox(@PathVariable("id") int id,Model model) {
		User user2 = userController.getUser(id, model);
		Message[] outbox = msgcontrol.getUserOutbox(user2);
		User user = userController.getCurrently_Logged_In();
		setMailboxPage("outboxPage");
		User useradd = new User();
		model.addAttribute("mailpage", getMailboxPage());
		model.addAttribute("searchedUser", getSearchedUser());
		model.addAttribute("roleList", roleList);
		model.addAttribute("outbox", outbox);
		model.addAttribute("useradd", useradd);
		model.addAttribute("users", getAllUsers());
		model.addAttribute("user", user);
		model.addAttribute("page", getPage());
    return "employee";
}
@GetMapping({"/viewSpecificUserTrash/{id}"})
public String viewUserTrash(@PathVariable("id") int id,Model model) {
		User user2 = userController.getUser(id, model);
		Message[] trash= msgcontrol.getUserTrash(user2);
		User user = userController.getCurrently_Logged_In();
		setMailboxPage("trashPage");
		User useradd = new User();
		model.addAttribute("mailpage", getMailboxPage());
		model.addAttribute("searchedUser", getSearchedUser());
		model.addAttribute("roleList", roleList);
		model.addAttribute("trash", trash);
		model.addAttribute("useradd", useradd);
		model.addAttribute("users", getAllUsers());
		model.addAttribute("user", user);
		model.addAttribute("page", getPage());
    return "employee";
}

    @RequestMapping({"/searchApplicationsButton"})
    public String searchApplicationsButton(Model model) {
		setPage("applications");
		User user = userController.getCurrently_Logged_In();
		Iterable<Applicant> applicantIterator = appRepo.findAll();
		
		applicantIterator .iterator().forEachRemaining(u -> getAllApplicants().add(u));
		User useradd = new User();
		model.addAttribute("applicants",getAllApplicants());
		model.addAttribute("useradd", useradd);
		model.addAttribute("user", user);
		model.addAttribute("page", page);
			return "employee";
   
    }

    @RequestMapping({"/lookupStatistics"})
    public String StatisticsButton(Model model) {
		setPage("stats");
		User user = userController.getCurrently_Logged_In();
		
		User useradd = new User();
		model.addAttribute("useradd", useradd);
		model.addAttribute("user", user);
		model.addAttribute("page", page);
			return "employee";
   
    }
    @RequestMapping({"/searchTickets"})
    public String searchTickets(Model model) {
		setPage("qualityControl");
		User user = userController.getCurrently_Logged_In();
		
		User useradd = new User();
		model.addAttribute("useradd", useradd);
		model.addAttribute("user", user);
		model.addAttribute("page", page);
			return "employee";
   
    }
    @RequestMapping({"/searchCustomerServiceTicketButton"})
    public String searchCustomerServiceTicketButton(Model model) {
		setPage("customerService");
		User user = userController.getCurrently_Logged_In();
		
		User useradd = new User();
		model.addAttribute("useradd", useradd);
		model.addAttribute("user", user);
		model.addAttribute("page", page);
			return "employee";
   
    }
    @RequestMapping({"/searchQualityControlTicketButton"})
    public String searchQualityControlTicketButton(Model model) {
		setPage("qualityControl");
		User user = userController.getCurrently_Logged_In();
		
		User useradd = new User();
		model.addAttribute("useradd", useradd);
		model.addAttribute("user", user);
		model.addAttribute("page", page);
			return "employee";
   
    }

   
    @GetMapping({"/editUserButton/{id}"})
    public String editUserButton(@PathVariable("id") int id,Model model) {
		setEdit("user");
		User user2 = userController.getUser(id, model);
		User user = userController.getCurrently_Logged_In();
		User useradd = new User();
	
		model.addAttribute("users", getAllUsers());
		model.addAttribute("searchedUser", getSearchedUser());
		model.addAttribute("roleList", roleList);
		model.addAttribute("useradd", useradd);
		model.addAttribute("user", user);
		model.addAttribute("page", page);
		model.addAttribute("edit", edit);
			return "employee";
   
    }
    
    @RequestMapping({"/editUser"})
    public String sendMessage(
    @RequestParam("id") Long id,@RequestParam("userName") String userName, @RequestParam("password") String password,
    @RequestParam("passwordConf") String passwordConf,@RequestParam("email") String email,
    @RequestParam("fName") String fName, @RequestParam("lName") String lName,
    @RequestParam("phoneNumber") String phoneNumber, @RequestParam("cc") String cc,
    @RequestParam("role") String role, @RequestParam("emailVerification") String emailVerification,
    @RequestParam("dispName") String dispName, @RequestParam("userDesc") String userDesc,
    @RequestParam("creationDate") String creationDate,Model model) {

 		
 		
        return "employee";
    }

    @GetMapping({"/searchUser/{id}"})
    public String searchUserById(@PathVariable("id") int id,Model model) {
    	User user = userController.getCurrently_Logged_In();
    	setSearchedUser(userController.getUser(id, model));
    	model.addAttribute("user", user);
		setPage("userResult");
		User useradd = new User();
		model.addAttribute("users", getAllUsers());
		model.addAttribute("useradd", useradd);
		model.addAttribute("page", page);
		model.addAttribute("roleList", roleList);
		model.addAttribute("searchedUser", getSearchedUser());

			return "employee";
   
    }
    
    @PostMapping({"/searchUser"})
    public String searchuser(@Valid User searchedUser, BindingResult result, Model model) {
    	User user = userController.getCurrently_Logged_In();
    	if(userController.getUserByUsername(searchedUser.getUsername()) == null){
    	setSearchedUser(userController.getUserByEmail(searchedUser.getEmail()));
    	}
    	else {
    	setSearchedUser(userController.getUserByUsername(searchedUser.getUsername()));
    	}

		model.addAttribute("user", user);
		setPage("userResult");
		User useradd = new User();
		model.addAttribute("useradd", useradd);
		model.addAttribute("page", page);
		model.addAttribute("roleList", roleList);
		model.addAttribute("searchedUser", getSearchedUser());

			return "employee";
   
    }

		@GetMapping({"/viewListing/{id}"})
    public String searchMarketListingButton(@PathVariable("id") int id,Model model) {
			System.out.println(id);
		setMailboxPage("searchMarketListing");
		User user = userController.getCurrently_Logged_In();
		User user2 = userController.getUser(id, model);
		MarketListing[] listings = market.getListingbyUser(user2);
		Widget[] widgets = new Widget[listings.length];
		User[] sellers = new User[listings.length];
		Object[][][] listObject = new Object[listings.length+1][listings.length+1][listings.length+1];
		for(int i = 0; i < listings.length;i++) {
		widgets[i] = listings[i].getWidgetSold();
		sellers[i] = listings[i].getSeller();
		listObject[i+1][0][0] = widgets[i];
		listObject[0][i+1][0] = sellers[i];
		listObject[0][0][i+1] = listings[i];
		}


		User useradd = new User();
		model.addAttribute("container", listObject);
		model.addAttribute("listings", listings);
		model.addAttribute("roleList", roleList);
		model.addAttribute("searchedUser", getSearchedUser());
		model.addAttribute("users", getAllUsers());
		model.addAttribute("useradd", useradd);
		model.addAttribute("user", user);
		model.addAttribute("page", page);
		model.addAttribute("mailpage", getMailboxPage());
		model.addAttribute("sellers", sellers);
		model.addAttribute("searchedUser", getSearchedUser());
		model.addAttribute("searchedWidgets", widgets);
		model.addAttribute("searchedMarkets", listings);
			return "employee";
    }
    @PostMapping({"/searchListing"})
    public String searchListing(@RequestParam("widgetName") String widgetName, Model model) {
    	User user = userController.getCurrently_Logged_In();
		model.addAttribute("user", user);
		User useradd = new User();
		model.addAttribute("useradd", useradd);
		model.addAttribute("page", page);
		model.addAttribute("roleList", roleList);
    	if(market.getWidget(widgetName)== null) {
    		setPage("listingSearchError");
    		return "employee";
    	}
		searchedWidget = market.getWidget(widgetName);
		searchedMarket = market.getListingByWidget(searchedWidget);
		User seller = searchedMarket.getSeller();
		model.addAttribute("user", user);
		setPage("listingResult");
		model.addAttribute("seller", seller);
		model.addAttribute("searchedUser", getSearchedUser());
		model.addAttribute("searchedWidget", searchedWidget);
		model.addAttribute("searchedMarket", searchedMarket);
		model.addAttribute("page", page);
		


			return "employee";
   
    }
    
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getEdit() {
		return edit;
	}
	public void setEdit(String edit) {
		this.edit = edit;
	}
	public User getSearchedUser() {
		return searchedUser;
	}
	public void setSearchedUser(User searchedUser) {
		this.searchedUser = searchedUser;
	}

	public String getMailboxPage() {
		return mailboxPage;
	}

	public void setMailboxPage(String mailboxPage) {
		this.mailboxPage = mailboxPage;
	}

	public List<User> getAllUsers() {
		return allUsers;
	}

	public void setAllUsers(List<User> allUsers) {
		this.allUsers = allUsers;
	}

	public List<Applicant> getAllApplicants() {
		return allApplicants;
	}

	public void setAllApplicants(List<Applicant> allApplicants) {
		this.allApplicants = allApplicants;
	}
    
}
