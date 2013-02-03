<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="jpavatar" uri="/jpavatar-apsadmin-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1><s:text name="title.avatarManagement" /></h1>
<div id="main">
	<s:form action="delete">
		<p>
			<s:text name="jpavatar.label.confirm.delete" />&#32;
		</p>
		<jpavatar:avatar var="currentAvatar" returnDefaultAvatar="true"  />
		<img src="<c:out value="${currentAvatar}" />"/>
		<p>
			<s:submit value="%{getText('label.remove')}" cssClass="button" />
		</p>
		<p>
			<a href="<s:url action="edit" />"><s:text name="label.back" /></a>
		</p>
	</s:form>
</div>