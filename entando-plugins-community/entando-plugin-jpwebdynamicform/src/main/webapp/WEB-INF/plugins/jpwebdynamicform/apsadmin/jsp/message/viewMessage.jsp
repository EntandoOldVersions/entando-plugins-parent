<%@ taglib prefix="s" uri="/struts-tags" %>
<h1><a href="<s:url action="list" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.messageManagement" />"><s:text name="title.messageManagement" /></a></h1>
<div id="main">
	<h2><s:text name="title.messageManagement.details" /></h2>
	<s:set name="id" value="message.id" />
	<s:set name="typeDescr" value="message.typeDescr" />
	
	<h3 class="margin-more-top"><s:text name="title.message.original" /></h3>
	<s:if test="%{answers.size()==0}">
		<p><s:text name="title.messageManagement.details.info" />:&#32;<em><s:property value="#typeDescr"/></em>&#32;(<s:text name="%{#id}"/>)</p>
		<s:include value="inc/include_messageDetails.jsp" />
	</s:if>
	<s:else>
		<p><s:text name="title.messageManagement.details.info" />:&#32;<em><s:property value="#typeDescr"/></em>&#32;(<s:text name="%{#id}"/>)</p>
		<s:include value="inc/include_messageDetails.jsp" />
		<h3 class="margin-more-top"><s:text name="title.message.answers" /></h3>
			<s:iterator var="answer" value="answers">
				<dl class="table-display">
					<dt><s:date name="#answer.sendDate" format="dd/MM/yyyy - HH:mm"/></dt>
					<dd><s:property value="%{#answer.text}" /></dd>
				</dl>
			</s:iterator>
	</s:else>
</div>