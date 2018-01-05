package org.jasig.cas.support.openid.web.support;


import org.springframework.stereotype.Component;

/**
 * Extracts a local Id from an openid.identity. The default provider can extract
 * the following uris: http://openid.myprovider.com/scottb provides a local id
 * of scottb.
 *
 * @author Scott Battaglia
 * @since 3.1
 */
@Component("defaultOpenIdUserNameExtractor")
public final class DefaultOpenIdUserNameExtractor implements OpenIdUserNameExtractor {

    @Override
    public String extractLocalUsernameFromUri(final String uri) {
        if (uri == null) {
            return null;
        }

        if (!uri.contains("/")) {
            return null;
        }

        return uri.substring(uri.lastIndexOf('/') + 1);
    }

}
