package org.jasig.cas.zk.redis;

import java.util.Collection;

import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("redisTgtTicketRegistry")
public class RedisTgtTicketRegistry implements TicketRegistry {
	
	@Autowired
	private RedisTgtTicketService redisTgtTicketService;
	
	@Override
	public void addTicket(Ticket ticket) {
		redisTgtTicketService.addTickey(ticket);
	}

	@Override
	public <T extends Ticket> T getTicket(String ticketId,
			Class<? extends Ticket> clazz) {
		return null;
	}

	@Override
	public Ticket getTicket(String ticketId) {
		return redisTgtTicketService.getTicket(ticketId);
	}

	@Override
	public boolean deleteTicket(String ticketId) {
		return redisTgtTicketService.deleteTicket(ticketId);
	}

	@Override
	public Collection<Ticket> getTickets() {
		throw new UnsupportedOperationException("getTickets not supported.");
	}

}
