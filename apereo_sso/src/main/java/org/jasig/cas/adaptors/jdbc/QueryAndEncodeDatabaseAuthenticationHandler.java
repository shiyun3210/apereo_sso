package org.jasig.cas.adaptors.jdbc;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.ConfigurableHashService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.util.ByteSource;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.security.GeneralSecurityException;
import java.util.Map;

/**
 * A JDBC querying handler that will pull back the password and
 * the private salt value for a user and validate the encoded
 * password using the public salt value. Assumes everything
 * is inside the same database table. Supports settings for
 * number of iterations as well as private salt.
 *
 * <p>
 * This handler uses the hashing method defined by Apache Shiro's
 * {@link org.apache.shiro.crypto.hash.DefaultHashService}. Refer to the Javadocs
 * to learn more about the behavior. If the hashing behavior and/or configuration
 * of private and public salts does nto meet your needs, a extension can be developed
 * to specify alternative methods of encoding and digestion of the encoded password.
 * </p>
 * @author Misagh Moayyed
 * @author Charles Hasegawa (mailto:chasegawa@unicon.net)
 * @since 4.1.0
 */
@Component("queryAndEncodeDatabaseAuthenticationHandler")
public class QueryAndEncodeDatabaseAuthenticationHandler extends AbstractJdbcUsernamePasswordAuthenticationHandler {

    private static final String DEFAULT_PASSWORD_FIELD = "password";
    private static final String DEFAULT_SALT_FIELD = "salt";
    private static final String DEFAULT_NUM_ITERATIONS_FIELD = "numIterations";

    /**
     * The Algorithm name.
     */
    @NotNull
    protected String algorithmName;

    /**
     * The Sql statement to execute.
     */
    @NotNull
    protected String sql;

    /**
     * The Password field name.
     */
    @NotNull
    protected String passwordFieldName = DEFAULT_PASSWORD_FIELD;

    /**
     * The Salt field name.
     */
    @NotNull
    protected String saltFieldName = DEFAULT_SALT_FIELD;

    /**
     * The Number of iterations field name.
     */
    @NotNull
    protected String numberOfIterationsFieldName = DEFAULT_NUM_ITERATIONS_FIELD;

    /**
     * The number of iterations. Defaults to 0.
     */
    protected long numberOfIterations;

    /**
     * The static/private salt.
     */
    protected String staticSalt;

    /**
     * Instantiates a new Query and encode database authentication handler.
     *
     * @param datasource The database datasource
     * @param sql the sql query to execute which must include a parameter placeholder
     *            for the user id. (i.e. {@code SELECT * FROM table WHERE username = ?}
     * @param algorithmName the algorithm name (i.e. {@code MessageDigestAlgorithms.SHA_512})
     */
    @Autowired(required = false)
    public QueryAndEncodeDatabaseAuthenticationHandler(@Qualifier("queryEncodeDatabaseDataSource")
                                                           final DataSource datasource,
                                                       @Value("${cas.jdbc.authn.query.encode.sql:}")
                                                       final String sql,
                                                       @Value("${cas.jdbc.authn.query.encode.alg:}")
                                                       final String algorithmName) {
        super();
        if (datasource != null) {
            setDataSource(datasource);
            this.sql = sql;
            this.algorithmName = algorithmName;
        }
    }

    @Override
    protected final HandlerResult authenticateUsernamePasswordInternal(final UsernamePasswordCredential transformedCredential)
            throws GeneralSecurityException, PreventedException {

        if (StringUtils.isBlank(this.sql) || StringUtils.isBlank(this.algorithmName) || getJdbcTemplate() == null) {
            throw new GeneralSecurityException("Authentication handler is not configured correctly");
        }

        final String username = getPrincipalNameTransformer().transform(transformedCredential.getUsername());
        final String encodedPsw = this.getPasswordEncoder().encode(transformedCredential.getPassword());

        try {
            final Map<String, Object> values = getJdbcTemplate().queryForMap(this.sql, username);
            final String digestedPassword = digestEncodedPassword(encodedPsw, values);

            if (!values.get(this.passwordFieldName).equals(digestedPassword)) {
                throw new FailedLoginException("Password does not match value on record.");
            }
            return createHandlerResult(transformedCredential,
                    this.principalFactory.createPrincipal(username), null);

        } catch (final IncorrectResultSizeDataAccessException e) {
            if (e.getActualSize() == 0) {
                throw new AccountNotFoundException(username + " not found with SQL query");
            } else {
                throw new FailedLoginException("Multiple records found for " + username);
            }
        } catch (final DataAccessException e) {
            throw new PreventedException("SQL exception while executing query for " + username, e);
        }

    }

