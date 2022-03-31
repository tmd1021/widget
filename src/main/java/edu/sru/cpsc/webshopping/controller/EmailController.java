package edu.sru.cpsc.webshopping.controller;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.RestController;

import edu.sru.cpsc.webshopping.domain.user.Message;
import edu.sru.cpsc.webshopping.domain.user.User;
@RestController
public class EmailController {
	@Bean
	public JavaMailSender getJavaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("smtp.gmail.com");
	    mailSender.setPort(587);
	    
	    mailSender.setUsername("worldofwidgetsinc@gmail.com");
	    mailSender.setPassword("zxfeppgfuibrisay");
	    
	    
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	    props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

	
	    return mailSender;
	}
	
	public void verificationEmail(User recipient,String code)
	{
		User user = recipient;
		user.setEmailVerification(code);
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setTo(user.getEmail()); 
        message.setSubject("<no-reply> Welcome!"); 
        message.setText("Hello " + user.getUsername() + " please use the following code to verify your account: " + "\n" + code);
        this.getJavaMailSender().send(message);
	}
	public void messageEmail(User recipient,User sender,Message theMessage)
	{
		User user = recipient;
		User messageSender = sender;
		Message msg = theMessage;
		
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setTo(user.getEmail()); 
        message.setSubject("<no-reply> " +messageSender.getUsername() + " sent you a message on World of Widgets"); 
        message.setText("From: " + msg.getOwner().getUsername() + "\n" + "Subject: " + msg.getSubject() + "\n" + msg.getContent());
        this.getJavaMailSender().send(message);
	}
	public void usernameRecovery(User user) {
		 SimpleMailMessage message = new SimpleMailMessage();
		 message.setTo(user.getEmail()); 
	        message.setSubject("<no-reply> Username Recovery"); 
	        message.setText("Your username is: " + user.getUsername() +"\n"+ "If you have received this email in error please contact us immediately.");
	        this.getJavaMailSender().send(message);
		
	}
	}

