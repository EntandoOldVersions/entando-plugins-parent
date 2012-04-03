<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<%-- START CICLO LINGUA --%>
<s:iterator value="langs" var="lang">
<div id="<s:property value="#lang.code" />_tab" class="tab">	
<h3 class="js_noscreen"><s:property value="#lang.descr" /> (<a class="backLink" href="#quickmenu" id="<s:property value="#lang.code" />_tab_quickmenu"><s:text name="note.goBackToQuickMenu" /></a>)</h3>

	<%-- START CICLO ATTRIBUTI --%>
	<s:if test="%{content.attributeList.size() > 0}">
		<s:iterator value="content.attributeList" id="attribute">
			<div class="contentAttributeBox">
			<%-- INIZIALIZZAZIONE TRACCIATORE --%>
			<s:set name="attributeTracer" value="initAttributeTracer(#attribute, #lang)" />
			
			<h4><s:property value="#attribute.name" /></h4>
				<s:if test="#attribute.type == 'Monotext' || #attribute.type == 'Text' || #attribute.type == 'Longtext' || #attribute.type == 'Enumerator'">
				<%-- ############# ATTRIBUTO TESTO MONOLINGUA ############# --%>
				<s:include value="/WEB-INF/apsadmin/jsp/entity/view/textAttribute.jsp" />
				</s:if>
				
				<s:if test="#attribute.type == 'Hypertext'">
				<%-- ############# ATTRIBUTO TESTO MONOLINGUA ############# --%>
				<s:include value="/WEB-INF/apsadmin/jsp/entity/view/hypertextAttribute.jsp" />
				</s:if>			
				
				<s:elseif test="#attribute.type == 'Boolean' || #attribute.type == 'CheckBox'">
				<%-- ############# ATTRIBUTO Boolean ############# --%>
				<s:include value="/WEB-INF/apsadmin/jsp/entity/view/checkBoxAttribute.jsp" />
				</s:elseif>
					
				<s:elseif test="#attribute.type == 'ThreeState'">
				<%-- ############# ATTRIBUTO ThreeState ############# --%>
				<s:include value="/WEB-INF/apsadmin/jsp/entity/view/threeStateAttribute.jsp" />
				</s:elseif>
				
				<s:elseif test="#attribute.type == 'Date'">
				<%-- ############# ATTRIBUTO Date ############# --%>
				<s:include value="/WEB-INF/apsadmin/jsp/entity/view/dateAttribute.jsp" />
				</s:elseif>
				
				<s:elseif test="#attribute.type == 'Number'">
				<%-- ############# ATTRIBUTO Number ############# --%>
				<s:include value="/WEB-INF/apsadmin/jsp/entity/view/numberAttribute.jsp" />
				</s:elseif>
				
				<s:elseif test="#attribute.type == 'Link'">
				<%-- ############# ATTRIBUTO Link ############# --%>
				<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/view/linkAttribute.jsp" />
				</s:elseif>
				
				<s:elseif test="#attribute.type == 'Image'">
				<%-- ############# ATTRIBUTO Image ############# --%>
				<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/view/imageAttribute.jsp" />
				</s:elseif>
				
				<s:elseif test="#attribute.type == 'Attach'">
				<%-- ############# ATTRIBUTO Attach ############# --%>
				<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/view/attachAttribute.jsp" />
				</s:elseif>
				
				<s:elseif test="#attribute.type == 'Composite'">
				<%-- ############# ATTRIBUTO Composite ############# --%>
				<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/view/compositeAttribute.jsp" />
				</s:elseif>
				
				<s:elseif test="#attribute.type == 'Monolist'">
				<%-- ############# ATTRIBUTO Monolist ############# --%>
				<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/view/monolistAttribute.jsp" />
				</s:elseif>
				
				<s:elseif test="#attribute.type == 'List'">
				<%-- ############# ATTRIBUTO Monolist ############# --%>
				<s:include value="/WEB-INF/apsadmin/jsp/entity/view/listAttribute.jsp" />
				</s:elseif>
			</div>
		</s:iterator>
	</s:if>
	<%-- END CICLO ATTRIBUTI --%>
</div>	
</s:iterator>
<%-- END CICLO LINGUA --%>