package edu.sru.cpsc.webshopping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.sru.cpsc.webshopping.domain.user.Message;
import edu.sru.cpsc.webshopping.domain.user.User;
import edu.sru.cpsc.webshopping.repository.user.UserRepository;

@Controller
public class MessagePageController {
	private UserController userController;
	private MessageDomainController msgcontrol;
	private EmailController emailController;
	private Message mailbox[];
	private int pageNumber = 1;
	private static int PAGEMULTIPLIER = 20;
	private String page = "home";
	private String page2;
	private int[] id;

	
	public MessagePageController(UserController userController,MessageDomainController msgcontrol,EmailController emailController,UserRepository repo) {
		this.userController = userController;
		this.msgcontrol = msgcontrol;
		this.emailController = emailController;
		
	}
	
	
	
	 @RequestMapping({"/messages"})
	    public String returnMessages(Model model) {
		 

			User user = userController.getCurrently_Logged_In();
			setMailbox(msgcontrol.getUserInbox(user));
			Message[] tempBox = getMailbox();
			Message[] tempBox2 = getMailbox();
			/*setPageNumber((int) Math.floor(tempBox.length/PAGEMULTIPLIER)+1);
			
			if(tempBox2.length < getPageNumber()*PAGEMULTIPLIER) {
				for(int i = (getPageNumber()-1)*PAGEMULTIPLIER; i < tempBox2.length; i++){
					System.out.println(i-(getPageNumber()-1)*PAGEMULTIPLIER);

					tempBox[i-(getPageNumber()-1)*PAGEMULTIPLIER] = tempBox2[i];
				}
				}
				else {
					for(int i = (getPageNumber()-1)*PAGEMULTIPLIER; i < getPageNumber()*PAGEMULTIPLIER; i++){

							tempBox[i-(getPageNumber()-1)*PAGEMULTIPLIER] = tempBox2[i];
					}
							}
			
					*/
					
			
			
					
					
			
			
			setPage("home");
			user.setViewMessage(false);
			model.addAttribute("view",user.getViewMessage());
			model.addAttribute("page",page);
			model.addAttribute("user",user);
			model.addAttribute("mailbox",tempBox );
	
	        return "messages";
	 }
	 @RequestMapping({"/outbox"})
	    public String returnSent(Model model) {
		 User user = userController.getCurrently_Logged_In();
			setMailbox(msgcontrol.getUserOutbox(user));
			Message[] tempBox = getMailbox();
			Message[] tempBox2 = getMailbox();

			setPage("outbox");
			user.setViewMessage(false);
			model.addAttribute("mailbox",tempBox );
			model.addAttribute("view",user.getViewMessage());
			model.addAttribute("page",page);
			model.addAttribute("user",user);

			
	        return "messages";
	    }
	 @RequestMapping({"/trashbox"})
	    public String returnTrash(Model model) {
		 User user = userController.getCurrently_Logged_In();
			setMailbox(msgcontrol.getUserTrash(user));
			Message[] tempBox = getMailbox();
			Message[] tempBox2 = getMailbox();

			setPage("trashBox");
			user.setViewMessage(false);
			model.addAttribute("mailbox",tempBox );
			model.addAttribute("view",user.getViewMessage());
			model.addAttribute("page",page);
			model.addAttribute("user",user);

	        return "messages";
	    }
   @RequestMapping({"/sendmail"})
   public String sendMessage(@RequestParam("recipient") String name, @RequestParam("message") String content,@RequestParam("subject") String subject,Model model) {

		
		User user = userController.getCurrently_Logged_In();
		User receiver = userController.getUserByUsername(name);
			setPage("sent");
			Message[] inbox = msgcontrol.getUserInbox(user);
			setMailbox(msgcontrol.getUserInbox(user));
			Message[] tempBox = getMailbox();
			model.addAttribute("page",page);
			model.addAttribute("user",user);
			model.addAttribute("mailbox",tempBox );
			user.setViewMessage(false);
			model.addAttribute("view",user.getViewMessage());
		    if(userController.getUserByUsername(name) == null) {
		    	setPage("fail");
		    	model.addAttribute("page",page);
		    	return "messages";   	
		       }
		    if(subject.length() == 0)
		    {
		    	subject = "<no subject>";
		    }
		    if(content.length() == 0)
		    {
		    	content = "<no content>";
		    }
			Message message = new Message();
			message.setOwner(user);
			message.setSender(user.getUsername());
			message.setContent(content);
			message.setSubject(subject);
			message.setMsgDate();
			message.setReceiverName(name);
			message.setReceiver(receiver);
			msgcontrol.addMessage(message);
			setMailbox(msgcontrol.getUserInbox(user));
			tempBox = getMailbox();

			model.addAttribute("mailbox",tempBox );
			model.addAttribute("page",page);
			model.addAttribute("user",user);

			emailController.messageEmail(receiver,user,message);
       return "messages";
   }
   @RequestMapping({"/trash"})
   public String sendToTrash(@RequestParam("id") int[] id,Model model) {
	   User user = userController.getCurrently_Logged_In();
System.out.println(id[0]);	
		   for(int i = 0; i < id.length; i++)
		   {
			   
			   Message tempMessage = msgcontrol.getMessage(id[i]);
			   msgcontrol.addMessageToTrash(tempMessage, user);

			   }
		  
	   

	   

return "redirect:messages";
   }
   
