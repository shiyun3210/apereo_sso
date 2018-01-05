package org.jasig.cas.zk.resolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.principal.PersonDirectoryPrincipalResolver;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.util.Pair;
import org.springframework.stereotype.Component;

@Component("userPrincipalResolver")
public class UserPrincipalResolver extends PersonDirectoryPrincipalResolver {
	
	@Override
    public Principal resolve(final Credential credential) {
        logger.debug("Attempting to resolve a principal...");

        final String principalId = extractPrincipalId(credential);

        if (principalId == null) {
            logger.debug("Got null for extracted principal ID; returning null.");
            return null;
        }

        logger.debug("Creating SimplePrincipal for [{}]", principalId);

        final Map<String, List<Object>> attributes = retrievePersonAttributes(principalId, credential);
        logger.debug("Principal id [{}] could not be found", principalId);

        if (attributes == null || attributes.isEmpty()) {
            logger.debug("Principal id [{}] did not specify any attributes", principalId);

            if (!this.returnNullIfNoAttributes) {
                logger.debug("Returning the principal with id [{}] without any attributes", principalId);
                return this.principalFactory.createPrincipal(principalId);
            }
            logger.debug("[{}] is configured to return null if no attributes are found for [{}]",
                    this.getClass().getName(), principalId);
            return null;
        }
        logger.debug("Retrieved [{}] attribute(s) from the repository", attributes.size());

        
        final Pair<String, Map<String, Object>> pair = convertPersonAttributesToPrincipal(principalId, attributes);
        System.out.println(pair.getFirst());
        System.out.println(pair.getSecond());
        
        return this.principalFactory.createPrincipal(pair.getFirst(), pair.getSecond());
    }

    /**
     * Convert person attributes to principal pair.
     *
     * @param extractedPrincipalId the extracted principal id
     * @param attributes           the attributes
     * @return the pair
     */
    protected Pair<String, Map<String, Object>> convertPersonAttributesToPrincipal(final String extractedPrincipalId,
                                                                                   final Map<String, List<Object>> attributes,final Credential credential) {
        final Map<String, Object> convertedAttributes = new HashMap<>();
        String principalId = extractedPrincipalId;
        for (final Map.Entry<String, List<Object>> entry : attributes.entrySet()) {
            final String key = entry.getKey();
            final List<Object> values = entry.getValue();
            if (StringUtils.isNotBlank(this.principalAttributeName)
                    && key.equalsIgnoreCase(this.principalAttributeName)) {
                if (values.isEmpty()) {
                    logger.debug("{} is empty, using {} for principal", this.principalAttributeName, extractedPrincipalId);
                } else {
                    principalId = values.get(0).toString();
                    logger.debug(
                            "Found principal attribute value {}; removing {} from attribute map.",
                            extractedPrincipalId,
                            this.principalAttributeName);
                }
            } else {
                convertedAttributes.put(key, values.size() == 1 ? values.get(0) : values);
            }
        }
        convertedAttributes.put("nickname", "ddddddd");
        convertedAttributes.put("uid", 28);
        
        return new Pair<>(principalId, convertedAttributes);
    }
}
