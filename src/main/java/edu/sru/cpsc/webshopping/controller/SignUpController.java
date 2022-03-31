package edu.sru.cpsc.webshopping.controller;




import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import edu.sru.cpsc.webshopping.domain.user.User;
import edu.sru.cpsc.webshopping.domain.widgets.Widget;
import edu.sru.cpsc.webshopping.repository.user.UserRepository;


@Controller
//@RequestMapping(value = "/user")
public class SignUpController {
	@Autowired
	private PasswordEncoder passwordEncoder;
	private UserController userController;
	private UserRepository userRepository;
	private WidgetController widgetController;
	private EmailController email;
	private UtilityController util;
	User signingUpUser = new User();
	User findUser = new User();
	private String answer;
	private String question;
	private int theId;
	private String page;
	public SignUpController(UserController userController, WidgetController widgetController,EmailController email,UtilityController util, UserRepository userRepository) {
		this.userController = userController;
		this.widgetController = widgetController;
		this.email = email;
		this.util = util;
		this.userRepository = userRepository;
	}
	
	 List<String> countryCodes;
	 @ModelAttribute
	 public void preLoad(Model model){
		 countryCodes = new ArrayList<>();
		 countryCodes.add("+1       USA");
		 countryCodes.add("+995		Abkhazia");
		 countryCodes.add("+93      Afghanistan");
		 countryCodes.add("+61      Australia");
		 countryCodes.add("+86      China");
		 countryCodes.add("+49      Germany");
		 countryCodes.add("+62      Indonesia");
		 countryCodes.add("+98      Iran");
		 countryCodes.add("+39      Italy");
		 countryCodes.add("+64      New Zealand");
		 countryCodes.add("+63      Philippines");
		 countryCodes.add("+65      Singapore");
		 countryCodes.add("+66      Thailand");
		 countryCodes.add("+44      UK");
		 countryCodes.add("+84      Vietnam");

	 }
	 List<String> secretQuestion1;
	 @ModelAttribute
	 public void preLoad2(Model model){
		 secretQuestion1 = new ArrayList<>();
		 secretQuestion1.add("What is your mother's maiden name?");
		 secretQuestion1.add("What is the name of your first pet?");
		 secretQuestion1.add("What is the name of the street you grew up on?");
		 secretQuestion1.add("What is the make of the first car you owned?");
		 secretQuestion1.add("What city were you born in?");
	 }
	 List<String> secretQuestion2;
	 @ModelAttribute
	 public void preLoad3(Model model){
		 secretQuestion2 = new ArrayList<>();
		 secretQuestion2.add("What is your favorite band?");
		 secretQuestion2.add("What is your father's middle name?");
		 secretQuestion2.add("What is your favorite school subject?");
		 secretQuestion2.add("What was the name of your first boyfriend/girlfriend?");
		 secretQuestion2.add("Where is somewhere you've always wanted to travel to?");
	 }
	 List<String> secretQuestion3;
	 @ModelAttribute
	 public void preLoad4(Model model){
		 secretQuestion3 = new ArrayList<>();
		 secretQuestion3.add("What is your favorite beverage?");
		 secretQuestion3.add("What is your favorite video game?");
		 secretQuestion3.add("What is your favorite tv show?");
		 secretQuestion3.add("What is your favorite outdoor activity?");
		 secretQuestion3.add("What was your highschool mascot?");
	 }
	
	@RequestMapping({"/newUser"})
	public String newUser(Model model) {
		User user = new User();
		userController.getCaptcha(user);
		model.addAttribute("user", user);
		model.addAttribute("secretQuestion1", secretQuestion1);
		model.addAttribute("secretQuestion2", secretQuestion2);
		model.addAttribute("secretQuestion3", secretQuestion3);
		model.addAttribute("countryCodes", countryCodes);
		model.addAttribute("users", userController.getAllUsers());
		model.addAttribute("widget", new Widget());
		model.addAttribute("widgets", widgetController.getAllWidgets());
		model.addAttribute("selectedWidget", null);
		return "newUser";
	}

