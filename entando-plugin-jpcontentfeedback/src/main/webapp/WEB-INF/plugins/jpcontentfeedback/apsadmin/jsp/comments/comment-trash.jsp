<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>

<s:set var="commentVar" value="comment" />
<jacmswpsa:content contentId="%{#commentVar.contentId}" var="contentVar" />
<h1>
<a href="<s:url action="list" />"><s:text name="jpcontentfeedback.title.commentsManager" /></a>
</h1>
<div id="main">
	<h2><s:text name="jpcontentfeedback.label.delete.comment.confirm" /></h2>
	<div class="centerText">
		<dl class="table-display">
			<dt><s:text name="jpcontentfeedback.author" /></dt>
				<dd><s:property value="#commentVar.username"/></dd>
			<dt><s:text name="jpcontentfeedback.date.creation" /></dt>
				<dd><s:date name="#commentVar.creationDate" format="dd/MM/yyyy HH:mm" /></dd>
			<dt><s:text name="jpcontentfeedback.comment" /></dt>
				<dd><s:property value="#commentVar.comment"/></dd>
			<dt><s:text name="jpcontentfeedback.status" /></dt>
				<dd><s:property value="getAllStatus().get(#commentVar.status)" /></dd>
			<dt><s:text name="jpcontentfeedback.content.id" /></dt>
				<dd><s:property value="#commentVar.contentId" /></dd>
			<dt><s:text name="jpcontentfeedback.content.description" /></dt>
				<dd><s:property value="#contentVar.descr" /></dd>
			<dt><s:text name="jpcontentfeedback.content.type" /></dt>
				<dd><s:property value="#contentVar.typeDescr" /> (<s:property value="#contentVar.typeCode" />)</dd>
		</dl>
	</div>
	<s:form action="delete">
		
		<s:if test="hasActionMessages()">
			<div class="message message_confirm">
				<h3><s:text name="messages.confirm" /></h3>
					<ul>
						<s:iterator value="actionMessages">
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
		<s:if test="hasActionErrors()">
			<div class="message message_error">
				<h3><s:text name="message.title.ActionErrors" /></h3>
					<ul>
						<s:iterator value="actionErrors">
							<li><s:property/></li>
						</s:iterator>
					</ul>
			</div>
		</s:if>

		<p class="centerText">
			<wpsf:hidden name="selectedComment" />
			<wpsf:hidden name="strutsAction" />
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.yes')}" cssClass="button"/> 
			<wpsf:submit useTabindexAutoIncrement="true" action="list" value="%{getText('label.no')}" cssClass="button"/> 
		</p>
	</s:form>
</div>
