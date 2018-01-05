package org.jasig.cas.web.support;

import org.jasig.cas.authentication.RememberMeCredential;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.CookieGenerator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Extends CookieGenerator to allow you to retrieve a value from a request.
 * The cookie is automatically marked as httpOnly, if the servlet container has support for it.
 * 
 * <p>
 * Also has support for RememberMe Services
 *
 * @author Scott Battaglia
 * @author Misagh Moayyed
 * @since 3.1
 *
 */
public class CookieRetrievingCookieGenerator extends CookieGenerator {

    private static final int DEFAULT_REMEMBER_ME_MAX_AGE = 7889231;

    /** The maximum age the cookie should be remembered for.
     * The default is three months ({@value} in seconds, according to Google) */
    private int rememberMeMaxAge = DEFAULT_REMEMBER_ME_MAX_AGE;

    /** Responsible for manging and verifying the cookie value. **/
    private final CookieValueManager casCookieValueManager;

    /**
     * Instantiates a new cookie retrieving cookie generator
     * with a default cipher of {@link org.jasig.cas.web.support.NoOpCookieValueManager}.
     */
    public CookieRetrievingCookieGenerator() {
        this(new NoOpCookieValueManager());
    }

    /**
     * Instantiates a new Cookie retrieving cookie generator.
     * @param casCookieValueManager the cookie manager
     */
    public CookieRetrievingCookieGenerator(final CookieValueManager casCookieValueManager) {
        super();
        final Method setHttpOnlyMethod = ReflectionUtils.findMethod(Cookie.class, "setHttpOnly", boolean.class);
        if(setHttpOnlyMethod != null) {
            super.setCookieHttpOnly(true);
        } else {
            logger.debug("Cookie cannot be marked as HttpOnly; container is not using servlet 3.0.");
        }
        this.casCookieValueManager = casCookieValueManager;
    }
    /**
     * Adds the cookie, taking into account {@link RememberMeCredential#REQUEST_PARAMETER_REMEMBER_ME}
     * in the request.
     *
     * @param request the request
     * @param response the response
     * @param cookieValue the cookie value
     */
    public void addCookie(final HttpServletRequest request, final HttpServletResponse response, final String cookieValue) {
        final String theCookieValue = this.casCookieValueManager.buildCookieValue(cookieValue, request);

        if (!StringUtils.hasText(request.getParameter(RememberMeCredential.REQUEST_PARAMETER_REMEMBER_ME))) {
            super.addCookie(response, theCookieValue);
        } else {
            final Cookie cookie = createCookie(theCookieValue);
            cookie.setMaxAge(this.rememberMeMaxAge);
            if (isCookieSecure()) {
                cookie.setSecure(true);
            }
            response.addCookie(cookie);
        }
    }

    /**
     * Retrieve cookie value.
     *
     * @param request the request
     * @return the cookie value
     */
    public String retrieveCookieValue(final HttpServletRequest request) {
        try {
            final Cookie cookie = org.springframework.web.util.WebUtils.getCookie(
                    request, getCookieName());
            return cookie == null ? null : this.casCookieValueManager.obtainCookieValue(cookie, request);
        } catch (final Exception e) {
            logger.debug(e.getMessage(), e);
        }
        return null;
    }

    public void setRememberMeMaxAge(final int maxAge) {
        this.rememberMeMaxAge = maxAge;
    }
}
