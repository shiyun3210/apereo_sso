package org.jasig.cas.support.saml.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.jasig.cas.ticket.UniqueTicketIdGenerator;
import org.opensaml.saml.saml1.binding.artifact.SAML1ArtifactType0001;
import org.opensaml.saml.saml2.binding.artifact.SAML2ArtifactType0004;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Unique Ticket Id Generator compliant with the SAML 1.1 specification for
 * artifacts. This should also be compliant with the SAML 2 specification.
 * <p>
 * Default to SAML 1.1 Compliance.
 *
 * @author Scott Battaglia
 * @since 3.0.0
 */
@Component("samlServiceTicketUniqueIdGenerator")
public final class SamlCompliantUniqueTicketIdGenerator implements UniqueTicketIdGenerator {

    /** Assertion handles are randomly-generated 20-byte identifiers. */
    private static final int ASSERTION_HANDLE_SIZE = 20;

    /** SAML 2 Type 0004 endpoint ID is 0x0001. */
    private static final byte[] ENDPOINT_ID = {0, 1};

    /** SAML defines the source id as the server name. */
    private final byte[] sourceIdDigest;

    /** Flag to indicate SAML2 compliance. Default is SAML1.1. */
    @Value("${cas.saml.ticketid.saml2:false}")
    private boolean saml2compliant;

    /** Random generator to construct the AssertionHandle. */
    private final SecureRandom random;

    /**
     * Instantiates a new SAML compliant unique ticket id generator.
     *
     * @param sourceId the source id
     */
    @Autowired
    public SamlCompliantUniqueTicketIdGenerator(@Value("${server.name}") final String sourceId) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            messageDigest.update(sourceId.getBytes("8859_1"));
            this.sourceIdDigest = messageDigest.digest();
        } catch (final Exception e) {
            throw new IllegalStateException("Exception generating digest of source ID.", e);
        }
        try {
            this.random = SecureRandom.getInstance("SHA1PRNG");
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalStateException("Cannot get SHA1PRNG secure random instance.");
        }
    }

    /**
     * {@inheritDoc}
     * We ignore prefixes for SAML compliance.
     */
    @Override
    public String getNewTicketId(final String prefix) {
        if (saml2compliant) {
            return new SAML2ArtifactType0004(ENDPOINT_ID, newAssertionHandle(), sourceIdDigest).base64Encode();
        }
        return new SAML1ArtifactType0001(this.sourceIdDigest, newAssertionHandle()).base64Encode();
    }

    public void setSaml2compliant(final boolean saml2compliant) {
        this.saml2compliant = saml2compliant;
    }

    /**
     * New assertion handle.
     *
     * @return the byte[] array of size {@link #ASSERTION_HANDLE_SIZE}
     */
    private byte[] newAssertionHandle() {
        final byte[] handle = new byte[ASSERTION_HANDLE_SIZE];
        this.random.nextBytes(handle);
        return handle;
    }
}
