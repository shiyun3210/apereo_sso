<%@ page session="false" contentType="application/xml; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<cas:serviceResponse xmlns:cas='http://www.yale.edu/tp/cas'>
    <cas:authenticationSuccess>
        <cas:user>${fn:escapeXml(principal.id)}</cas:user>
        <c:choose>
			<c:when test="${not empty principal.attributes.parentId}">
				<cas:attributes>
			        <cas:userId>${fn:escapeXml(principal.attributes.userId)}</cas:userId>
			        <cas:parentId>${fn:escapeXml(principal.attributes.parentId)}</cas:parentId>
			        <cas:companyId>${fn:escapeXml(principal.attributes.companyId)}</cas:companyId>
					<cas:lastLoginDate>${fn:escapeXml(principal.attributes.lastLoginDate)}</cas:lastLoginDate>
					<cas:mobilePhone>${fn:escapeXml(principal.attributes.mobilePhone)}</cas:mobilePhone>
					<cas:contactNameCn>${fn:escapeXml(principal.attributes.contactNameCn)}</cas:contactNameCn>
					<cas:auditStatus>${fn:escapeXml(principal.attributes.auditStatus)}</cas:auditStatus>
		        </cas:attributes>
			</c:when>
			<c:otherwise>
				<cas:attributes>
			        <cas:userId>${fn:escapeXml(principal.attributes.userId)}</cas:userId>
					<cas:userName>${fn:escapeXml(principal.attributes.contact)}</cas:userName>
		        </cas:attributes>
			</c:otherwise>
		</c:choose>
        
        <c:if test="${not empty pgtIou}">
            <cas:proxyGrantingTicket>${pgtIou}</cas:proxyGrantingTicket>
        </c:if>
        <c:if test="${fn:length(chainedAuthentications) > 0}">
            <cas:proxies>
                <c:forEach var="proxy" items="${chainedAuthentications}" varStatus="loopStatus" begin="0" end="${fn:length(chainedAuthentications)}" step="1">
                    <cas:proxy>${fn:escapeXml(proxy.principal.id)}</cas:proxy>
                </c:forEach>
            </cas:proxies>
        </c:if>
    </cas:authenticationSuccess>
</cas:serviceResponse>
