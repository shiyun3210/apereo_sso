package org.jasig.cas.web;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.webflow.execution.repository.BadlyFormattedFlowExecutionKeyException;
import org.springframework.webflow.execution.repository.FlowExecutionRepositoryException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * The FlowExecutionExceptionResolver catches the FlowExecutionRepositoryException
 * thrown by Spring Webflow when the given flow id no longer exists. This can
 * occur if a particular flow has reached an end state (the id is no longer
 * valid)
 * <p>
 * It will redirect back to the requested URI which should start a new workflow.
 * </p>
 *
 * @author Scott Battaglia
 * @author Misagh Moayyed
 * @since 3.0.0
 */
@Component("errorHandlerResolver")
public final class FlowExecutionExceptionResolver implements HandlerExceptionResolver {

    /** Instance of a logger. */
    private final transient Logger logger = LoggerFactory.getLogger(this.getClass());

    @NotNull
    private String modelKey = "exception.message";

    @Override
    public ModelAndView resolveException(final HttpServletRequest request,
        final HttpServletResponse response, final Object handler,
        final Exception exception) {

        /*
         * Since FlowExecutionRepositoryException is a common ancestor to these exceptions and other
         * error cases we would likely want to hide from the user, it seems reasonable to check for
         * FlowExecutionRepositoryException.
         *
         * BadlyFormattedFlowExecutionKeyException is specifically ignored by this handler
         * because redirecting to the requested URI with this exception may cause an infinite
         * redirect loop (i.e. when invalid "execution" parameter exists as part of the query string
         */
        if (!(exception instanceof FlowExecutionRepositoryException)
              || exception instanceof BadlyFormattedFlowExecutionKeyException) {
            logger.debug("Ignoring the received exception due to a type mismatch", exception);
            return null;
        }

        final String urlToRedirectTo = request.getRequestURI()
                + (request.getQueryString() != null ? '?'
                + request.getQueryString() : "");

        logger.debug("Error getting flow information for URL [{}]", urlToRedirectTo, exception);
        final Map<String, Object> model = new HashMap<>();
        model.put(this.modelKey, StringEscapeUtils.escapeHtml4(exception.getMessage()));

        return new ModelAndView(new RedirectView(urlToRedirectTo), model);
    }

    public void setModelKey(final String modelKey) {
        this.modelKey = modelKey;
    }
}
