package org.jasig.cas.adaptors.generic;

import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;

/**
 * Class designed to read data from a file in the format of USERNAME SEPARATOR
 * PASSWORD that will go line by line and look for the username. If it finds the
 * username it will compare the supplied password (first put through a
 * PasswordTranslator) that is compared to the password provided in the file. If
 * there is a match, the user is authenticated. Note that the default password
 * translator is a plaintext password translator and the default separator is
 * "::" (without quotes).
 *
 * @author Scott Battaglia
 * @author Marvin S. Addison
 * @since 3.0.0
 */
@Component("fileAuthenticationHandler")
public class FileAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {

    /** The default separator in the file. */
    private static final String DEFAULT_SEPARATOR = "::";

    /** The separator to use. */
    @NotNull
    private String separator = DEFAULT_SEPARATOR;

    /** The filename to read the list of usernames from. */
    private Resource fileName;


    @Override
    protected final HandlerResult authenticateUsernamePasswordInternal(final UsernamePasswordCredential credential)
            throws GeneralSecurityException, PreventedException {

        try {
            if (this.fileName == null || !this.fileName.exists()) {
                throw new FileNotFoundException("Filename does not exist");
            }

            final String username = credential.getUsername();
            final String passwordOnRecord = getPasswordOnRecord(username);
            if (StringUtils.isBlank(passwordOnRecord)) {
                throw new AccountNotFoundException(username + " not found in backing file.");
            }
            final String password = credential.getPassword();
            if (StringUtils.isNotBlank(password) && this.getPasswordEncoder().encode(password).equals(passwordOnRecord)) {
                return createHandlerResult(credential, this.principalFactory.createPrincipal(username), null);
            }
        } catch (final IOException e) {
            throw new PreventedException("IO error reading backing file", e);
        }
        throw new FailedLoginException();
    }

    /**
     * @param fileName The fileName to set.
     */
    @Autowired
    public final void setFileName(@Value("${file.authn.filename:}") final Resource fileName) {
        this.fileName = fileName;
    }

    /**
     * @param separator The separator to set.
     */
    @Autowired
    public final void setSeparator(@Value("${file.authn.separator:" + DEFAULT_SEPARATOR + '}') final String separator) {
        this.separator = separator;
    }

    /**
     * Gets the password on record.
     *
     * @param username the username
     * @return the password on record
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private String getPasswordOnRecord(final String username) throws IOException {

        try (BufferedReader bufferedReader =
                     new BufferedReader(
                             new InputStreamReader(this.fileName.getInputStream(), Charset.defaultCharset()))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                final String[] lineFields = line.split(this.separator);
                final String userOnRecord = lineFields[0];
                final String passOnRecord = lineFields[1];
                if (username.equals(userOnRecord)) {
                    return passOnRecord;
                }
                line = bufferedReader.readLine();
            }
        }
        return null;
    }
}
