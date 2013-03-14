<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jpavatar" uri="/jpavatar-apsadmin-core" %>
 
<h1><s:text name="title.avatarManagement" /></h1>

<%-- 
	--TODO REMOVE
	<h2><s:text name="title.avatarEdit" /></h2>
--%>

<div id="main">
	<s:if test="hasActionErrors()">
		<div class="message message_error">
			<h2><s:text name="message.title.ActionErrors" /></h2>	
			<ul>
				<s:iterator value="actionErrors">
					<li><s:property escape="false" /></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	<s:if test="hasFieldErrors()">
		<div class="message message_error">
			<h2><s:text name="message.title.FieldErrors" /></h2>	
			<ul>
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	<s:if test="hasActionMessages()">
		<div class="message message_confirm">
			<h2><s:text name="messages.confirm" /></h2>	
			<ul>
				<s:iterator value="actionMessages">
					<li><s:property/></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	
	<jpavatar:avatar var="currentAvatar" returnDefaultAvatar="true" avatarStyleVar="style" />
	
	<c:choose>
		<c:when test="${style == 'gravatar'}">
			<p>
				<s:text name="jpavatar.label.current.avatar" />
			</p> 		
			<img src="<c:out value="${currentAvatar}" />"/>
		</c:when>
		<c:when test="${style == 'local'}">
			<p>
				<s:text name="jpavatar.label.current.avatar" />
			</p> 		
			<img src="<c:out value="${currentAvatar}" />"/>
			<s:if test="null == avatarResource">
				<s:form action="save" method="post" enctype="multipart/form-data">
					<p>
						<label for="jpavatar_file" class="basic-mint-label"><s:text name="label.avatarImage" />:</label> 
						<s:set var="fileTabIndex"><wpsa:counter /></s:set>
						<s:file name="avatar" tabindex="%{#fileTabIndex}"/>
						&#32;
						<wpsf:submit useTabindexAutoIncrement="true" cssClass="button" value="%{getText('label.ok')}" />
					</p>
				</s:form>
			</s:if>
			<s:else>
				<s:form action="bin">
					<p>
						<s:submit cssClass="button" value="%{getText('label.remove')}"/>
					</p>
				</s:form>
			</s:else>
		</c:when>
		<c:otherwise>
			style <c:out value="${style}" />
		</c:otherwise>
	</c:choose>

</div>