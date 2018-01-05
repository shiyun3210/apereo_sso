package org.jasig.cas.zk.redis;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.ServiceTicketImpl;
import org.jasig.cas.ticket.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component("redisServiceTicketService")
public class RedisServiceTicketService {
	@Autowired
	private RedisTemplate<String,ServiceTicketImpl> stringRedisTemplate;
	
	private static final int TIME_OUT_SECONDS = 10;
	
	public void addTickey(Ticket ticket){
		if(ticket!=null&&ticket instanceof ServiceTicketImpl){
			ServiceTicketImpl serviceTicket = (ServiceTicketImpl)ticket;
			try {
				stringRedisTemplate.opsForValue().set(RedisCommonKey.getServiceTicketKey(ticket.getId()), serviceTicket,TIME_OUT_SECONDS,TimeUnit.SECONDS);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public ServiceTicket getTicket(String ticketId) {
		if(StringUtils.isBlank(ticketId)){
			return null;
		}
		try {
			return stringRedisTemplate.opsForValue().get(RedisCommonKey.getServiceTicketKey(ticketId));
		} catch (Exception e) {
			
		}
		return null;
	}
	
	public boolean deleteTicket(String ticketId) {
		if(ticketId==null){
			return false;
		}
		try {
			stringRedisTemplate.delete(RedisCommonKey.getServiceTicketKey(ticketId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
}
