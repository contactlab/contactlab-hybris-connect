<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:theme code="newsletter.language" var="language" text="en"/>

<div class="cmsimage">
	<a id="nlBtnSubscribe" href="#subscriptionsDiv" class="lightbox"><img title="newsletter" src="${commonResourcePath}/../../addons/contactlabaddon/desktop/common/images/banner_newsletter_${language}.jpg" alt="newsletter"></a>
</div>