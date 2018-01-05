package org.jasig.cas.logout;

import java.net.URL;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Define a logout request for a service accessed by a user.
 *
 * @author Jerome Leleu
 * @since 4.0.0
 */
public final class DefaultLogoutRequest implements LogoutRequest {

    /** Generated serialVersionUID. */
    private static final long serialVersionUID = -6411421298859045022L;

    /** The service ticket id. */
    private final String ticketId;

    /** The service. */
    private final SingleLogoutService service;

    /** The status of the logout request. */
    private LogoutRequestStatus status = LogoutRequestStatus.NOT_ATTEMPTED;

    private final URL logoutUrl;

    /**
     * Build a logout request from ticket identifier and service.
     * Default status is {@link LogoutRequestStatus#NOT_ATTEMPTED}.
     *
     * @param ticketId the service ticket id.
     * @param service the service.
     * @param logoutUrl the logout url
     */
    public DefaultLogoutRequest(final String ticketId, final SingleLogoutService service, final URL logoutUrl) {
        this.ticketId = ticketId;
        this.service = service;
        this.logoutUrl = logoutUrl;
    }

    @Override
    public LogoutRequestStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(final LogoutRequestStatus status) {
        this.status = status;
    }

    @Override
    public String getTicketId() {
        return ticketId;
    }

    @Override
    public SingleLogoutService getService() {
        return service;
    }

    @Override
    public URL getLogoutUrl() {
        return logoutUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("ticketId", ticketId)
                .append("service", service)
                .append("status", status)
                .toString();
    }
}
