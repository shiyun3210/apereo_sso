package org.jasig.cas.zk.redis;

import java.util.Collection;

import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("redisServiceTicketRegistry")
public class RedisServiceTicketRegistry implements TicketRegistry {
	
	@Autowired
	private RedisServiceTicketService redisTickeyService;
	
	@Override
	public void addTicket(Ticket ticket) {
		redisTickeyService.addTickey(ticket);
	}

	@Override
	public <T extends Ticket> T getTicket(String ticketId,
			Class<? extends Ticket> clazz) {
		redisTickeyService.getTicket(ticketId);
		return null;
	}

	@Override
	public ServiceTicket getTicket(String ticketId) {
		return redisTickeyService.getTicket(ticketId);
	}

	@Override
	public boolean deleteTicket(String ticketId) {
		return redisTickeyService.deleteTicket(ticketId);
	}

	@Override
	public Collection<Ticket> getTickets() {
		throw new UnsupportedOperationException("getTickets not supported.");
	}

}