   @RequestMapping({"/spamButton"})
   public String spamButton(@RequestParam("id") int[] id,Model model) {
	   setPage2("ticket");
	   User user = userController.getCurrently_Logged_In();
	   setId(id);
	    model.addAttribute("view",user.getViewMessage());
		model.addAttribute("page",page);
		model.addAttribute("page2",page2);

return "messages";
   }
@RequestMapping({"/justSend"})
public String sendGenericTicket(Model model) {
	   setPage2("ThankYou");
	   User user = userController.getCurrently_Logged_In();
	   
	    model.addAttribute("view",user.getViewMessage());
		model.addAttribute("page",page);
		model.addAttribute("page2",page2);

return "messages";
}
@RequestMapping({"/elaborate"})
public String sendUserTicket(Model model) {
	   setPage2("elaborate");
	   User user = userController.getCurrently_Logged_In();
	   
	    model.addAttribute("view",user.getViewMessage());
		model.addAttribute("page",page);
		model.addAttribute("page2",page2);

return "messages";
}
   @RequestMapping({"/spam"})
   public String sendToSpam(@RequestParam("id") int[] id,@RequestParam("category") String category,@RequestParam("feedback") String feedback,Model model) {
	   User user = userController.getCurrently_Logged_In();

	   if(getPage().equals("outbox")) {

		   Message[] outbox = msgcontrol.getUserOutbox(user);
		   for(int i = 0; i < id.length; i++)
		   {
			   
			   Message tempMessage = outbox[id[i]];
			   msgcontrol.addMessageToSpam(tempMessage, user,category,feedback);

			   }
		   }
	   
	   if(getPage().equals("home")) {
		   Message[] inbox = msgcontrol.getUserInbox(user);
		   for(int i = 0; i < id.length; i++)
		   {
			 
			   Message tempMessage = inbox[id[i]];
			   msgcontrol.addMessageToTrash(tempMessage, user);
  
		   }
	   }
	   

return "redirect:messages";
   }
   @GetMapping({"/openMessage/{id}"})
   public String openMessage(@PathVariable("id") int id,Model model) {
		User user = new User();
		user = userController.getCurrently_Logged_In();
		user.setViewMessage(true);
		model.addAttribute("view",user.getViewMessage());
		   
		   System.out.println(id);	

		   			   
		   			   Message tempMessage = msgcontrol.getMessage(id);

		model.addAttribute("msg", tempMessage.getContent());
		model.addAttribute("sentFrom", tempMessage.getSender());
		model.addAttribute("sentDate", tempMessage.getMsgDate());
		model.addAttribute("sentSubject", tempMessage.getSubject());
		model.addAttribute("user", user);
       return "messages";
   }
   @GetMapping({"/closeMessage"})
   public String closeMessage(Model model) {
		User user = new User();
		user = userController.getCurrently_Logged_In();
		user.setViewMessage(false);
		model.addAttribute("view",user.getViewMessage());
       return "redirect:messages";
   }
	
	public String getPage() {
		return page;
	}



	public void setPage(String page) {
		this.page = page;
	}



	public int[] getId() {
		return id;
	}



	public void setId(int[] id) {
		this.id = id;
	}



	public String getPage2() {
		return page2;
	}



	public void setPage2(String page2) {
		this.page2 = page2;
	}



	public Message[] getMailbox() {
		return mailbox;
	}
	public Message getMailboxSingle(int i) {
		return mailbox[i];
	}



	public void setMailbox(Message[] mailbox) {
		this.mailbox = mailbox;
	}


	public int getPageNumber() {
		return pageNumber;
	}



	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
}
