<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1><s:text name="title.messageManagement" /></h1>
<div id="main">

	<h2><s:text name="title.messageManagement.configuration" /></h2>
	<h3 class="margin-more-top"><s:text name="title.messagetype" />:&#32;<s:property value="%{messageType.descr}"/></h3>
	
	<s:set var="removeAddressImage" ><wp:resourceURL />administration/common/img/icons/list-remove.png</s:set>
	<s:form action="save">
		<s:if test="hasFieldErrors()">
			<div class="message message_error">
				<h3><s:text name="message.title.FieldErrors" /></h3>
				<ul>
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<li><s:property escape="true" /></li>
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
						<li><s:property escape="true" /></li>
					</s:iterator>
				</ul>
			</div>
		</s:if>
	
		<p class="noscreen">
			<wpsf:hidden name="typeCode" />
			<s:iterator value="recipientsTo" var="recipient" status="rowstatus">
				<wpsf:hidden name="recipientsTo" value="%{#recipient}" id="recipientsTo-%{#rowstatus.index}" />
			</s:iterator>
			<s:iterator value="recipientsCc" var="recipient" status="rowstatus">
				<wpsf:hidden name="recipientsCc" value="%{#recipient}" id="recipientsCc-%{#rowstatus.index}" />
			</s:iterator>
			<s:iterator value="recipientsBcc" var="recipient" status="rowstatus">
				<wpsf:hidden name="recipientsBcc" value="%{#recipient}" id="recipientsBcc-%{#rowstatus.index}" />
			</s:iterator>
		</p>
	
		<fieldset class="margin-bit-top">
			<legend><s:text name="webdynamicform" /></legend>
	
			<%--
			<p><s:text name="webdynamicform.intro" /></p>
			 --%>
	
			<p><s:text name="webdynamicform.parameters.summary" /></p>
			<dl class="table-display">
				<%--
				<dt><s:text name="label.store" /></dt>
				<dd><s:text name="label.store.help" /></dd>
				--%>
	
				<dt><s:text name="label.senderCode" /></dt>
				<dd><s:text name="label.sender.help" /></dd>
	
				<dt><s:text name="label.mailAttrName" /></dt>
				<dd><s:text name="label.attribute.email.help" /></dd>
	
				<dt><s:text name="label.subjectModel" /></dt>
				<dd><s:text name="label.subjectModel.help"></s:text></dd>
	
				<dt><s:text name="label.bodyModel" /></dt>
				<dd><s:text name="label.bodyModel.help" /></dd>
			</dl>
	
			<p>
				<wpsf:checkbox useTabindexAutoIncrement="true" id="jpwebdynamicform_store" name="store" cssClass="radiocheck" />&#32;
				<label for="jpwebdynamicform_store"><s:text name="label.local.message.store" /></label>
			</p>
	
			<p>
				<%-- Il mittente è quello di sistema che figurerà nelle mail agli operatori o all'utente
				E' obbligatorio solo se mailAttrName o notifiable sono true --%>
				<label for="jpwebdynamicform_sendercode" class="basic-mint-label"><s:text name="label.senderCode" />:</label>
				<wpsf:select useTabindexAutoIncrement="true" id="jpwebdynamicform_sendercode" list="senders" name="senderCode" listKey="key" listValue="value" headerKey="" headerValue="%{getText('label.select')}" />
			</p>
	
			<p>
				<%-- L'attributo dell'entità che contiene l'indirizzo eMail dell'utente del portale.
				Serve se si vuole consentire l'invio di eMail di risposta in back-end --%>
				<label for="jpwebdynamicform_mailattribute" class="basic-mint-label"><s:text name="label.mailAttrName" />:</label>
				<wpsf:select useTabindexAutoIncrement="true" id="jpwebdynamicform_mailattribute" list="textAttributes" name="mailAttrName" listKey="name" listValue="name" headerKey="" headerValue="%{getText('label.select')}" />
			</p>
	
			<p>
				<label for="jpwebdynamicform_subjectmodel" class="basic-mint-label"><s:text name="label.subjectModel" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" id="jpwebdynamicform_subjectmodel" name="subjectModel" />
			</p>
	
			<p>
				<label for="jpwebdynamicform_bodymodel" class="basic-mint-label"><s:text name="label.bodyModel" />:</label>
				<wpsf:textarea useTabindexAutoIncrement="true" name="bodyModel" rows="6" cols="60" />
			</p>
		</fieldset>
	
		<fieldset>
			<legend><s:text name="automatic.notification.receipt" /></legend>
	
			<p><s:text name="automatic.notification.receipt.intro" /></p>
	
			<%-- Indica se si vuole far notificare vial mail il messaggio agli operatori
			Se true, influenzerà la validazione (obbligatorietà) degli altri campi. --%>
			<p>
				<wpsf:checkbox useTabindexAutoIncrement="true" id="jpwebdynamicform_notifiable" name="notifiable" />&#32;
				<label for="jpwebdynamicform_notifiable"><s:text name="label.automatic.notification.active" /></label>
			</p>
	
			<h3 class="margin-more-top margin-bit-bottom"><s:text name="label.configured.recipients" /></h3>
			<p>
				<span class="important"><s:text name="label.recipientsTo" /></span>:<s:if test="%{recipientsTo == null || recipientsTo.size() == 0}">&#32;<s:text name="label.no.configured.recipients" /></s:if>
			</p>
	
			<s:else>
				<ul>
				<s:iterator value="recipientsTo" var="recipient" >
					<li>
						<wpsa:actionParam action="removeAddress" var="actionName" >
							<wpsa:actionSubParam name="recipientType" value="1" />
							<wpsa:actionSubParam name="address" value="%{#recipient}" />
						</wpsa:actionParam>
						<wpsf:submit useTabindexAutoIncrement="true" title="%{getText('label.remove')}: %{#recipient}" action="%{#actionName}" type="image" src="%{#removeAddressImage}" value="%{getText('label.delete')}" />:&#32;
						<s:property value="#recipient"/>
					</li>
				</s:iterator>
				</ul>
			</s:else>
	
			<p><span class="important"><s:text name="label.recipientsCc" /></span>:<s:if test="%{recipientsCc == null || recipientsCc.size() == 0}">&#32;<s:text name="label.no.configured.recipients" /></s:if></p>
			<s:else>
				<ul>
					<s:iterator value="recipientsCc" var="recipient" >
						<li>
						<wpsa:actionParam action="removeAddress" var="actionName" >
							<wpsa:actionSubParam name="recipientType" value="2" />
							<wpsa:actionSubParam name="address" value="%{#recipient}" />
						</wpsa:actionParam>
						<wpsf:submit useTabindexAutoIncrement="true" title="%{getText('label.remove')}: %{#recipient}" action="%{#actionName}" type="image" src="%{#removeAddressImage}" value="%{getText('label.delete')}" />:&#32;
						<s:property value="#recipient"/>
						</li>
					</s:iterator>
				</ul>
			</s:else>
	
			<p><span class="important"><s:text name="label.recipientsBcc" /></span>:<s:if test="%{recipientsBcc == null || recipientsBcc.size() == 0}">&#32;<s:text name="label.no.configured.recipients" /></s:if></p>
			<s:else>
				<ul>
					<s:iterator value="recipientsBcc" var="recipient" >
						<li>
						<wpsa:actionParam action="removeAddress" var="actionName" >
							<wpsa:actionSubParam name="recipientType" value="3" />
							<wpsa:actionSubParam name="address" value="%{#recipient}" />
						</wpsa:actionParam>
						<wpsf:submit useTabindexAutoIncrement="true" title="%{getText('label.remove')}: %{#recipient}" action="%{#actionName}" type="image" src="%{#removeAddressImage}" value="%{getText('label.delete')}" />:&#32;
						<s:property value="#recipient"/>
						</li>
					</s:iterator>
				</ul>
			</s:else>
	
			<h3 class="margin-more-top margin-bit-bottom"><s:text name="addrecipent" /></h3>
			<p>
				<label for="jpwebdynamicform_addrectype" class="basic-mint-label"><s:text name="label.recipientType" />:</label>
				<wpsf:select useTabindexAutoIncrement="true" list="#{1: getText('label.recipient.to'), 2: getText('label.recipient.cc'), 3: getText('label.recipient.bcc')}" name="recipientType" id="jpwebdynamicform_addrectype" />
			</p>
			<p>
				<label for="jpwebdynamicform_addrecaddress" class="basic-mint-label"><s:text name="label.address" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" id="jpwebdynamicform_addrecaddress" name="address" />
			</p>
			<p>
				<wpsf:submit useTabindexAutoIncrement="true" cssClass="button" action="addAddress" value="%{getText('label.addAddress')}" />
			</p>
		</fieldset>
	
		<p class="centerText">
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" />
		</p>
	</s:form>
	
</div>