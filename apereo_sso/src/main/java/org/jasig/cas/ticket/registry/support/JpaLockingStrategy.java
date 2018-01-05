package org.jasig.cas.ticket.registry.support;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;

/**
 * JPA 2.0 implementation of an exclusive, non-reentrant lock.
 *
 * @author Marvin S. Addison
 * @since 3.0.0
 */
@Component("jpaLockingStrategy")
public class JpaLockingStrategy implements LockingStrategy {

    /** Default lock timeout is 1 hour. */
    public static final int DEFAULT_LOCK_TIMEOUT = 3600;

    /** Transactional entity manager from Spring context. */
    @NotNull
    @PersistenceContext(unitName = "ticketEntityManagerFactory")
    protected EntityManager entityManager;

    /** Logger instance. */
    private final transient Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Application identifier that identifies rows in the locking table,
     * each one of which may be for a different application or usage within
     * a single application.
     */
    @NotNull
    @Value(("${database.cleaner.appid:cas-ticket-registry-cleaner}"))
    private String applicationId;

    /** Unique identifier that identifies the client using this lock instance. */
    @NotNull
    @Value(("${host.name:cas01.example.org}"))
    private String uniqueId;

    /** Amount of time in seconds lock may be held. */
    private int lockTimeout = DEFAULT_LOCK_TIMEOUT;


    /**
     * @param  id  Application identifier that identifies a row in the lock
     *             table for which multiple clients vie to hold the lock.
     *             This must be the same for all clients contending for a
     *             particular lock.
     */
    public void setApplicationId(final String id) {
        this.applicationId = id;
    }


    /**
     * @param  id  Identifier used to identify this instance in a row of the
     *             lock table.  Must be unique across all clients vying for
     *             locks for a given application ID.
     */
    public void setUniqueId(final String id) {
        this.uniqueId = id;
    }


    /**
     * @param  seconds  Maximum amount of time in seconds lock may be held.
     *                  A value of zero indicates that locks are held indefinitely.
     *                  Use of a reasonable timeout facilitates recovery from node failures,
     *                  so setting to zero is discouraged.
     */
    public void setLockTimeout(final int seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException("Lock timeout must be non-negative.");
        }
        this.lockTimeout = seconds;
    }


    @Override
    public boolean acquire() {
        final Lock lock;
        try {
            lock = entityManager.find(Lock.class, applicationId, LockModeType.PESSIMISTIC_WRITE);
        } catch (final PersistenceException e) {
            logger.debug("{} failed querying for {} lock.", uniqueId, applicationId, e);
            return false;
        }

        boolean result = false;
        if (lock != null) {
            final DateTime expDate = new DateTime(lock.getExpirationDate());
            if (lock.getUniqueId() == null) {
                // No one currently possesses lock
                logger.debug("{} trying to acquire {} lock.", uniqueId, applicationId);
                result = acquire(entityManager, lock);
            } else if (new DateTime().isAfter(expDate)) {
                // Acquire expired lock regardless of who formerly owned it
                logger.debug("{} trying to acquire expired {} lock.", uniqueId, applicationId);
                result = acquire(entityManager, lock);
            }
        } else {
            // First acquisition attempt for this applicationId
            logger.debug("Creating {} lock initially held by {}.", applicationId, uniqueId);
            result = acquire(entityManager, new Lock());
        }
        return result;
    }

    @Override
    public void release() {
        final Lock lock = entityManager.find(Lock.class, applicationId, LockModeType.PESSIMISTIC_WRITE);

        if (lock == null) {
            return;
        }
        // Only the current owner can release the lock
        final String owner = lock.getUniqueId();
        if (uniqueId.equals(owner)) {
            lock.setUniqueId(null);
            lock.setExpirationDate(null);
            logger.debug("Releasing {} lock held by {}.", applicationId, uniqueId);
            entityManager.persist(lock);
        } else {
            throw new IllegalStateException("Cannot release lock owned by " + owner);
        }
    }

    /**
     * Gets the current owner of the lock as determined by querying for
     * uniqueId.
     *
     * @return  Current lock owner or null if no one presently owns lock.
     */
    public String getOwner() {
        final Lock lock = entityManager.find(Lock.class, applicationId);
        if (lock != null) {
            return lock.getUniqueId();
        }
        return null;
    }

    @Override
    public String toString() {
        return uniqueId;
    }

    /**
     * Acquire the lock object.
     *
     * @param em the em
     * @param lock the lock
     * @return true, if successful
     */
    private boolean acquire(final EntityManager em, final Lock lock) {
        lock.setUniqueId(uniqueId);
        if (lockTimeout > 0) {
            final Calendar cal = Calendar.getInstance();
            cal.add(Calendar.SECOND, lockTimeout);
            lock.setExpirationDate(cal.getTime());
        } else {
            lock.setExpirationDate(null);
        }
        boolean success = false;
        try {
            if (lock.getApplicationId() != null) {
                em.merge(lock);
            } else {
                lock.setApplicationId(applicationId);
                em.persist(lock);
            }
            success = true;
        } catch (final PersistenceException e) {
            success = false;
            if (logger.isDebugEnabled()) {
                logger.debug("{} could not obtain {} lock.", uniqueId, applicationId, e);
            } else {
                logger.info("{} could not obtain {} lock.", uniqueId, applicationId);
            }
        }
        return success;
    }


    /**
     * Describes a database lock.
     *
     * @author Marvin S. Addison
     *
     */
    @Entity
    @Table(name = "locks")
    private static class Lock {
        /** column name that holds application identifier. */
        @Id
        @Column(name="application_id")
        private String applicationId;

        /** Database column name that holds unique identifier. */
        @Column(name="unique_id")
        private String uniqueId;

        /** Database column name that holds expiration date. */
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name="expiration_date")
        private Date expirationDate;

        /**
         * @return the applicationId
         */
        public String getApplicationId() {
            return applicationId;
        }

        /**
         * @param applicationId the applicationId to set
         */
        public void setApplicationId(final String applicationId) {
            this.applicationId = applicationId;
        }

        /**
         * @return the uniqueId
         */
        public String getUniqueId() {
            return uniqueId;
        }

        /**
         * @param uniqueId the uniqueId to set
         */
        public void setUniqueId(final String uniqueId) {
            this.uniqueId = uniqueId;
        }

        /**
         * @return the expirationDate
         */
        public DateTime getExpirationDate() {
            return this.expirationDate == null ? null : new DateTime(this.expirationDate);
        }

        /**
         * @param expirationDate the expirationDate to set
         */
        public void setExpirationDate(final Date expirationDate) {
            this.expirationDate = expirationDate;
        }
    }
}
