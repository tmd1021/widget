package edu.sru.cpsc.webshopping.controller;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.apiclub.captcha.Captcha;
import edu.sru.cpsc.webshopping.controller.billing.DirectDepositController;
import edu.sru.cpsc.webshopping.controller.billing.PaymentDetailsController;
import edu.sru.cpsc.webshopping.domain.billing.DirectDepositDetails;
import edu.sru.cpsc.webshopping.domain.billing.PaymentDetails;
import edu.sru.cpsc.webshopping.domain.user.Message;
import edu.sru.cpsc.webshopping.domain.user.Statistics;
import edu.sru.cpsc.webshopping.domain.user.Statistics.Category;
import edu.sru.cpsc.webshopping.domain.user.User;
import edu.sru.cpsc.webshopping.domain.widgets.Widget;
import edu.sru.cpsc.webshopping.repository.user.UserRepository;
import edu.sru.cpsc.webshopping.secure.CaptchaUtil;

@RestController
public class UserController {
	private User Currently_Logged_In;
	private UserRepository userRepository;
	private StatisticsDomainController statControl;
	private PaymentDetailsController paymentDetailsController;
	private DirectDepositController directDepositDetailsController;
	@PersistenceContext
	private EntityManager entityManager;
	
	UserController(UserRepository userRepository,StatisticsDomainController statControl,
			PaymentDetailsController paymentDetailsController, DirectDepositController directDepositDetailsController) {
		this.userRepository = userRepository;
		this.statControl = statControl;
		this.paymentDetailsController = paymentDetailsController;
		this.directDepositDetailsController = directDepositDetailsController;
	}
	
	/**
	 * User of Currently_Logged_In_User in database has Widget added to their list of wishlisted items
	 * @param widget Widget to wishlist
	 * @exception IllegalStateException if no user is logged in
	 * @exception IllegalArgumentException if the wishlisted Widget is not found in the database
	 */
	@PostMapping("/add-to-wishlist")
	@Transactional
	public void addToWishlist(@Validated Widget widget) {
		if (Currently_Logged_In == null) {
			throw new IllegalStateException("User not logged in when attempting to add new Widget to wishlist.");
		}
		User user = entityManager.find(User.class, Currently_Logged_In.getId());
		Widget addedWidget = entityManager.find(Widget.class, widget.getId());
		if (addedWidget == null) {
			throw new IllegalArgumentException("Widget pass to addToWishlist not found in database.");
		}
		user.getWishlistedWidgets().add(addedWidget);
		userRepository.save(user);
	}
	
	/**
	 * Saves or updates the current PaymentDetails of the user
	 * PaymentDetails is encoded using BCryptPasswordEncoder and then stored in the database
	 * @param details new PaymentDetails to save
	 * @exception IllegalStateException is thrown if the user is not logged in
	 */
	@PostMapping("/update-payment-details") 
	@Transactional
	public void updatePaymentDetails(@Validated PaymentDetails details) {
		System.out.println("update payment details database function called");
		if (Currently_Logged_In == null) {
			throw new IllegalStateException("Error updating payment details: User not logged in.");
		}
		User user = entityManager.find(User.class, Currently_Logged_In.getId());
		// Encode fields
		details.setCardholderName(passwordEncoder.encode(details.getCardholderName()));
		details.setCardNumber(passwordEncoder.encode(details.getCardNumber()));
		details.setCardType(passwordEncoder.encode(details.getCardType()));
		details.setExpirationDate(passwordEncoder.encode(details.getExpirationDate()));
		details.setPostalCode(passwordEncoder.encode(details.getPostalCode()));
		details.setSecurityCode(passwordEncoder.encode(details.getSecurityCode()));
		// No assigned details - add to user
		if (user.getPaymentDetails() == null) {
			entityManager.persist(details);
			user.setPaymentDetails(details);
			entityManager.merge(user);
		}
		else {
			PaymentDetails curr = entityManager.find(PaymentDetails.class, user.getPaymentDetails().getId());
			curr.transferFields(details);
			user.setPaymentDetails(curr);
			entityManager.merge(user);
		}
		Currently_Logged_In.setPaymentDetails(details);
	}
	
