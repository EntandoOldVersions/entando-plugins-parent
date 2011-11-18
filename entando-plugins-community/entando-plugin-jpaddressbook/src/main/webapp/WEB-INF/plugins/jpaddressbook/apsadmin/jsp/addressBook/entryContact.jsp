<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<s:set var="targetNS" value="%{'/do/jpaddressbook/AddressBook'}" />
<h1><s:text name="title.contactManagement" /><jsp:include page="/WEB-INF/apsadmin/jsp/common/inc/operations-context-general.jsp" /></h1>
<div id="main">
	<h2>
		<s:if test="contact.id == null"><s:text name="jpaddressbook.title.newContact" /></s:if>
		<s:else><s:text name="jpaddressbook.title.edit" /></s:else>
	</h2>
	
	<s:form>
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
		
		<fieldset class="margin-more-top">
			<legend><s:text name="label.info" /></legend>
			<s:set name="lang" value="defaultLang" />
			<%-- START CICLO ATTRIBUTI --%>
			<s:iterator value="contact.attributes" var="attribute">
				<s:if test="#attribute.active">
					
					<%-- INIZIALIZZAZIONE TRACCIATORE --%>
					<wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" />
					<s:if test="#attribute.type == 'List' || #attribute.type == 'Monolist'">
					<p class="important">
						<span class="monospace">(<s:text name="label.list" />)&#32;</span><s:property value="#attribute.name" /><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" />:
					</p>
					</s:if>
					<s:elseif test="#attribute.type == 'Boolean' || #attribute.type == 'ThreeState'">
					<p class="important">
						<s:property value="#attribute.name" /><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" />:
					</p>
					</s:elseif>
					<s:elseif test="#attribute.type == 'CheckBox'">
					<%-- Leave this completely blank --%>
					</s:elseif>
					<s:else>
					<p> 
						<label for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />" class="basic-mint-label"><s:property value="#attribute.name" /><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" />:</label>
					</s:else>
					
					<s:if test="#attribute.type == 'Monotext'">
					<%-- ############# ATTRIBUTO TESTO MONOLINGUA ############# --%>
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/monotextAttribute.jsp" />
					</p>
					</s:if>
					
					<s:elseif test="#attribute.type == 'Text'">
					<%-- ############# ATTRIBUTO TESTO SEMPLICE MULTILINGUA ############# --%>
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
					</p>
					</s:elseif>
					
					<s:elseif test="#attribute.type == 'Longtext'">
					<%-- ############# ATTRIBUTO TESTOLUNGO ############# --%>
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/longtextAttribute.jsp" />
					</p>
					</s:elseif>
					
					<s:elseif test="#attribute.type == 'Hypertext'">
					<%-- ############# ATTRIBUTO TESTOLUNGO ############# --%>
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/hypertextAttribute.jsp" />
					</p>
					</s:elseif>
					
					<s:elseif test="#attribute.type == 'Boolean'">
					<%-- ############# ATTRIBUTO Boolean ############# --%>
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/booleanAttribute.jsp" />
					</s:elseif>
					
					<s:elseif test="#attribute.type == 'ThreeState'">
					<%-- ############# ATTRIBUTO ThreeState ############# --%>
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/threeStateAttribute.jsp" />
					</s:elseif>
					
					<s:elseif test="#attribute.type == 'Number'">
					<%-- ############# ATTRIBUTO Number ############# --%>
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/numberAttribute.jsp" />
					</p>
					</s:elseif>
					
					<s:elseif test="#attribute.type == 'Date'">
					<%-- ############# ATTRIBUTO Date ############# --%>
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/dateAttribute.jsp" />
					</p>
					</s:elseif>
					
					<s:elseif test="#attribute.type == 'Enumerator'">
					<%-- ############# ATTRIBUTO TESTO Enumerator ############# --%>
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/enumeratorAttribute.jsp" />
					</p>
					</s:elseif>
					
					<s:elseif test="#attribute.type == 'Monolist'">
					<%-- ############# ATTRIBUTO Monolist ############# --%>
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/monolistAttribute.jsp" />
					</s:elseif>
					
					<s:elseif test="#attribute.type == 'List'">
					<%-- ############# ATTRIBUTO List ############# --%>
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/listAttribute.jsp" />
					</s:elseif>
					
					<s:elseif test="#attribute.type == 'Composite'">
					<%-- ############# ATTRIBUTO Composite ############# --%>
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/compositeAttribute.jsp" />
					</p>
					</s:elseif>
					
					<s:elseif test="#attribute.type == 'CheckBox'">
					<%-- ############# ATTRIBUTO CheckBox ############# --%>
					<p><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/checkBoxAttribute.jsp" /></p>
					</s:elseif>

				</s:if>	
				
			</s:iterator>
			<%-- END CICLO ATTRIBUTI --%>
		</fieldset>
		<p class="centerText"><wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" action="save" /></p>
	</s:form>

</div>