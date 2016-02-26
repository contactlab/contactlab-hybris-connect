<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="headline">
	<spring:theme code="text.account.newsletter" text="Newsletter"/>
</div>

<div id="Profile-Newsletter">
          <p><spring:theme code="text.account.newsletter.subscriptions"/></p>
	<c:forEach items="${allCampaigns}" var="campaign">
		<c:set var="checked" value=""/>
		<c:forEach items="${campaigns}" var="subscribedCampaign">
			<c:if test="${campaign.id eq subscribedCampaign.id}">
				<c:set var="checked" value="checked=\"checked\""/>
			</c:if>
		</c:forEach>
           <div class="checkbox-list">
               <input type="checkbox" name="campaigns" disabled="disabled" ${checked}><label>${fn:escapeXml(campaign.label)}</label>
           </div>
	</c:forEach>

    <a class="button" href="newsletter/update"><spring:theme code="text.account.newsletter.changeSubscription" text="Change your subscriptions"/></a>
</div>