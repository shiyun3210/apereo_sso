package org.jasig.cas.ticket.support;

import org.jasig.cas.ticket.TicketState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Expiration policy that is based on a certain time period for a ticket to
 * exist.
 * <p>
 * The expiration policy defined by this class is one of inactivity.  If you are inactive for the specified
 * amount of time, the ticket will be expired.
 *
 * @author Scott Battaglia
 * @since 3.0.0
 */
@Component("timeoutExpirationPolicy")
public final class TimeoutExpirationPolicy extends AbstractCasExpirationPolicy {

    /** Serialization support. */
    private static final long serialVersionUID = -7636642464326939536L;

    /** The time to kill in milliseconds. */
    @Value("#{${tgt.timeout.maxTimeToLiveInSeconds:28800}*1000L}")
    private final long timeToKillInMilliSeconds;


    /** No-arg constructor for serialization support. */
    private TimeoutExpirationPolicy() {
        this.timeToKillInMilliSeconds = 0;
    }

    /**
     * Instantiates a new timeout expiration policy.
     *
     * @param timeToKillInMilliSeconds the time to kill in milli seconds
     */
    public TimeoutExpirationPolicy(final long timeToKillInMilliSeconds) {
        this.timeToKillInMilliSeconds = timeToKillInMilliSeconds;
    }

    /**
     * Instantiates a new Timeout expiration policy.
     *
     * @param timeToKill the time to kill
     * @param timeUnit the time unit
     */
    public TimeoutExpirationPolicy(final long timeToKill, final TimeUnit timeUnit) {
        this.timeToKillInMilliSeconds = timeUnit.toMillis(timeToKill);
    }

    @Override
    public boolean isExpired(final TicketState ticketState) {
        return (ticketState == null)
            || (System.currentTimeMillis() - ticketState.getLastTimeUsed() >= this.timeToKillInMilliSeconds);
    }
}
