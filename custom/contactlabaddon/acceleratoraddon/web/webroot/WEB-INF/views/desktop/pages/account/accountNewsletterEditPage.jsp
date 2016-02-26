<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<c:url var="profileUrl" value="/my-account/profile" />
<c:url var="subscriptionUpdateUrl" value="/my-account/newsletter/update" />

<div class="headline">
	<spring:theme code="text.account.newsletter" text="Newsletter"/>
</div>

<div id="Profile-Newsletter">
          <p><spring:theme code="text.account.newsletter.subscriptions"/></p>
	<form:form  id="emailSubscriptionForm" action="${subscriptionUpdateUrl}" method="post" modelAttribute="emailSubscriptionForm" >
		<c:if test="${not empty campaigns}">
	 		<form:checkboxes path="selectedCampaigns" items="${campaigns}" itemValue="id" itemLabel="label" element="div class=\"checkbox-list\""/>
	 	</c:if>
	 	<div class="form-actions">
	 		<form:button class="b"><spring:theme code="text.account.newsletter.saveUpdates" text="Save Updates"/> </form:button>
		</div>
	</form:form>
</div>

