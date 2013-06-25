<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1><wp:i18n key="jpuserprofile_EDITPROFILE_TITLE" /></h1>
<c:choose>
	<c:when test="${sessionScope.currentUser != 'guest'}">
		
		<form 
			action="<wp:action path="/ExtStr2/do/jpuserprofile/Front/CurrentUser/Profile/save.action" />" 
			method="post" 
			class="form-horizontal">
			
			<s:if test="hasFieldErrors()">
				<div class="alert alert-block">
					<p><strong><wp:i18n key="jpuserprofile_MESSAGE_TITLE_FIELDERRORS" /></strong></p>
					<ul class="unstyled">
						<s:iterator value="fieldErrors">
							<s:iterator value="value">
								<li><s:property escape="false" /></li>
							</s:iterator>
						</s:iterator>
					</ul>
				</div>
			</s:if>
			
			<s:set name="lang" value="defaultLang" />

			<div class="control-group public-profile">
				<s:set name="publicCheckedValue" value="%{userProfile.publicProfile != null && userProfile.publicProfile == true}" />
				
				<label class="control-label" for="jpuserprofile_publicValue"><wp:i18n key="jpuserprofile_PUBLICCHECK" /></label>
				<div class="controls">
					<wpsf:checkbox 
						useTabindexAutoIncrement="true" 
						name="publicProfile" 
						id="jpuserprofile_publicValue" 
						value="#publicCheckedValue" />
				</div>
			</div>

			<%-- Attributes --%>
			<s:iterator value="userProfile.attributeList" id="attribute">
				<s:if test="%{#attribute.active}">
					<%-- INIZIALIZZAZIONE TRACCIATORE --%>
					<wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" />
					<s:set var="i18n_attribute_name">jpuserprofile_<s:property value="userProfile.typeCode" />_<s:property value="#attribute.name" /></s:set>
					<s:set var="attribute_id">jpuserprofile_<s:property value="#attribute.name" /></s:set>
					<s:include value="/WEB-INF/plugins/jpuserprofile/aps/jsp/internalServlet/inc/iteratorAttribute.jsp" />
				</s:if>
			</s:iterator>
			<%-- Attributes //end --%>
			
			<p class="form-actions">
				<wp:i18n key="jpuserprofile_SAVE_PROFILE" var="jpuserprofile_SAVE_PROFILE" />
				<wpsf:submit 
					useTabindexAutoIncrement="true" 
					value="%{#attr.jpuserprofile_SAVE_PROFILE}" 
					cssClass="btn btn-primary"
					/>
			</p>
		</form>
			
	</c:when>
	<c:otherwise>
		<p>
			<wp:i18n key="jpuserprofile_PLEASE_LOGIN" />
		</p>
	</c:otherwise>
</c:choose>