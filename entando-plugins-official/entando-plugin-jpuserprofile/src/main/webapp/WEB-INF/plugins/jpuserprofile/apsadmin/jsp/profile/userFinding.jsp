<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:set var="targetNS" value="%{'/do/jpuserprofile'}" />
<h1><s:text name="jpuserprofile.title.profileManagement" /><%-- <s:include value="/WEB-INF/apsadmin/jsp/common/inc/operations-context-general.jsp" /> --%></h1>

<div id="main">

<s:form action="search">
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

<p><label for="text" class="basic-mint-label label-search"><s:text name="label.search.by"/>&#32;<s:text name="label.username"/>:</label>
<wpsf:textfield useTabindexAutoIncrement="true" name="text" id="text" cssClass="text" /></p>

<p><label for="withProfile" class="basic-mint-label label-search"><s:text name="label.search.hasProfile"/>:</label>
	<wpsf:select useTabindexAutoIncrement="true" name="withProfile" id="withProfile" 
		list="#{1:getText('label.yes'),2:getText('label.no')}" headerKey="" headerValue="%{getText('label.bothYesAndNo')}" />
</p>

<p>
	<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.search')}" cssClass="button" />
</p>

<div class="subsection-light">

<wpsa:subset source="users" count="10" objectName="groupUser" advanced="true" offset="5">
<s:set name="group" value="#groupUser" />

<div class="pager">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

<table class="generic" summary="<s:text name="note.userList.summary" />">
<caption><span><s:text name="title.userManagement.userList" /></span></caption>
<tr>
	<th><s:text name="jpuserprofile.label.username" /></th>
	<th><s:text name="jpuserprofile.label.name" /></th>
	<th><s:text name="jpuserprofile.label.mail" /></th>
	<th class="icon"><abbr title="<s:text name="label.state" />">S</abbr></th>	
</tr>
<s:iterator id="user">

<s:if test="!#user.japsUser">
	<s:set name="statusIconImagePath" id="statusIconImagePath"><wp:resourceURL/>administration/common/img/icons/user-status-notjAPSUser.png</s:set>
	<s:set name="statusIconText" id="statusIconText"><s:text name="note.userStatus.notjAPSUser" /></s:set>
</s:if>
<s:elseif test="#user.disabled">
	<s:set name="statusIconImagePath" id="statusIconImagePath"><wp:resourceURL/>administration/common/img/icons/user-status-notActive.png</s:set>
	<s:set name="statusIconText" id="statusIconText"><s:text name="note.userStatus.notActive" /></s:set>	
</s:elseif>
<s:elseif test="!#user.accountNotExpired">
	<s:set name="statusIconImagePath" id="statusIconImagePath"><wp:resourceURL/>administration/common/img/icons/user-status-expiredAccount.png</s:set>
	<s:set name="statusIconText" id="statusIconText"><s:text name="note.userStatus.expiredAccount" /></s:set>	
</s:elseif>
<s:elseif test="!#user.credentialsNotExpired">
	<s:set name="statusIconImagePath" id="statusIconImagePath"><wp:resourceURL/>administration/common/img/icons/user-status-expiredPassword.png</s:set>
	<s:set name="statusIconText" id="statusIconText"><s:text name="note.userStatus.expiredPassword" /></s:set>	
</s:elseif>
<s:elseif test="!#user.disabled">
	<s:set name="statusIconImagePath" id="statusIconImagePath"><wp:resourceURL/>administration/common/img/icons/user-status-active.png</s:set>
	<s:set name="statusIconText" id="statusIconText"><s:text name="note.userStatus.active" /></s:set>
</s:elseif>

<tr>
	<td><a href="<s:url namespace="/do/jpuserprofile" action="edit"><s:param name="username" value="#user.username"/></s:url>" title="<s:text name="label.edit" />: <s:property value="#user.username" />" ><s:property value="#user" /></a></td>
	<s:set var="userProfileVar" value="null" />
	<wpsa:entity entityManagerName="jpuserprofileUserProfileManager" key="%{#user.username}" var="userProfileVar"/>
	
	<td class="centerText monospace">
		<s:if test="null != #userProfileVar">
		<s:property value="#userProfileVar.getValue(#userProfileVar.firstNameAttributeName)"/>&nbsp;<s:property value="#userProfileVar.getValue(#userProfileVar.surnameAttributeName)"/>
		</s:if>
		<s:else><abbr title="<s:text name="jpuserprofile.label.noProfile" />">&ndash;</abbr></s:else>
	</td>
	
	<td class="centerText monospace">
		<s:if test="null != #userProfileVar">
		<s:property value="#userProfileVar.getValue(#userProfileVar.mailAttributeName)"/>
		</s:if>
		<s:else><abbr title="<s:text name="jpuserprofile.label.noProfile" />">&ndash;</abbr></s:else>
	</td>
	
	<td class="icon"><img src="<s:property value="#statusIconImagePath" />" alt="<s:property value="#statusIconText" />" title="<s:property value="#statusIconText" />" /></td>
</tr>
</s:iterator>
</table>

<div class="pager">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

</wpsa:subset>

</div>

</s:form>

</div>