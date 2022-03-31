package edu.sru.cpsc.webshopping.controller;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.sru.cpsc.webshopping.domain.market.Transaction;
import edu.sru.cpsc.webshopping.domain.user.Message;
import edu.sru.cpsc.webshopping.domain.user.User;
import edu.sru.cpsc.webshopping.repository.message.MessageRepository;



@RestController
public class MessageDomainController {
	private MessageRepository Messagerepository;
	@PersistenceContext
	private EntityManager entityManager;
	
	MessageDomainController(MessageRepository Messagerepository) {
		this.Messagerepository = Messagerepository;
	}
	

	@RequestMapping("/get-Message/{id}")
	@Transactional
	public Message getMessage(@PathVariable("id") long id) {
	
		Message Message = entityManager.find(Message.class, id);
		return Message;
	}
	

	@RequestMapping({"/get-all-boxes"})
	public Iterable<Message> getAllBoxes()
	{
		Iterable<Message> boxes = Messagerepository.findAll();
		return boxes;
	}
	

	@Transactional
	@PostMapping("/add-Message") 
	public void addMessage(@Validated Message Message, BindingResult result) {
		if (result.hasErrors()) {
			throw new IllegalArgumentException("Invalid Message");
		}
		User owner = entityManager.find(User.class, Message.getOwner().getId());
		User receiver = entityManager.find(User.class, Message.getReceiver().getId());
		
		Message.setOwner(owner);
		Message.setReceiver(receiver);
		
		Messagerepository.save(Message);
	}
	@PostMapping("/add-Message-no-binding") 
	public void addMessage(@Validated Message Message) {
	
		User owner = entityManager.find(User.class, Message.getOwner().getId());
		User receiver = entityManager.find(User.class, Message.getReceiver().getId());
		
		Message.setOwner(owner);
		Message.setReceiver(receiver);
		
		Messagerepository.save(Message);
	}
	@Transactional
	@PostMapping("/add-Message-to-Trash") 
	public void addMessageToTrash(@Validated Message Message,User user) {

		Message.setTrashOwner(user);
		if(user.getUsername().equals(Message.getOwner().getUsername()) && user.getUsername().equals(Message.getReceiver().getUsername()))
		{
			
			Message.setOwner(null);
			Message.setReceiver(null);
			Messagerepository.save(Message);
			return;
		}
		if(user.getUsername().equals(Message.getOwner().getUsername()))
		{
			
			Message.setOwner(null);
			Messagerepository.save(Message);
			return;
		}
		else {
			Message.setReceiver(null);
			Messagerepository.save(Message);
			return;
		}

	}
	@Transactional
	@PostMapping("/add-Message-to-Spam") 
	public void addMessageToSpam(@Validated Message Message,User user,String category,String feedback) {

		Message.setTicketCategory(category);
		Message.setUserFeedback(feedback);
		Message.setSpamReporter(user.getUsername());
		if(user.getUsername().equals(Message.getOwner().getUsername()) && user.getUsername().equals(Message.getReceiver().getUsername()))
		{
			
			Message.setOwner(null);
			Message.setReceiver(null);
			Messagerepository.save(Message);
			return;
		}
		if(user.getUsername().equals(Message.getOwner().getUsername()))
		{
			
			Message.setOwner(null);
			Messagerepository.save(Message);
			return;
		}
		else {
			Message.setReceiver(null);
			Messagerepository.save(Message);
			return;
		}

	}
	

	@Transactional
	@PostMapping("/edit-Message")
	public void editMessage(@Validated Message updatedMessage) {
		Optional<Message> dbListing = Messagerepository.findById(updatedMessage.getId());
		dbListing.get().setContent(updatedMessage.getContent());
		dbListing.get().setSender(updatedMessage.getSender());
		dbListing.get().setMsgDate();
		dbListing.get().setSubject(updatedMessage.getSubject());
		dbListing.get().setReceiverName(updatedMessage.getReceiverName());
		// Ensure that we are referring to persistent entities
		dbListing.get().setOwner(entityManager.find(User.class, updatedMessage.getOwner().getId()));
		dbListing.get().setReceiver(entityManager.find(User.class, updatedMessage.getReceiver().getId()));

		Messagerepository.save(dbListing.get());
	}
	
	@Transactional
	@GetMapping("get-users-Outbox")
	public Message[] getUserOutbox(@Validated User user) {
		return Messagerepository.findByOwner(user);
	}
	@Transactional
	@GetMapping("get-users-Messages")
	public Message[] getUserInbox(@Validated User user) {
		return Messagerepository.findByReceiver(user);
	}
	@Transactional
	@GetMapping("get-users-Trash")
	public Message[] getUserTrash(@Validated User user) {
		return Messagerepository.findByTrashOwner(user);
	}
	

	@PostMapping("/delete-Message/{id}")
	public void deleteMessage(@PathVariable long id) {
		Messagerepository.deleteById(id);
	}
}
