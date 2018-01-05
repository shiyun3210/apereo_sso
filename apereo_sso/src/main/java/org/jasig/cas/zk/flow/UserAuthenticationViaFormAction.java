package org.jasig.cas.zk.flow;

import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.CasProtocolConstants;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.web.flow.AuthenticationViaFormAction;
import org.jasig.cas.web.support.WebUtils;
import org.jasig.cas.zk.redis.RedisLoginTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public class UserAuthenticationViaFormAction extends AuthenticationViaFormAction {
	
	private static final String TRANSITION_ID_ERROR = "error";
	@Autowired
	private RedisLoginTicketService redisLoginTicketService;
	
	@Override
 	protected boolean checkLoginTicketIfExists(final RequestContext context) {
		
        final String loginTicketFromFlowScope = WebUtils.getLoginTicketFromFlowScope(context);
        final String loginTicketFromRequest = WebUtils.getLoginTicketFromRequest(context);

        logger.trace("Comparing login ticket in the flow scope [{}] with login ticket in the request [{}]",
                loginTicketFromFlowScope, loginTicketFromRequest);
        return StringUtils.equals(loginTicketFromFlowScope, loginTicketFromRequest);
    }

	@Override
	protected Event returnInvalidLoginTicketEvent(final RequestContext context, final MessageContext messageContext) {
        final String loginTicketFromRequest = WebUtils.getLoginTicketFromRequest(context);
        logger.warn("Invalid login ticket [{}]", loginTicketFromRequest);
        messageContext.addMessage(new MessageBuilder().error().code("error.invalid.loginticket").build());
        return new Event(this,TRANSITION_ID_ERROR);
    }

	@Override
	protected boolean isRequestAskingForServiceTicket(final RequestContext context) {
        final String ticketGrantingTicketId = WebUtils.getTicketGrantingTicketId(context);
        final Service service = WebUtils.getService(context);
        return (StringUtils.isNotBlank(context.getRequestParameters().get(CasProtocolConstants.PARAMETER_RENEW))
                && ticketGrantingTicketId != null
                && service != null);
    }
}
