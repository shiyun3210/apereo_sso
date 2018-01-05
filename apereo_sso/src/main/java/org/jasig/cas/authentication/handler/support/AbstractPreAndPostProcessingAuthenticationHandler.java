package org.jasig.cas.authentication.handler.support;

import org.jasig.cas.authentication.AbstractAuthenticationHandler;
import org.jasig.cas.authentication.BasicCredentialMetaData;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.DefaultHandlerResult;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.MessageDescriptor;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.principal.Principal;

import javax.security.auth.login.FailedLoginException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * Abstract authentication handler that allows deployers to utilize the bundled
 * AuthenticationHandlers while providing a mechanism to perform tasks before
 * and after authentication.
 *
 * @author Scott Battaglia
 * @author Marvin S. Addison
 *
 * @since 3.1
 */
public abstract class AbstractPreAndPostProcessingAuthenticationHandler extends AbstractAuthenticationHandler {

    /**
     * Template method to perform arbitrary pre-authentication actions.
     *
     * @param credential the Credential supplied
     * @return true if authentication should continue, false otherwise.
     */
    protected boolean preAuthenticate(final Credential credential) {
        return true;
    }

    /**
     * Template method to perform arbitrary post-authentication actions.
     *
     * @param credential the supplied credential
     * @param result the result of the authentication attempt.
     *
     * @return An authentication handler result that MAY be different or modified from that provided.
     */
    protected HandlerResult postAuthenticate(final Credential credential, final HandlerResult result) {
        return result;
    }

    /**
     * {@inheritDoc}
     **/
    @Override
    public final HandlerResult authenticate(final Credential credential)
            throws GeneralSecurityException, PreventedException {

        if (!preAuthenticate(credential)) {
            throw new FailedLoginException();
        }

        return postAuthenticate(credential, doAuthentication(credential));
    }

    /**
     * Performs the details of authentication and returns an authentication handler result on success.
     *
     *
     * @param credential Credential to authenticate.
     *
     * @return Authentication handler result on success.
     *
     * @throws GeneralSecurityException On authentication failure that is thrown out to the caller of
     * {@link #authenticate(org.jasig.cas.authentication.Credential)}.
     * @throws PreventedException On the indeterminate case when authentication is prevented.
     */
    protected abstract HandlerResult doAuthentication(Credential credential)
            throws GeneralSecurityException, PreventedException;

    /**
     * Helper method to construct a handler result
     * on successful authentication events.
     *
     * @param credential the credential on which the authentication was successfully performed.
     * Note that this credential instance may be different from what was originally provided
     * as transformation of the username may have occurred, if one is in fact defined.
     * @param principal the resolved principal
     * @param warnings the warnings
     * @return the constructed handler result
     */
    protected final HandlerResult createHandlerResult(final Credential credential, final Principal principal,
                                                      final List<MessageDescriptor> warnings) {
        return new DefaultHandlerResult(this, new BasicCredentialMetaData(credential), principal, warnings);
    }
}