	/**
	 * Deletes the PaymentDetails associated with the user
	 */
	@Transactional
	public void deletePaymentDetails()
	{
		User user = entityManager.find(User.class, Currently_Logged_In.getId());
		PaymentDetails details = user.getPaymentDetails();
		user.setPaymentDetails(null);
		Currently_Logged_In.setPaymentDetails(null);
		userRepository.save(user);
		paymentDetailsController.deletePaymentDetails(details);
	}
	
	/**
	 * Deletes the DirectDepositDetails associated with the user
	 */
	@Transactional
	public void deleteDirectDepositDetails() {
		User user = entityManager.find(User.class, Currently_Logged_In.getId());
		DirectDepositDetails details = user.getDirectDepositDetails();
		user.setDirectDepositDetails(null);
		Currently_Logged_In.setDirectDepositDetails(null);
		userRepository.save(user);
		directDepositDetailsController.deleteDirectDepositDetails(details);
	}
	
	/**
	 * Returns true if the passed details matches the currently logged in User's PaymentDetails
	 * Returns false if they do not match
	 * The comparison is done using the BCryptPasswordEncoder matches functionality
	 * @param details the raw PaymentDetails to compare with the stored PaymentDetails
	 * @return boolean
	 * @exception IllegalStateException is thrown if the user is not logged in
	 * @exception IllegalStateException is thrown if the user has no saved PaymentDetails
	 */
	@GetMapping("/verify-payment-details")
	@Transactional
	public boolean verifyPaymentDetails(@Validated PaymentDetails details) {
		if (Currently_Logged_In == null) {
			throw new IllegalStateException("No logged in user when attempting to verify payment details.");
		}
		else if (Currently_Logged_In.getPaymentDetails() == null) {
			throw new IllegalStateException("User does not have an added PaymentDetails for verifying.");
		}
		PaymentDetails encodedDetails = Currently_Logged_In.getPaymentDetails();
		boolean isValid = true;
		isValid = isValid && passwordEncoder.matches(details.getCardholderName(), encodedDetails.getCardholderName());
		isValid = isValid && passwordEncoder.matches(details.getCardNumber(), encodedDetails.getCardNumber());
		isValid = isValid && passwordEncoder.matches(details.getCardType(), encodedDetails.getCardType());
		isValid = isValid && passwordEncoder.matches(details.getExpirationDate(), encodedDetails.getExpirationDate());
		isValid = isValid && passwordEncoder.matches(details.getPostalCode(), encodedDetails.getPostalCode());
		isValid = isValid && passwordEncoder.matches(details.getSecurityCode(), encodedDetails.getSecurityCode());
		return isValid;
	}
	
	/**
	 * Saves or updates the current DirectDepositDetails of the user
	 * DirectDepositDetails is encoded using BCryptPasswordEncoder and then stored in the database
	 * @param details new DirectDepositDetails to save
	 * @exception IllegalStateException is thrown if the user is not logged in
	 */
	@PostMapping("/update-direct-deposit-details") 
	@Transactional
	public void updateDirectDepositDetails(@Validated DirectDepositDetails details) {
		System.out.println("update direct deposit details database function called");
		if (Currently_Logged_In == null) {
			throw new IllegalStateException("Error updating payment details: User not logged in.");
		}
		User user = entityManager.find(User.class, Currently_Logged_In.getId());
		// Encode fields
		details.setAccountholderName(passwordEncoder.encode(details.getAccountholderName()));
		details.setAccountNumber(passwordEncoder.encode(details.getAccountNumber()));
		details.setRoutingNumber(passwordEncoder.encode(details.getRoutingNumber()));
		details.setBankName(passwordEncoder.encode(details.getBankName()));
		// No assigned details - add to user
		if (user.getDirectDepositDetails() == null) {
			entityManager.persist(details);
			user.setDirectDepositDetails(details);
			entityManager.merge(user);
		}
		else {
			DirectDepositDetails curr = entityManager.find(DirectDepositDetails.class, user.getDirectDepositDetails().getId());
			curr.transferFields(details);
			user.setDirectDepositDetails(curr);
			entityManager.merge(user);
		}
		Currently_Logged_In.setDirectDepositDetails(details);
	}
	
