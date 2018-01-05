package org.jasig.cas.zk.redis;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicketImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component("redisTgtTicketService")
public class RedisTgtTicketService {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	private static final int TIME_OUT = 30;
	
	public void addTickey(Ticket tgtTicket){
		if(tgtTicket!=null&&tgtTicket instanceof TicketGrantingTicketImpl){
			TicketGrantingTicketImpl tgtEntity = (TicketGrantingTicketImpl)tgtTicket;
			try {
				stringRedisTemplate.opsForValue().set(RedisCommonKey.getTgtTicketKey(tgtTicket.getId()), new ObjectMapper().writeValueAsString(tgtEntity),TIME_OUT, TimeUnit.MINUTES);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public Ticket getTicket(String tgtTicketId) {
		if(StringUtils.isBlank(tgtTicketId)){
			return null;
		}
		try {
			String str = stringRedisTemplate.opsForValue().get(RedisCommonKey.getTgtTicketKey(tgtTicketId));
			if(StringUtils.isNotBlank(str)){
				return new ObjectMapper().readValue(str, TicketGrantingTicketImpl.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean deleteTicket(String tgtTicketId) {
		if(StringUtils.isBlank(tgtTicketId)){
			return false;
		}
		try {
			stringRedisTemplate.delete(RedisCommonKey.getTgtTicketKey(tgtTicketId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
}
