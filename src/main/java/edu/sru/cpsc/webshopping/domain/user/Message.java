package edu.sru.cpsc.webshopping.domain.user;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import edu.sru.cpsc.webshopping.controller.MessageDomainController;
import edu.sru.cpsc.webshopping.controller.UserController;
import edu.sru.cpsc.webshopping.repository.message.MessageRepository;
import edu.sru.cpsc.webshopping.repository.user.UserRepository;

@Entity
public class Message {
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private long id;
@ManyToOne
private User trashOwner;
@ManyToOne
private User owner;
@ManyToOne
private User receiver;
private String content;
private String sender;
private String msgDate;
private String subject;
private String receiverName;
//These are for when a message is sent to spam
private String spamReporter;
private String reviewerName;
private String ticketCategory;
private String userFeedback;
private Boolean isTicket = false;
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public User getTrashOwner() {
	return trashOwner;
}
public void setTrashOwner(User trashOwner) {
	this.trashOwner = trashOwner;
}
public User getOwner() {
	return owner;
}
public void setOwner(User owner) {
	this.owner = owner;
}
public User getReceiver() {
	return receiver;
}
public void setReceiver(User receiver) {
	this.receiver = receiver;
}
public String getReceiverName() {
	return receiverName;
}
public void setReceiverName(String receiverName) {
	this.receiverName = receiverName;
}
public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}
public String getSender() {
	return sender;
}
public void setSender(String sender) {
	this.sender = sender;
}
public String getMsgDate() {
	return msgDate;
}
public void setMsgDate() {
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd HH:mm");  
	LocalDateTime now = LocalDateTime.now();    
	this.msgDate = dtf.format(now);
}
public String getSubject() {
	return subject;
}
public void setSubject(String subject) {
	this.subject = subject;
}
public String getReviewerName() {
	return reviewerName;
}
public void setReviewerName(String reviewerName) {
	this.reviewerName = reviewerName;
}
public String getTicketCategory() {
	return ticketCategory;
}
public void setTicketCategory(String ticketCategory) {
	this.ticketCategory = ticketCategory;
}
public String getUserFeedback() {
	return userFeedback;
}
public void setUserFeedback(String userFeedback) {
	this.userFeedback = userFeedback;
}
public String getSpamReporter() {
	return spamReporter;
}
public void setSpamReporter(String spamReporter) {
	this.spamReporter = spamReporter;
}
public Boolean getIsTicket() {
	return isTicket;
}
public void setIsTicket(Boolean isTicket) {
	this.isTicket = isTicket;
}
}