	@PostMapping("/add-user-signup")
	public String addUser(@Valid User user, BindingResult result, Model model){
		
		User usertemp = user;
		String countryCode = user.getCountryCode();
		String[] data = countryCode.split(" ");

		user.setPhoneNumber(data[0] + user.getPhoneNumber());
		String countryCodeRegex = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$";

		  if(!(user.getPhoneNumber().matches(countryCodeRegex))) {
		    	result.addError(
		    	          new FieldError("phoneNumber", "phoneNumber", "Enter Phone number: ###-###-#### or ##########"));	    	
		       }
	    if(userController.getUserByUsername(usertemp.getUsername()) != null) {
	    	result.addError(
	    	          new FieldError("username", "username", "Username is already taken."));	    	
	       }
	    if(userController.getUserByEmail(usertemp.getEmail()) != null) {
	    	result.addError(
	    	          new FieldError("email", "email", "An account is already associated with this email."));	    	
	       }

	    if(!(usertemp.getPasswordconf().equals(usertemp.getPassword()))) {
	 
	    	result.addError(
	    	          new FieldError("passwordconf", "passwordconf", "Passwords don't match."));
	       }

	   
	    if (result.hasErrors()) {
	    	model.addAttribute("user", user);
	    	model.addAttribute("countryCodes", countryCodes);
			model.addAttribute("secretQuestion1", secretQuestion1);
			model.addAttribute("secretQuestion2", secretQuestion2);
			model.addAttribute("secretQuestion3", secretQuestion3);
	    	 userController.getCaptcha(user);
        return "newUser" ;
}
	    
		if(user.getCaptcha().equals(user.getHiddenCaptcha())) {
			if(user.getUsername().contains("admin" + "widget"))
			{
				user.setRole("ROLE_ADMIN");
				user.setEnabled(true);
			}
			model.addAttribute("message", "User Registered successfully!");
			if(user.getRole() != "ROLE_ADMIN") {
			email.verificationEmail(user, util.randomStringGenerator());
			}
			signingUpUser = user;
			userController.addUser(user, result);
			model.addAttribute("user", user);
			return "redirect:index";
			} 
			else {
		   result.addError(
		    	          new FieldError("captcha", "captcha", "Incorrect Captcha."));
			userController.getCaptcha(user);
			model.addAttribute("secretQuestion1", secretQuestion1);
			model.addAttribute("secretQuestion2", secretQuestion2);
			model.addAttribute("secretQuestion3", secretQuestion3);
			model.addAttribute("user", user);
			model.addAttribute("countryCodes", countryCodes);
			return "newUser";
			}
	   
	    
	}
	@RequestMapping("/userSecrets")
	public String addUserSecrets(@RequestParam("secret1") String secret1,@RequestParam("secret2") String secret2,@RequestParam("secret3") String secret3,@RequestParam("answer1") String answer1,@RequestParam("answer2") String answer2,@RequestParam("answer3") String answer3,Model model){
		signingUpUser.setUserSecret1(answer1);
		signingUpUser.setUserSecret2(answer2);
		signingUpUser.setUserSecret3(answer3);
		signingUpUser.setSecret1(secret1);
		signingUpUser.setSecret2(secret2);
		signingUpUser.setSecret3(secret3);
		
		userRepository.save(signingUpUser);

			return "redirect:index";
			}
	  @GetMapping({"/forgotUser/{id}"})
	   public String forgotUser(@PathVariable("id") int id,Model model) {
		  setPage("email");
		  model.addAttribute("page",page);
		  if(id == 0) {
			  setTheId(0);
			  setPage("user");
		  }
		  else {
			  setTheId(1);
			  setPage("pass");
		  }
		return "forgotUser";
	}
	@PostMapping({"/findUser"})
	public String findUser(Model model,@RequestParam("email") String email2) {
		if(userController.getUserByEmail(email2) == null)
		{
			setPage("findUserFail");
			model.addAttribute("page",page);
			return "forgotUser";
		}
		findUser = userController.getUserByEmail(email2);
		if(getTheId() == 0) {
			email.usernameRecovery(findUser);

		}
		 Random rand = new Random();
	        // Generate random integers in range 0 to 2
	        int rand_int1 = rand.nextInt(3);

	        if(rand_int1 == 0)
	        {
	        	setQuestion(findUser.getSecret1());
	        	setAnswer(findUser.getUserSecret1());
	        }
	        if(rand_int1 == 1)
	        {
	        	setQuestion(findUser.getSecret2());
	        	setAnswer(findUser.getUserSecret2());
	        }
	        else
	        {
	        	setQuestion(findUser.getSecret3());
	        	setAnswer(findUser.getUserSecret3());
	        }
	    model.addAttribute("question",question);
		model.addAttribute("page",page);
		return "forgotUser";
	}
	@PostMapping({"/answerQuestion"})
	public String answerQuestion(@RequestParam("answer") String answer2,Model model) {
		if(answer2.equals(getAnswer())) {
			setPage("reset");
			model.addAttribute("page",page);
		}
		else {
			setPage("wrong");
			model.addAttribute("question",getQuestion());
			model.addAttribute("page",page);
		}

		return "forgotUser";
	}
	@PostMapping("/resetPassword")
	public String resetPassword(@RequestParam("pass") String pass,@RequestParam("pass2") String pass2,Model model){
		if(pass.length()<6 || !(pass.equals(pass2))) {
			setPage("resetfail");
			model.addAttribute("page",page);
			return "forgotUser";
		}
		else {
			
			findUser.setPassword(passwordEncoder.encode(pass));
			userRepository.save(findUser);
			setPage("resetSuccess");
			model.addAttribute("page",page);
			return "forgotUser";
			
		}
	}
	   
	    
	
	
	
	
	
	@RequestMapping("/verify")
	public String verifyUser(@RequestParam("username") String name, @RequestParam("verification") String verification,Model model) {
		if(userController.getUserByUsername(name) == null)
		{
			model.addAttribute("message", "Wrong Username!");
			return "emailverification";
		}
		
		User user = userController.getUserByUsername(name);
		if(user.getEnabled())
		{
			model.addAttribute("message", "User already Verified!");
			return "redirect:index";
		}
		if(verification.equals(user.getEmailVerification()))
		{

			model.addAttribute("message", "User Verified successfully!");
			user.setEnabled(true);
			userRepository.save(user);
			return "redirect:index";
		}
		else
		{
			model.addAttribute("message", "Wrong verification code!");
			return "emailverification";
		}
		
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public int getTheId() {
		return theId;
	}

	public void setTheId(int theId) {
		this.theId = theId;
	}
}
