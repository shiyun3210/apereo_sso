package org.jasig.cas.web.flow;

import javax.validation.constraints.NotNull;

import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.services.RegisteredService;
import org.jasig.cas.services.ServicesManager;
import org.jasig.cas.ticket.UniqueTicketIdGenerator;
import org.jasig.cas.web.support.WebUtils;
import org.jasig.cas.zk.enums.ClientInfoEnum;
import org.jasig.cas.zk.redis.RedisLoginTicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.webflow.execution.RequestContext;


/**
 * Generates the login ticket parameter as described in section 3.5 of the
 * <a href="http://www.jasig.org/cas/protocol">CAS protocol</a>.
 *
 * @author Marvin S. Addison
 * @since 3.4.9
 *
 */
@Component("generateLoginTicketAction")
public class GenerateLoginTicketAction {
    /** 3.5.1 - Login tickets SHOULD begin with characters "LT-". */
    private static final String PREFIX = "LT";

    /** Logger instance. */
    private final transient Logger logger = LoggerFactory.getLogger(getClass());

    @NotNull
    private UniqueTicketIdGenerator ticketIdGenerator;
    
    @NotNull
    @Autowired
    @Qualifier("redisLoginTicketService")
    private RedisLoginTicketService redisLoginTicketService;
    
    @NotNull
    @Autowired
    @Qualifier("servicesManager")
    private ServicesManager servicesManager;

    /**
     * Generate the login ticket.
     *
     * @param context the context
     * @return {@code "generated"}
     */
    public final String generate(final RequestContext context) {
        final String loginTicket = this.ticketIdGenerator.getNewTicketId(PREFIX);
        logger.debug("Generated login ticket {}", loginTicket);
        WebUtils.putLoginTicket(context, loginTicket);
        redisLoginTicketService.addTickey(loginTicket);
        final Service service = WebUtils.getService(context);
        RegisteredService regService = servicesManager.findServiceBy(service);
        if(regService!=null){
        	ClientInfoEnum clientInfo = ClientInfoEnum.getInstance((int)regService.getId());
        	if(clientInfo.getType()==1){
        		return "gylgenerated";
        	}else{
        		return "kjgenerated";
        	}
        }
        return "gylgenerated";
    }

    @Autowired
    public void setTicketIdGenerator(@Qualifier("loginTicketUniqueIdGenerator") final UniqueTicketIdGenerator generator) {
        this.ticketIdGenerator = generator;
    }
}
