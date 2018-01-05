package org.jasig.cas.zk.redis;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component("redisLoginTicketService")
public class RedisLoginTicketService {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	private static final int TIME_OUT = 30;
	private static final String TICKET_FLAG = "login_ticket";
	
	public void addTickey(String loginTicket){
		if(StringUtils.isNotBlank(loginTicket)){
			try {
				stringRedisTemplate.opsForValue().set(RedisCommonKey.getLoginTicketKey(loginTicket), TICKET_FLAG,TIME_OUT,TimeUnit.MINUTES);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isValid(String loginTicketId) {
		if(StringUtils.isBlank(loginTicketId)){
			return false;
		}
		try {
			String str = stringRedisTemplate.opsForValue().get(RedisCommonKey.getLoginTicketKey(loginTicketId));
			if(StringUtils.isNotBlank(str)){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void deleteTicket(String loginTicketId) {
		if(StringUtils.isNotBlank(loginTicketId)){
			try {
				stringRedisTemplate.delete(RedisCommonKey.getLoginTicketKey(loginTicketId));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
