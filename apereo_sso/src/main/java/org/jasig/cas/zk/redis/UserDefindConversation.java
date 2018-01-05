package org.jasig.cas.zk.redis;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.webflow.conversation.Conversation;
import org.springframework.webflow.conversation.ConversationId;
import org.springframework.webflow.conversation.ConversationLockException;

public class UserDefindConversation implements Conversation,Serializable{
	
	
	private ConversationId id;
	
	private Map<Object,Object> attribute;
	
	
	public UserDefindConversation(ConversationId id){
		this.id = id;
		attribute = new HashMap<Object,Object>();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public ConversationId getId() {
		return this.id;
	}

	@Override
	public void lock() throws ConversationLockException {
		
	}

	@Override
	public Object getAttribute(Object name) {
		return this.attribute.get(name);
	}

	@Override
	public void putAttribute(Object name, Object value) {
		this.attribute.put(name, value);
	}

	@Override
	public void removeAttribute(Object name) {
		this.attribute.remove(name);
	}

	@Override
	public void end() {
		
	}

	@Override
	public void unlock() {
		
	}
	
	public void createConversation(){
		
	}

}
