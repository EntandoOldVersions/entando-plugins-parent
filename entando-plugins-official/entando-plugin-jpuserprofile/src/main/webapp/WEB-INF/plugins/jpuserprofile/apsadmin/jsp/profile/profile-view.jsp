<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<h1><s:text name="jpuserprofile.menu.profileAdmin" /></h1>

<div id="main">
	<h2 class="margin-more-bottom"><s:text name="jpuserprofile.title.profileDetail" /></h2>
	
	<s:set name="lang" value="defaultLang" />
	<s:set var="userProfileVar" value="%{getUserProfile(username)}" />
	<s:if test="%{null != #userProfileVar}">
		<div class="centerText">
			<dl class="table-display">
				<dt><s:text name="jpuserprofile.label.username" /></dt>
				<dd><s:property value="username" /> </dd>
				<s:set name="lang" value="defaultLang" />
				<s:iterator value="#userProfileVar.attributeList" id="attribute">
				<%-- INIZIALIZZAZIONE TRACCIATORE --%>
				<s:set name="attributeTracer" value="initAttributeTracer(#attribute, #lang)" />
					<%-- VISUALIZZAZIONE CONTENUTO ATTRIBUTI  --%>	
					<dt><s:property value="#attribute.name" />:</dt>
					<dd>
						<%-- ############# ATTRIBUTO TESTO MONOLINGUA ############# --%>
						<s:if test="#attribute.type == 'Monotext'">
							<s:include value="/WEB-INF/apsadmin/jsp/entity/view/monotextAttribute.jsp" />
						</s:if>
					
						<%-- ############# ATTRIBUTO TESTO SEMPLICE MULTILINGUA ############# --%>
						<s:elseif test="#attribute.type == 'Text'">
							<s:include value="/WEB-INF/apsadmin/jsp/entity/view/textAttribute.jsp" />
						</s:elseif>
					
						<%-- ############# ATTRIBUTO TESTOLUNGO ############# --%>
						<s:elseif test="#attribute.type == 'Longtext'">
							<s:include value="/WEB-INF/apsadmin/jsp/entity/view/longtextAttribute.jsp" />	
						</s:elseif>
					
						<%-- ############# ATTRIBUTO HYPERTEXT ############# --%>
						<s:elseif test="#attribute.type == 'Hypertext'">
							<s:include value="/WEB-INF/apsadmin/jsp/entity/view/hypertextAttribute.jsp" />	
						</s:elseif>
					
						<%-- ############# ATTRIBUTO Boolean ############# --%>
						<s:elseif test="#attribute.type == 'Boolean'">
							<s:include value="/WEB-INF/apsadmin/jsp/entity/view/booleanAttribute.jsp" />
						</s:elseif>
					
						<%-- ############# ATTRIBUTO ThreeState ############# --%>
						<s:elseif test="#attribute.type == 'ThreeState'">
							<s:include value="/WEB-INF/apsadmin/jsp/entity/view/threeStateAttribute.jsp" />
						</s:elseif>
					
						<%-- ############# ATTRIBUTO Number ############# --%>
						<s:elseif test="#attribute.type == 'Number'">
							<s:include value="/WEB-INF/apsadmin/jsp/entity/view/numberAttribute.jsp" />
						</s:elseif>
					
						<%-- ############# ATTRIBUTO Date ############# --%>
						<s:elseif test="#attribute.type == 'Date'">
							<s:include value="/WEB-INF/apsadmin/jsp/entity/view/dateAttribute.jsp" />
						</s:elseif>
					
						<%-- ############# ATTRIBUTO TESTO Enumerator ############# --%>
						<s:elseif test="#attribute.type == 'Enumerator'">
							<s:include value="/WEB-INF/apsadmin/jsp/entity/view/enumeratorAttribute.jsp" />
						</s:elseif>
				
						<%-- ############# ATTRIBUTO Monolist ############# --%>
						<s:elseif test="#attribute.type == 'Monolist'">
							<s:include value="/WEB-INF/apsadmin/jsp/entity/view/monolistAttribute.jsp" />
						</s:elseif>
					
						<%-- ############# ATTRIBUTO List ############# --%>
						<s:elseif test="#attribute.type == 'List'">
							<s:include value="/WEB-INF/apsadmin/jsp/entity/view/listAttribute.jsp" />
						</s:elseif>
				
						<%-- ############# ATTRIBUTO Composite ############# --%>
						<s:elseif test="#attribute.type == 'Composite'">
							<s:include value="/WEB-INF/apsadmin/jsp/entity/view/compositeAttribute.jsp" />
						</s:elseif>
						
						<%-- ############# ATTRIBUTO CheckBox ############# --%>
						<s:elseif test="#attribute.type == 'CheckBox'">
							<s:include value="/WEB-INF/apsadmin/jsp/entity/view/checkBoxAttribute.jsp" />
						</s:elseif>
					</dd>
				</s:iterator>
			</dl>
		</div>
	</s:if>
	
	<p>
		<a href="<s:url namespace="/do/jpuserprofile" action="list" />" ><s:text name="jpuserprofile.note.returnTo.search" /></a> 
	</p>
</div>