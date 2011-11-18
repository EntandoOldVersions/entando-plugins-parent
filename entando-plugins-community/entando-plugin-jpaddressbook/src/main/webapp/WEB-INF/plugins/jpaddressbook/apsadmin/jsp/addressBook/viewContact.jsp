<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<s:set var="targetNS" value="%{'/do/jpaddressbook/AddressBook'}" />
<h1><s:text name="title.contactManagement" /><jsp:include page="/WEB-INF/apsadmin/jsp/common/inc/operations-context-general.jsp" /></h1>

<div id="main">
	
	<h2><s:text name="jpaddressbook.title.details" /></h2>
	
	<%-- START CICLO ATTRIBUTI --%>
	<s:set name="lang" value="defaultLang" /> 
	<dl class="table-display">
		<s:iterator value="contact.attributes" id="attribute">
			<s:if test="#attribute.active">
				<%-- INIZIALIZZAZIONE TRACCIATORE --%>
				<wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" />
				
				<s:if test="%{#attribute.type == 'Monotext' && !(#attribute.getTextForLang(#lang.code) == null || #attribute.getTextForLang(#lang.code) == '')}">
					<%-- ############# ATTRIBUTO TESTO MONOLINGUA ############# --%>
					<dt>
						<span class="important"><s:property value="%{#attribute.name}" /></span>
					</dt>
					<dd>
						<s:if test="%{#attribute.getTextForLang(#lang.code) == null || #attribute.getTextForLang(#lang.code) == ''}">
							&ndash;
						</s:if>
						<s:else>
							<s:property value="%{#attribute.getTextForLang(#lang.code)}" />
						</s:else>
					</dd>
				</s:if>
				
				<s:elseif test="%{#attribute.type == 'Text' && !(#attribute.getTextForLang(#lang.code) == null || #attribute.getTextForLang(#lang.code) == '')}">
					<%-- ############# ATTRIBUTO TESTO ############# --%>
					<dt>
						<span class="important"><s:property value="%{#attribute.name}" /></span>
					</dt>
					<dd>
						<s:if test="%{#attribute.getTextForLang(#lang.code) == null || #attribute.getTextForLang(#lang.code) == ''}">
							&ndash;
						</s:if>
						<s:else>				
							<s:property value="%{#attribute.getTextForLang(#lang.code)}" />
						</s:else>
					</dd>
				</s:elseif>
				
				<s:elseif test="%{#attribute.type == 'Longtext' && !(#attribute.getTextForLang(#lang.code) == null || #attribute.getTextForLang(#lang.code) == '')}">
					<%-- ############# ATTRIBUTO Longtext ############# --%>
					<dt>
						<span class="important"><s:property value="%{#attribute.name}" /></span>
					</dt>
					<dd>
						<s:if test="%{#attribute.getTextForLang(#lang.code) == null || #attribute.getTextForLang(#lang.code) == ''}">
							&ndash;
						</s:if>
						<s:else>	
							<s:property value="%{#attribute.getTextForLang(#lang.code)}" />
						</s:else>				
					</dd>
				</s:elseif>
				
				<s:elseif test="%{#attribute.type == 'Hypertext' && !(#attribute.textMap[#lang.code] == null || #attribute.textMap[#lang.code] == '')}">
					<%-- ############# ATTRIBUTO Hypertext ############# --%>
					<dt>
						<span class="important"><s:property value="%{#attribute.name}" /></span>
					</dt>
					<dd>
						<s:if test="%{#attribute.textMap[#lang.code] == null || #attribute.textMap[#lang.code] == ''}">
							&ndash;
						</s:if> 
						<s:else>	
							<s:property value="%{#attribute.textMap[#lang.code]}" />
						</s:else>				
					</dd>
				</s:elseif>
				
				<s:elseif test="#attribute.type == 'Boolean'">
					<%-- ############# ATTRIBUTO Boolean ############# --%>
					<dt>
						<span class="important"><s:property value="%{#attribute.name}" /></span>
					</dt>
					<dd>
						<s:if test="%{#attribute.value == true}"><s:text name="label.yes"/></s:if>
						<s:else><s:text name="label.no"/></s:else>
					</dd>
				</s:elseif>
				
				<s:elseif test="#attribute.type == 'ThreeState'">
					<%-- ############# ATTRIBUTO ThreeState ############# --%>
					<dt>
						<span class="important"><s:property value="%{#attribute.name}" /></span>
					</dt>
					<dd>
						<s:if test="%{#attribute.booleanValue == null}"><s:text name="label.bothYesAndNo"/></s:if>
						<s:elseif test="%{#attribute.booleanValue != null && #attribute.booleanValue == true}"><s:text name="label.yes"/></s:elseif>
						<s:elseif test="%{#attribute.booleanValue != null && #attribute.booleanValue == false}"><s:text name="label.no"/></s:elseif>			
					</dd>
				</s:elseif>
				
				<s:elseif test="%{#attribute.type == 'Number' !(#attribute.value == null || #attribute.value == '')}">
					<%-- ############# ATTRIBUTO Number ############# --%>
					<dt>
						<span class="important"><s:property value="%{#attribute.name}" /></span>
					</dt>
					<dd>
						<s:if test="%{#attribute.value == null || #attribute.value == ''}">
							&ndash;
						</s:if> 
						<s:else>	
							<s:property value="#attribute.value" />
						</s:else>
					</dd>
				</s:elseif>
				
				<s:elseif test="%{#attribute.type == 'Date' !(#attribute.getFormattedDate('dd/MM/yyyy') == null || #attribute.getFormattedDate('dd/MM/yyyy') == '')}">
					<%-- ############# ATTRIBUTO Date ############# --%>
					<dt>
						<span class="important"><s:property value="%{#attribute.name}" /></span>
					</dt>
					<dd>
						<s:if test="%{#attribute.getFormattedDate('dd/MM/yyyy') == null || #attribute.getFormattedDate('dd/MM/yyyy') == ''}">
							&ndash;
						</s:if>
						<s:else>
							<s:property value="#attribute.getFormattedDate('dd/MM/yyyy')" />
						</s:else>
					</dd>
				</s:elseif>
				
				<s:elseif test="%{#attribute.type == 'Enumerator' !(#attribute.getText() == null || #attribute.getText() == '')}">
					<%-- ############# ATTRIBUTO TESTO Enumerator ############# --%>
					<dt>
						<span class="important"><s:property value="%{#attribute.name}" /></span>
					</dt>
					<dd>
						<s:if test="%{#attribute.getText() == null || #attribute.getText() == ''}">
							&ndash;
						</s:if>
						<s:else>
							<s:property value="%{#attribute.getText()}" />
						</s:else>				
					</dd>
				</s:elseif>
				
				<s:elseif test="#attribute.type == 'CheckBox'">
					<%-- ############# ATTRIBUTO CheckBox ############# --%>
					<dt>
						<span class="important"><s:property value="%{#attribute.name}" /></span>
					</dt>
					<dd>
						<s:set name="checkedValue" value="%{#attribute.booleanValue != null && #attribute.booleanValue ==true}" />
						<s:if test="%{#checkedValue}"><s:text name="label.yes"/></s:if>
						<s:else><s:text name="label.no"/></s:else> 
					</dd>
				</s:elseif>
				
			</s:if> 
		</s:iterator> 
		<dt><span class="important">Contatto Pubblico</span></dt>
		<dd>
			<s:if test="contact.publicContact"><s:text name="label.yes"/></s:if>
			<s:else><s:text name="label.no"/></s:else>
		</dd>
	</dl>
	
	<%-- liste --%>
	<s:iterator value="contact.attributes" var="attribute">

		<s:if test="#attribute.type == 'Monolist'">
			<%-- ############# ATTRIBUTO Monolist ############# --%>
			<%-- TODO TO-DO --%>
			<h3><s:property value="#attribute.name" /></h3>
			<s:if test="%{#attribute == null || #attribute == ''}">
				&ndash;
			</s:if>
			<s:else>
				<s:include value="/WEB-INF/apsadmin/jsp/entity/view/monolistAttribute.jsp" />
			</s:else>				
		</s:if>
			
		<s:elseif test="#attribute.type == 'List'">
			<%-- ############# ATTRIBUTO List ############# --%>
			<%-- TODO TO-DO --%>
			<h3><s:property value="#attribute.name" /></h3>
			<s:if test="%{#attribute == null || #attribute == ''}">
				&ndash;
			</s:if>
			<s:else>
				<s:include value="/WEB-INF/apsadmin/jsp/entity/view/listAttribute.jsp" />
			</s:else>
		</s:elseif>
			
		<s:elseif test="#attribute.type == 'Composite'">
			<%-- ############# ATTRIBUTO Composite ############# --%>
			<%-- TODO TO-DO --%>
			<h3><s:property value="#attribute.name" /></h3>
			<s:if test="%{#attribute == null || #attribute == ''}">
				&ndash;
			</s:if>
			<s:else>
				<s:include value="/WEB-INF/apsadmin/jsp/entity/view/compositeAttribute.jsp" />
			</s:else>
		</s:elseif>

	</s:iterator> 
	<%-- END CICLO ATTRIBUTI --%>
</div>