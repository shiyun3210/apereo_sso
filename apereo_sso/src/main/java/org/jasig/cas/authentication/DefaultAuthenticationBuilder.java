package org.jasig.cas.authentication;

import org.jasig.cas.authentication.principal.Principal;
import org.joda.time.DateTime;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Constructs immutable {@link Authentication} objects using the builder pattern.
 *
 * @author Marvin S. Addison
 * @since 4.0.0
 */
public class DefaultAuthenticationBuilder implements AuthenticationBuilder {
    private static final long serialVersionUID = -8504842011648432398L;
    /** Authenticated principal. */
    private Principal principal;

    /** Credential metadata. */
    private final List<CredentialMetaData> credentials = new ArrayList<>();

    /** Authentication metadata attributes. */
    private final Map<String, Object> attributes = new LinkedHashMap<>();

    /** Map of handler names to authentication successes. */
    private final Map<String, HandlerResult> successes = new LinkedHashMap<>();

    /** Map of handler names to authentication failures. */
    private final Map<String, Class<? extends Exception>> failures = new LinkedHashMap<>();

    /** Authentication date. */
    private DateTime authenticationDate;

    /**
     * Creates a new instance using the current date for the authentication date.
     */
    public DefaultAuthenticationBuilder() {
        authenticationDate = new DateTime();
    }

    /**
     * Creates a new instance using the current date for the authentication date and the given
     * principal for the authenticated principal.
     *
     * @param p Authenticated principal.
     */
    public DefaultAuthenticationBuilder(final Principal p) {
        this();
        this.principal = p;
    }

    /**
     * Gets the authentication date.
     *
     * @return Authentication date.
     */
    public DateTime getAuthenticationDate() {
        return this.authenticationDate == null ? null : new DateTime(this.authenticationDate);
    }

    /**
     * Sets the authentication date and returns this instance.
     *
     * @param d Authentication date.
     *
     * @return This builder instance.
     */
    @Override
    public AuthenticationBuilder setAuthenticationDate(final DateTime d) {
        this.authenticationDate = d;
        return this;
    }

    /**
     * Gets the authenticated principal.
     *
     * @return Principal.
     */
    @Override
    public Principal getPrincipal() {
        return this.principal;
    }

    @Override
    public AuthenticationBuilder addCredentials(final List<CredentialMetaData> credentials) {
        this.credentials.addAll(credentials);
        return this;
    }

    /**
     * Sets the principal returns this instance.
     *
     * @param p Authenticated principal.
     *
     * @return This builder instance.
     */
    @Override
    public AuthenticationBuilder setPrincipal(final Principal p) {
        this.principal = p;
        return this;
    }

    /**
     * Gets the list of credentials that were attempted to be authenticated.
     *
     * @return Non-null list of credentials that attempted authentication.
     */
    public List<CredentialMetaData> getCredentials() {
        return this.credentials;
    }

    /**
     * Sets the list of metadata about credentials presented for authentication.
     *
     * @param credentials Non-null list of credential metadata.
     *
     * @return This builder instance.
     */
    public AuthenticationBuilder setCredentials(final List<CredentialMetaData> credentials) {
        Assert.notNull(credentials, "Credential cannot be null");
        this.credentials.clear();
        this.credentials.addAll(credentials);
        return this;
    }

    /**
     * Adds metadata about a credential presented for authentication.
     *
     * @param credential Credential metadata.
     *
     * @return This builder instance.
     */
    @Override
    public AuthenticationBuilder addCredential(final CredentialMetaData credential) {
        this.credentials.add(credential);
        return this;
    }

    /**
     * Gets the authentication attribute map.
     *
     * @return Non-null authentication attribute map.
     */
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    /**
     * Sets the authentication metadata attributes.
     *
     * @param attributes Non-null map of authentication metadata attributes.
     *
     * @return This builder instance.
     */
    @Override
    public AuthenticationBuilder setAttributes(final Map<String, Object> attributes) {
        Assert.notNull(attributes, "Attributes cannot be null");
        this.attributes.clear();
        for (final Map.Entry<String, Object> entry : attributes.entrySet()) {
            this.attributes.put(entry.getKey(), entry.getValue());
        }
        return this;
    }

