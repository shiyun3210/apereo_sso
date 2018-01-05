package org.jasig.cas.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * No-Op cipher executor that does nothing for encryption/decryption.
 * @author Misagh Moayyed
 * @since 4.1
 */
public final class NoOpCipherExecutor extends AbstractCipherExecutor<String, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoOpCipherExecutor.class);

    /**
     * Instantiates a new No-Op cipher executor.
     * Issues a warning on safety.
     */
    public NoOpCipherExecutor() {
        super(NoOpCipherExecutor.class.getName());
        LOGGER.warn("[{}] does no encryption and may NOT be safe in a production environment. "
                + "Consider using other choices, such as [{}] that handle encryption, signing and verification of "
                + "all appropriate values.", this.getClass().getName(), BaseStringCipherExecutor.class.getName());
    }

    @Override
    public String encode(final String value) {
        return value;
    }

    @Override
    public String decode(final String value) {
        return value;
    }
}
