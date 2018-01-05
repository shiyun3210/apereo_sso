package org.jasig.cas.zk.redis;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.webflow.conversation.Conversation;

@Component("redisConversationService")
public class RedisConversationService {
	
	@Autowired
	private RedisTemplate<String, UserDefindConversation> redisTemplate;
	
	public void addConversation(Conversation userDefindConversation){
		if(userDefindConversation!=null&&userDefindConversation instanceof UserDefindConversation ){
			try {
				redisTemplate.opsForValue().set(RedisCommonKey.getCasConversationKey(userDefindConversation.getId().toString()), (UserDefindConversation)userDefindConversation,30,TimeUnit.MINUTES);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public UserDefindConversation getConversation(String conversationId){
		if(StringUtils.isNotBlank(conversationId)){
			try {
				return redisTemplate.opsForValue().get(RedisCommonKey.getCasConversationKey(conversationId));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public void delConversation(String conversationId){
		if(StringUtils.isNotBlank(conversationId)){
			try {
				redisTemplate.delete(RedisCommonKey.getCasConversationKey(conversationId));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
