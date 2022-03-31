package edu.sru.cpsc.webshopping.repository.message;

import org.springframework.data.repository.CrudRepository;

import edu.sru.cpsc.webshopping.domain.user.Message;
import edu.sru.cpsc.webshopping.domain.user.User;

public interface MessageRepository extends CrudRepository<Message, Long> {
	Message[] findByOwner(User user);
	Message[] findByReceiver(User user);
	Message[] findByTrashOwner(User user);
}
