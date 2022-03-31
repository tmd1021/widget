/*package edu.sru.cpsc.webshopping.secure;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import edu.sru.cpsc.webshopping.controller.UserController;
import edu.sru.cpsc.webshopping.repository.user.UserRepository;

@Service
public class UniqueLoginValidator implements ConstraintValidator<UniqueLogin, String> {
	
	    private UserController userController;
	    private UserRepository userRepository;
	    
	    public UniqueLoginValidator() {
	    }
	
	    public void storeController(UserController userController)
	    {
	    	this.userController = userController;
	    }
	    @Override
	    public void initialize(UniqueLogin constraint) {
	    }
	   
	    @Override
	    public boolean isValid(String login, ConstraintValidatorContext context) {
	    	
	    	if(this.userController == null) {
	    		storeController(userController);
	    		System.out.println(userController.getUserByUsername(login).getUsername());
	    		return true;
	    	}
	       if(userController.getUserByUsername(login) == null) {
	    	   System.out.println("return true");
	       	return true;
	       }
	        boolean valid = (userController.getUserByUsername(login).getUsername() == login);
	        System.out.println("value: " + valid);
			return valid;
	    }
	 


}
	
	
*/