<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1><s:text name="jpcontentfeedback.title.commentsManager" /></h1>
<div id ="main">
	<h2 class="margin-more-bottom"><s:text name="jpcontentfeedback.title.comment.list" /></h2>
	
	<s:form action="search">
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
		
		<p>
			<s:set var="allStatus" value="%{getAllStatus()}" />
			<label for="status" class="basic-mint-label label-search"><s:text name="label.search.by" />&#32;<s:text name="jpcontentfeedback.status" />:</label>
			<wpsf:select useTabindexAutoIncrement="true" list="#allStatus" name="status" id="status" listKey="key" listValue="value" headerKey="" headerValue="%{getText('label.all')}" />
		</p>
	
		<fieldset><legend class="accordion_toggler"><s:text name="title.searchFilters" /></legend>
		<div class="accordion_element" >
			<p>
				<label for="comment" class="basic-mint-label"><s:text name="jpcontentfeedback.comment" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="text" id="comment" cssClass="text"/>
			</p>
			<p>
				<label for="author" class="basic-mint-label"><s:text name="jpcontentfeedback.author" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="author" id="author" cssClass="text"/>
			</p>
			<p>
				<label for="from_cal" class="basic-mint-label"><s:text name="jpcontentfeedback.date.from" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="from" id="from_cal" value="%{from}" cssClass="text"/>&#32;<span class="inlineNote">dd/MM/yyyy</span>
			</p>
			<p>
				<label for="to_cal" class="basic-mint-label"><s:text name="jpcontentfeedback.date.to" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="to" id="to_cal" value="%{to}" cssClass="text"/>&#32;<span class="inlineNote">dd/MM/yyyy</span>
			</p>
		</div>
		</fieldset>
		<p>
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.search')}" cssClass="button" />
		</p>
	
	
	<div class="subsection-light">
	
	<wpsa:subset source="commentIds" count="10" objectName="groupComment" advanced="true" offset="5">
	<s:set name="group" value="#groupComment" />
	
	<div class="pager">
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
	</div>

		<s:set var="lista" value="commentIds" />
		<s:if test="!#lista.empty">
			<table class="generic" summary="<s:text name="jpcontentfeedback.note.comment.list" />">
				<caption><span><s:text name="jpcontentfeedback.title.comment.list" /></span></caption>
				<tr>
					<th><s:text name="jpcontentfeedback.author" /></th>
					<th><s:text name="jpcontentfeedback.date.creation" /></th>
					<th><s:text name="jpcontentfeedback.status" /></th>
					<th>-</th>
				</tr>
				<s:iterator var="commentoId">
					<tr>
					<s:set var="commento" value="%{getComment(#commentoId)}" />
						<td><s:property value="#commento.username"/></td>
						<td class="centerText monospace"><s:date name="#commento.creationDate" format="dd/MM/yyyy HH:mm" /></td>
						<td><s:text name="%{'jpcontentfeedback.label.' + #commento.status}" /></td>
						<td class="icon_double">
							<a href="<s:url action="view"><s:param name="selectedComment" value="#commentoId" /></s:url>" title="<s:text name="label.edit" />:&#32;<s:date name="#commento.creationDate" format="dd/MM/yyyy HH:mm" />" ><img src="<wp:resourceURL />administration/common/img/icons/edit-content.png" alt="<s:text name="label.edit" />" /></a>
							<a href="<s:url action="trash"><s:param name="selectedComment" value="#commentoId" /></s:url>" title="<s:text name="label.remove" />:&#32;<s:date name="#commento.creationDate" format="dd/MM/yyyy HH:mm" />" ><img src="<wp:resourceURL />administration/common/img/icons/delete.png" alt="<s:text name="label.remove" />" /></a>
						</td>
					</tr>
				</s:iterator>
			</table>
		</s:if>
		<s:else><p><s:text name="jpcontentfeedback.note.list.empty" /></p></s:else>
	</div>
	
	<div class="pager">
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
	</div>
	
	</wpsa:subset>
</s:form>
</div>