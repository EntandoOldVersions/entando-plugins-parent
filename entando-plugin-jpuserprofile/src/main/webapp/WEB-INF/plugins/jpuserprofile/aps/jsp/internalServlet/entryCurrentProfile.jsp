<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<h2><wp:i18n key="jpuserprofile_EDITPROFILE_TITLE" /></h2>

<c:choose>
	<c:when test="${sessionScope.currentUser != 'guest'}">
		
		<form action="<wp:action path="/ExtStr2/do/jpuserprofile/Front/CurrentUser/Profile/save.action" />" method="post" class="newContentForm">
			
			<s:if test="hasFieldErrors()">
				<h3><wp:i18n key="jpuserprofile_MESSAGE_TITLE_FIELDERRORS" /></h3>
				<ul>
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<li><s:property escape="false" /></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</s:if>
			
			<s:set name="lang" value="defaultLang" />
			
			<s:set name="publicCheckedValue" value="%{userProfile.publicProfile != null && userProfile.publicProfile == true}" />
			<p> 
				<label for="jpuserprofile_publicValue"><wp:i18n key="jpuserprofile_PUBLICCHECK" /></label>: 
				<wpsf:checkbox useTabindexAutoIncrement="true" id="jpuserprofile_publicValue" name="publicProfile" value="#publicCheckedValue" ></wpsf:checkbox>
			</p>
			
			<%-- START CICLO ATTRIBUTI --%>
			<s:iterator value="userProfile.attributeList" id="attribute">
				<s:if test="%{#attribute.active}">
					<%-- INIZIALIZZAZIONE TRACCIATORE --%>
					<wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" />
					<s:set var="i18n_attribute_name">jpuserprofile_<s:property value="userProfile.typeCode" />_<s:property value="#attribute.name" /></s:set>
					<s:set var="attribute_id">jpuserprofile_<s:property value="#attribute.name" /></s:set>
					<s:include value="/WEB-INF/plugins/jpuserprofile/aps/jsp/internalServlet/inc/iteratorAttribute.jsp" />
				</s:if>
			</s:iterator>
			<%-- END CICLO ATTRIBUTI --%>
			
			<s:set var="savelabel"><wp:i18n key="jpuserprofile_SAVE_PROFILE" /></s:set>
			<p><wpsf:submit useTabindexAutoIncrement="true" value="%{savelabel}" /></p>
		</form>
			
	</c:when>
	<c:otherwise>
		<p>
			<wp:i18n key="jpuserprofile_PLEASE_LOGIN" />.
		</p>
	</c:otherwise>
</c:choose>