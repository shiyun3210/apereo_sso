/**
 * <p>Credentials is a marker interface for an opaque object that may be recognized by
 * Handlers and Resolvers. Credentials may be a Userid/Password, Certificate,
 * RemoteUser, IP address, etc.</p>
 * <p>When the simple AuthenticationManagerImpl is
 * used, that bean is configured with a list of AuthenticationHandlers that
 * validate Credentials and CredentialsToPrincipalResolvers that turn Credentials
 * into Principal objects.</p>
 * <p>The Authentication Handler validates Credentials but does not extract
 * information. This seems curious in the simple case when the credential are a
 * Userid/Password. It becomes clearer for a Certificate. A Certificate is valid if
 * you trust the CA, if it hasn't expired, and if it isn't revoked. You can decide
 * all this, and still not have the foggiest idea what ID to give to the person (if
 * it is a person) represented by the Certificate.</p>
 * <p>The CredentialsToPrincipalResolver looks into previously validated
 * Credentials to construct a Principal object containing an ID (and in more
 * complex cases some attributes). The DefaultCredentialsToPrincipalResolver takes
 * UsernamePasswordCredentials and creates a SimplePrincipal containing the Userid.</p>
 * @since 3.0
 */
package org.jasig.cas.authentication.principal;