    /**
     * Digest encoded password.
     *
     * @param encodedPassword the encoded password
     * @param values the values retrieved from database
     * @return the digested password
     */
    protected String digestEncodedPassword(final String encodedPassword, final Map<String, Object> values) {
        final ConfigurableHashService hashService = new DefaultHashService();

        if (StringUtils.isNotBlank(this.staticSalt)) {
            hashService.setPrivateSalt(ByteSource.Util.bytes(this.staticSalt));
        }
        hashService.setHashAlgorithmName(this.algorithmName);

        Long numOfIterations = this.numberOfIterations;
        if (values.containsKey(this.numberOfIterationsFieldName)) {
            final String longAsStr = values.get(this.numberOfIterationsFieldName).toString();
            numOfIterations = Long.valueOf(longAsStr);
        }

        hashService.setHashIterations(numOfIterations.intValue());
        if (!values.containsKey(this.saltFieldName)) {
            throw new RuntimeException("Specified field name for salt does not exist in the results");
        }

        final String dynaSalt = values.get(this.saltFieldName).toString();
        final HashRequest request = new HashRequest.Builder()
                                    .setSalt(dynaSalt)
                                    .setSource(encodedPassword)
                                    .build();
        return hashService.computeHash(request).toHex();
    }

    /**
     * Sets static/private salt to be combined with the dynamic salt retrieved
     * from the database. Optional.
     *
     * <p>If using this implementation as part of a password hashing strategy,
     * it might be desirable to configure a private salt.
     * A hash and the salt used to compute it are often stored together.
     * If an attacker is ever able to access the hash (e.g. during password cracking)
     * and it has the full salt value, the attacker has all of the input necessary
     * to try to brute-force crack the hash (source + complete salt).</p>
     *
     * <p>However, if part of the salt is not available to the attacker (because it is not stored with the hash),
     * it is much harder to crack the hash value since the attacker does not have the complete inputs necessary.
     * The privateSalt property exists to satisfy this private-and-not-shared part of the salt.</p>
     * <p>If you configure this attribute, you can obtain this additional very important safety feature.</p>
     * @param staticSalt the static salt
     */
    @Autowired
    public final void setStaticSalt(@Value("${cas.jdbc.authn.query.encode.salt.static:}")
                                    final String staticSalt) {
        this.staticSalt = staticSalt;
    }

    /**
     * Sets password field name. Default is {@link #DEFAULT_PASSWORD_FIELD}.
     *
     * @param passwordFieldName the password field name
     */
    @Autowired
    public final void setPasswordFieldName(@Value("${cas.jdbc.authn.query.encode.password:" + DEFAULT_PASSWORD_FIELD + '}')
                                               final String passwordFieldName) {
        this.passwordFieldName = passwordFieldName;
    }

    /**
     * Sets salt field name. Default is {@link #DEFAULT_SALT_FIELD}.
     *
     * @param saltFieldName the password field name
     */
    @Autowired
    public final void setSaltFieldName(@Value("${cas.jdbc.authn.query.encode.salt:" + DEFAULT_SALT_FIELD + '}')
                                       final String saltFieldName) {
        this.saltFieldName = saltFieldName;
    }

    /**
     * Sets number of iterations field name. Default is {@link #DEFAULT_NUM_ITERATIONS_FIELD}.
     *
     * @param numberOfIterationsFieldName the password field name
     */
    @Autowired
    public final void setNumberOfIterationsFieldName(@Value("${cas.jdbc.authn.query.encode.iterations.field:"
                                                            + DEFAULT_NUM_ITERATIONS_FIELD + '}')
                                                         final String numberOfIterationsFieldName) {
        this.numberOfIterationsFieldName = numberOfIterationsFieldName;
    }

    /**
     * Sets number of iterations. Default is 0.
     *
     * @param numberOfIterations the number of iterations
     */
    @Autowired
    public final void setNumberOfIterations(@Value("${cas.jdbc.authn.query.encode.iterations:0}")
                                                final long numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

}
