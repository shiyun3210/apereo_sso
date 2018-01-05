package org.jasig.cas.web.flow;

import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.jasig.cas.logout.LogoutManager;
import org.jasig.cas.logout.LogoutRequest;
import org.jasig.cas.logout.LogoutRequestStatus;
import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * Logout action for front SLO : find the next eligible service and perform front logout.
 *
 * @author Jerome Leleu
 * @since 4.0.0
 */
@Component("frontChannelLogoutAction")
public final class FrontChannelLogoutAction extends AbstractLogoutAction {
    /** Defines the default logout parameter for requests. */
    public static final String DEFAULT_LOGOUT_PARAMETER = "SAMLRequest";

    /** Defines the parameter name that is passed to the flow which contains the logout request. */
    public static final String DEFAULT_FLOW_ATTRIBUTE_LOGOUT_URL = "logoutUrl";

    private static final Logger LOGGER = LoggerFactory.getLogger(FrontChannelLogoutAction.class);

    private String logoutRequestParameter = DEFAULT_LOGOUT_PARAMETER;

    @NotNull
    private final LogoutManager logoutManager;

    /**
     * Build from the logout manager.
     *
     * @param logoutManager a logout manager.
     */
    @Autowired
    public FrontChannelLogoutAction(@Qualifier("logoutManager") final LogoutManager logoutManager) {
        this.logoutManager = logoutManager;
    }

    @Override
    protected Event doInternalExecute(final HttpServletRequest request, final HttpServletResponse response,
            final RequestContext context) throws Exception {

        final List<LogoutRequest> logoutRequests = WebUtils.getLogoutRequests(context);
        final Integer startIndex = getLogoutIndex(context);
        if (logoutRequests != null) {
            for (int i = startIndex; i < logoutRequests.size(); i++) {
                final LogoutRequest logoutRequest = logoutRequests.get(i);
                if (logoutRequest.getStatus() == LogoutRequestStatus.NOT_ATTEMPTED) {
                    // assume it has been successful
                    logoutRequest.setStatus(LogoutRequestStatus.SUCCESS);

                    // save updated index
                    putLogoutIndex(context, i + 1);

                    final String logoutUrl = logoutRequest.getLogoutUrl().toExternalForm();
                    LOGGER.debug("Using logout url [{}] for front-channel logout requests", logoutUrl);

                    final String logoutMessage = logoutManager.createFrontChannelLogoutMessage(logoutRequest);
                    LOGGER.debug("Front-channel logout message to send under [{}] is [{}]",
                            this.logoutRequestParameter, logoutMessage);

                    // redirect to application with SAML logout message
                    final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(logoutUrl);
                    builder.queryParam(this.logoutRequestParameter, URLEncoder.encode(logoutMessage, "UTF-8"));

                    return result(REDIRECT_APP_EVENT, DEFAULT_FLOW_ATTRIBUTE_LOGOUT_URL, builder.build().toUriString());
                }
            }
        }

        // no new service with front-channel logout -> finish logout
        return new Event(this, FINISH_EVENT);
    }

    public LogoutManager getLogoutManager() {
        return logoutManager;
    }

    public void setLogoutRequestParameter(final String logoutRequestParameter) {
        this.logoutRequestParameter = logoutRequestParameter;
    }
}
