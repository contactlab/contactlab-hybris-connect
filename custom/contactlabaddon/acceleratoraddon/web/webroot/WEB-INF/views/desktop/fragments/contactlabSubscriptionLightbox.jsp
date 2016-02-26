<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>

<%-- <div id="form_subscriptions">
   <form id="subscriptions">
        <h1>be the first to know</h1>
        <hr>
        <input type="text" id="first_name" value="First name"><div class="error_nl input_txt" style="visibility:hidden;"></div>
        <input type="text" id="last_name" value="Last name"><div class="error_nl input_txt" style="visibility:hidden;"></div>
        <input type="text" id="email" value="Your email"><div class="error_nl input_txt" style="visibility:hidden;"></div>
        <p>Which newsletter would you like to receive?</p>
            <div class="checkbox-list-nl">
                <input type="checkbox" name="list1"><label>Lorem ipsum</label>
            </div>
            <div class="checkbox-list-nl">
                <input type="checkbox" name="list2" checked="checked"><label>Lorem ipsum</label>
            </div>
            <div class="checkbox-list-nl">
                <input type="checkbox" name="list3"><label>Dolor sit</label>
            </div>
            <div class="checkbox-list-nl">
                <input type="checkbox" name="list4"><label>All</label>
            </div>

            <div class="checkbox-list-nl-privacy">
                <input type="checkbox" name="list4"><label>I accept the <a href="#" target="_blank">Terms &amp; Conditions</a></label><div class="error_nl" style="visibility:hidden;"></div>
            </div>
            <div class="error_nl_txt" style="visibility:hidden;">Required field. Please fill in.</div>

            <input name="do_subscribe" type="submit" value="Sign up for our newsletter">
   </form>
</div> --%>
<div class="lightbox-target" id="subscriptionsDiv" tabindex="-1">
	<div id="form_subscriptions">
		<c:choose>	
			<c:when test="${subscriptionCompleted eq true}">
				<c:url var="subscriptionconfirmUrl" value="emailmarketing/subscriptionconfirm"/>
				<script type="text/javascript">
					window.location="${subscriptionconfirmUrl}";
				</script>
			</c:when>
			
			<c:otherwise>
				<c:url var="subscriptionUrl" value="emailmarketing/subscribe"/>
				<form:form  id="subscriptions" action="${subscriptionUrl}" method="post" modelAttribute="emailSubscriptionForm" >
					<h1><spring:theme code="newsletter.lightbox.title" /></h1>
					<hr>
					<div id="globalMessages">
						<common:globalMessages/>
					</div>
		
				 	<spring:theme text='Name' code='newsletter.firstName.placeholder' var="firstnamePh" />
					<spring:theme text='Surname' code='newsletter.lastName.placeholder' var="lastnamePh" />
					<spring:theme text='Email' code='newsletter.email.placeholder' var="emailPh" />
						
					<form:input path="firstName" placeholder="${firstnamePh }"  /><br>
					<form:input path="lastName" placeholder="${lastnamePh }" /><br>
					<form:input path="email"  placeholder="${emailPh }" /><br>
					
					
					<p><spring:theme code="newsletter.lightbox.newsletterlist" /></p>
					<c:if test="${not empty campaigns}">
				 		<form:checkboxes path="selectedCampaigns" items="${campaigns}" itemValue="id" itemLabel="label" element="div class=\"checkbox-list-nl\""/>
				 	</c:if>
				 	
				 	
				 	<div class="checkbox-list-nl-privacy">
				 		<form:checkbox path="privacy"/>
				 		<form:label path="privacy"><spring:theme code="newsletter.lightbox.privacy"/></form:label>
				 	</div>
		            
				 	
				 	<form:button value=""><spring:theme text="Subscribe" code="newsletter.button.subscribe" /></form:button>
				 	<%-- <spring:theme text="" code="newsletter.button.subscribe" var="subscribeTxt" />
				 	<input type="submit" value="${subscribeTxt}" /> --%>
				</form:form>
			</c:otherwise>
		</c:choose>
	</div>
	<a class="lightbox-close" href="#"></a>
</div>

<script type="text/javascript">
	$(document).ready(function(){
		$("#subscriptions").submit(function(e){ 
			e.preventDefault();  
			var action = $(this).attr("action");
			
			$.post(
				action,
				$(this).serialize(),
				function(data){
					$("#subscriptionsDiv").replaceWith(data);
					$.colorbox.resize();
				}
			);
		});
		$("a.lightbox-close").click(
			function(){
				$.colorbox.close();
				return(false);
			}
		);
	});
	
</script>
