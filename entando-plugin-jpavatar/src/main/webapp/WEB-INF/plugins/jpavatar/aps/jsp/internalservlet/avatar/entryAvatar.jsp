<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jpavatar" uri="/jpavatar-apsadmin-core" %>
<div class="jpavatar">
	<h2><wp:i18n key="jpavatar_TITLE" /></h2>
	<c:choose>
		<c:when test="${sessionScope.currentUser != 'guest'}">
		
			<s:if test="hasActionErrors()">
				<div class="message message_error">
				<h3><s:text name="message.title.ActionErrors" /></h3>	
					<ul>
					<s:iterator value="actionErrors">
						<li><s:property escape="false" /></li>
					</s:iterator>
					</ul>
				</div>
			</s:if>
			
			<s:if test="hasFieldErrors()">
				<div class="message message_error">
				<h3><s:text name="message.title.FieldErrors" /></h3>	
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
				<h3><s:text name="messages.confirm" /></h3>	
				<ul>
					<s:iterator value="actionMessages">
						<li><s:property /></li>
					</s:iterator>
				</ul>
				</div>
			</s:if>

			<jpavatar:avatar var="currentAvatar" returnDefaultAvatar="true" avatarStyleVar="style" />
	
			<c:choose>
				<c:when test="${style == 'gravatar'}">
					<p>
						<wp:i18n key="jpavatar_CURRENT_AVATAR" />:<br />
					</p> 		
					<img src="<c:out value="${currentAvatar}" />"/>
				</c:when>
				<c:when test="${style == 'local'}">
					<s:if test="null == avatarResource">
						<p>
							<wp:i18n key="jpavatar_YOU_HAVE_NO_AVATAR" /><br />
						</p> 		
						<img src="<c:out value="${currentAvatar}" />"/>
					
					
						<form action="<wp:action path="/ExtStr2/do/jpavatar/Front/Avatar/save.action" />" method="post" enctype="multipart/form-data">
							<p>
								<label for="jpavatar_upload"><wp:i18n key="jpavatar_UPLOAD" /></label>:<br />
								<s:file name="avatar" id="jpavatar_upload" /> 
							</p>
							<p>
								<s:set var="upload"><wp:i18n key="jpavatar_GO_UPLOAD" /></s:set>
								<s:submit value="%{#upload}" />
							</p>
						</form>
					</s:if>
					<s:else>
						<p>
							<wp:i18n key="jpavatar_CURRENT_AVATAR" />:<br />
						</p> 
						<img src="<c:out value="${currentAvatar}" />"/>
						<form action="<wp:action path="/ExtStr2/do/jpavatar/Front/Avatar/bin.action" />" method="post">
							<p>
								<s:set var="delete"><wp:i18n key="jpavatar_DELETE" /></s:set>
								<s:submit value="%{#delete}"/> 
							</p>
		
						</form>
					</s:else>
				</c:when>
				<c:otherwise>
					style <c:out value="${style}" />
				</c:otherwise>
			</c:choose>

		</c:when>
	</c:choose>
</div>