	/**
	 * Returns true if the passed details matches the currently logged in User's DirectDepositDetails
	 * Returns false if they do not match
	 * The comparison is done using the BCryptPasswordEncoder matches functionality
	 * @param details the raw PaymentDetails to compare with the stored PaymentDetails
	 * @return boolean
	 * @exception IllegalStateException is thrown if the user is not logged in
	 * @exception IllegalStateException is thrown if the user has no saved PaymentDetails
	 */
	@GetMapping("/verify-direct-deposit-details")
	@Transactional
	public boolean verifyDirectDepositDetails(@Validated DirectDepositDetails details) {
		if (Currently_Logged_In == null) {
			throw new IllegalStateException("No logged in user when attempting to verify payment details.");
		}
		else if (Currently_Logged_In.getDirectDepositDetails() == null) {
			throw new IllegalStateException("User does not have an added PaymentDetails for verifying.");
		}
		DirectDepositDetails encodedDetails = Currently_Logged_In.getDirectDepositDetails();
		boolean isValid = true;
		isValid = isValid && passwordEncoder.matches(details.getAccountholderName(), encodedDetails.getAccountholderName());
		isValid = isValid && passwordEncoder.matches(details.getAccountNumber(), encodedDetails.getAccountNumber());
		isValid = isValid && passwordEncoder.matches(details.getRoutingNumber(), encodedDetails.getRoutingNumber());
		return isValid;
	}
	
	// Basic CRUD functions
	@RequestMapping("/get-user") 
	public User getUser(@PathVariable("id") long id, Model model) {
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID passed to find a user"));
		return user;
	}
	@RequestMapping("/get-username") 
	public User getUserByUsername(@PathVariable("username") String name) {
		User user = userRepository.findByUsername(name);
		return user;
	}
	@RequestMapping("/get-email") 
	public User getUserByEmail(@PathVariable("email") String email) {
		User user = userRepository.findByEmail(email);
		return user;
	}

	@RequestMapping("/get-all-users") 
	public Iterable<User> getAllUsers() {
		Iterable<User> users = userRepository.findAll();
		return users;
	}
	@Autowired
	private PasswordEncoder passwordEncoder;
	@PostMapping("/add-user")
	public User addUser(@Valid User user, BindingResult result) {
		// Find specific errors
		// Verifies that the giver user.creationDate is in a valid format
		//LocalDate.parse(user.getCreationDate());
		// Find other errors determined by the result
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		Statistics stats = new Statistics(Category.ACCOUNTCREATION,1);
		statControl.addStatistics(stats);
		return userRepository.save(user);
	}
	
	@PostMapping("/edit-user/{id}/{returnPath}")
	public void updateUser(@PathVariable("id") long id, @PathVariable("returnPath") String returnPath,
							 @Validated User user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			// Temporary - just returns to the website index
			throw new IllegalArgumentException();
		}
		
		userRepository.save(user);
	}
	
	@PostMapping("/delete-user/{id}") 
	public void deleteUser(@PathVariable("id") long id, Model model) {
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID passed to delete a User"));
		userRepository.delete(user);
	}
	// End of basic CRUD functions

	public User getCurrently_Logged_In() {
		return Currently_Logged_In;
	}

	public void setCurrently_Logged_In(User currently_Logged_In) {
		Currently_Logged_In = currently_Logged_In;
	}
	
	void getCaptcha(User user) {
		Captcha captcha = CaptchaUtil.createCaptcha(240, 70);
		user.setHiddenCaptcha(captcha.getAnswer());
		user.setCaptcha(""); // value entered by the User
		user.setRealCaptcha(CaptchaUtil.encodeCaptcha(captcha));
		
	}
}