    /**
     * Adds an authentication metadata attribute key-value pair.
     *
     * @param key Authentication attribute key.
     * @param value Authentication attribute value.
     *
     * @return This builder instance.
     */
    @Override
    public AuthenticationBuilder addAttribute(final String key, final Object value) {
        this.attributes.put(key, value);
        return this;
    }

    /**
     * Gets the authentication success map.
     *
     * @return Non-null map of handler names to successful handler authentication results.
     */
    @Override
    public Map<String, HandlerResult> getSuccesses() {
        return this.successes;
    }

    /**
     * Sets the authentication handler success map.
     *
     * @param successes Non-null map of handler names to successful handler authentication results.
     *
     * @return This builder instance.
     */
    @Override
    public AuthenticationBuilder setSuccesses(final Map<String, HandlerResult> successes) {
        Assert.notNull(successes, "Successes cannot be null");
        this.successes.clear();
        return addSuccesses(successes);
    }

    @Override
    public AuthenticationBuilder addSuccesses(final Map<String, HandlerResult> successes) {
        for (final Map.Entry<String, HandlerResult> entry : successes.entrySet()) {
            addSuccess(entry.getKey(), entry.getValue());
        }
        return this;
    }

    /**
     * Adds an authentication success to the map of handler names to successful authentication handler results.
     *
     * @param key Authentication handler name.
     * @param value Successful authentication handler result produced by handler of given name.
     *
     * @return This builder instance.
     */
    @Override
    public AuthenticationBuilder addSuccess(final String key, final HandlerResult value) {
        this.successes.put(key, value);
        return this;
    }

    /**
     * Gets the authentication failure map.
     *
     * @return Non-null authentication failure map.
     */
    @Override
    public Map<String, Class<? extends Exception>> getFailures() {
        return this.failures;
    }

    /**
     * Sets the authentication handler failure map.
     *
     * @param failures Non-null map of handler name to authentication failures.
     *
     * @return This builder instance.
     */
    @Override
    public AuthenticationBuilder setFailures(final Map<String, Class<? extends Exception>> failures) {
        Assert.notNull(failures, "Failures cannot be null");
        this.failures.clear();
        return addFailures(failures);
    }

    @Override
    public AuthenticationBuilder addFailures(final Map<String, Class<? extends Exception>> failures) {
        for (final Map.Entry<String, Class<? extends Exception>> entry : failures.entrySet()) {
            addFailure(entry.getKey(), entry.getValue());
        }
        return this;
    }

    /**
     * Adds an authentication failure to the map of handler names to the authentication handler failures.
     *
     * @param key Authentication handler name.
     * @param value Exception raised on handler failure to authenticate credential.
     *
     * @return This builder instance.
     */
    @Override
    public AuthenticationBuilder addFailure(final String key, final Class<? extends Exception> value) {
        this.failures.put(key, value);
        return this;
    }

    /**
     * Creates an immutable authentication instance from builder data.
     *
     * @return Immutable authentication.
     */
    @Override
    public Authentication build() {
        return new ImmutableAuthentication(
                this.authenticationDate,
                this.credentials,
                this.principal,
                this.attributes,
                this.successes,
                this.failures);
    }

    /**
     * Creates a new builder initialized with data from the given authentication source.
     *
     * @param source Authentication source.
     *
     * @return New builder instance initialized with all fields in the given authentication source.
     */
    public static AuthenticationBuilder newInstance(final Authentication source) {
        final DefaultAuthenticationBuilder builder = new DefaultAuthenticationBuilder(source.getPrincipal());
        builder.setAuthenticationDate(source.getAuthenticationDate());
        builder.setCredentials(source.getCredentials());
        builder.setSuccesses(source.getSuccesses());
        builder.setFailures(source.getFailures());
        builder.setAttributes(source.getAttributes());
        return builder;
    }

    /**
     * Creates a new builder.
     *
     * @return New builder instance
     */
    public static AuthenticationBuilder newInstance() {
        return new DefaultAuthenticationBuilder();
    }
}
