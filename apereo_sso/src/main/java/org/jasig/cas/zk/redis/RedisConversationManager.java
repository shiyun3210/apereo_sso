package org.jasig.cas.zk.redis;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.webflow.conversation.Conversation;
import org.springframework.webflow.conversation.ConversationException;
import org.springframework.webflow.conversation.ConversationId;
import org.springframework.webflow.conversation.ConversationManager;
import org.springframework.webflow.conversation.ConversationParameters;
import org.springframework.webflow.conversation.impl.BadlyFormattedConversationIdException;
import org.springframework.webflow.conversation.impl.SimpleConversationId;

public class RedisConversationManager implements ConversationManager {
	
	private int conversationIdSequence;
	
	@NotNull
	@Autowired
	@Qualifier("redisConversationService")
	private RedisConversationService redisConversationService;
	
	
	@Override
	public Conversation beginConversation(
			ConversationParameters conversationParameters)
			throws ConversationException {
		return createConversation(conversationParameters);
	}
	
	private Conversation createConversation(ConversationParameters parameters){
		UserDefindConversation conversation = new UserDefindConversation(nextId());
		conversation.putAttribute("name", parameters.getName());
		conversation.putAttribute("caption", parameters.getCaption());
		conversation.putAttribute("description", parameters.getDescription());
		redisConversationService.addConversation(conversation);
		return conversation;
	}
	
	@Override
	public Conversation getConversation(ConversationId id)
			throws ConversationException {
		return redisConversationService.getConversation(id.toString());
	}

	@Override
	public ConversationId parseConversationId(String encodedId)
			throws ConversationException {
		try {
			return new SimpleConversationId(Integer.valueOf(encodedId));
		} catch (NumberFormatException e) {
			throw new BadlyFormattedConversationIdException(encodedId, e);
		}
	}
	
	private ConversationId nextId() {
		return new SimpleConversationId(++conversationIdSequence);
	}
}
