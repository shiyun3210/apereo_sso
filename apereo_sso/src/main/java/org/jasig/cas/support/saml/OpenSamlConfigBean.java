package org.jasig.cas.support.saml;


import net.shibboleth.utilities.java.support.xml.ParserPool;
import org.opensaml.core.config.ConfigurationService;
import org.opensaml.core.config.InitializationException;
import org.opensaml.core.config.InitializationService;
import org.opensaml.core.xml.XMLObjectBuilderFactory;
import org.opensaml.core.xml.config.XMLObjectProviderRegistry;
import org.opensaml.core.xml.io.MarshallerFactory;
import org.opensaml.core.xml.io.UnmarshallerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

/**
 * Load the opensaml config context.
 * @author Misagh Moayyed
 * @since 4.1
 */
public final class OpenSamlConfigBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenSamlConfigBean.class);

    @Autowired
    @NotNull
    private ParserPool parserPool;

    @NotNull
    private XMLObjectBuilderFactory builderFactory;

    @NotNull
    private MarshallerFactory marshallerFactory;

    @NotNull
    private UnmarshallerFactory unmarshallerFactory;


    /**
     * Instantiates the config bean.
     */
    public OpenSamlConfigBean() {}

    /**
     * Gets the configured parser pool.
     *
     * @return the parser pool
     */
    public ParserPool getParserPool() {
        return parserPool;
    }

    public XMLObjectBuilderFactory getBuilderFactory() {
        return builderFactory;
    }

    public MarshallerFactory getMarshallerFactory() {
        return marshallerFactory;
    }

    public UnmarshallerFactory getUnmarshallerFactory() {
        return unmarshallerFactory;
    }

    /**
     * Initialize opensaml.
     */
    @PostConstruct
    public void init() {
        LOGGER.info("Initializing OpenSaml configuration...");
        Assert.notNull(this.parserPool, "parserPool cannot be null");

        try {
            InitializationService.initialize();
        } catch (final InitializationException e) {
            throw new RuntimeException("Exception initializing OpenSAML", e);
        }

        XMLObjectProviderRegistry registry;
        synchronized(ConfigurationService.class) {
            registry = ConfigurationService.get(XMLObjectProviderRegistry.class);
            if (registry == null) {
                LOGGER.debug("XMLObjectProviderRegistry did not exist in ConfigurationService, will be created");
                registry = new XMLObjectProviderRegistry();
                ConfigurationService.register(XMLObjectProviderRegistry.class, registry);
            }
        }

        registry.setParserPool(this.parserPool);

        this.builderFactory = registry.getBuilderFactory();
        Assert.notNull(this.builderFactory, "parserPool cannot be null");

        this.marshallerFactory = registry.getMarshallerFactory();
        Assert.notNull(this.marshallerFactory, "marshallerFactory cannot be null");

        this.unmarshallerFactory = registry.getUnmarshallerFactory();
        Assert.notNull(this.unmarshallerFactory, "unmarshallerFactory cannot be null");

        LOGGER.debug("Initialized OpenSaml successfully.");
    }
}
