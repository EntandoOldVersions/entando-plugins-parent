<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<div class="centerText">
<dl class="table-display">
	<dt><s:text name="label.user" />:</dt>
	<dd><s:property value="%{message.username}" /></dd>

	<dt><s:text name="label.creationDate" />:</dt>
	<dd><s:date name="%{message.creationDate}" format="dd/MM/yyyy HH:mm" /></dd>

<s:set name="lang" value="defaultLang" />
<s:iterator value="message.attributeList" id="attribute">
<%-- INIZIALIZZAZIONE TRACCIATORE --%>
<s:set name="attributeTracer" value="initAttributeTracer(#attribute, #lang)"></s:set>
	<dt><s:property value="#attribute.name" />:</dt>

	<dd>

	<s:if test="#attribute.type == 'Boolean' || #attribute.type == 'CheckBox'">
		<!-- ############# ATTRIBUTO Boolean ############# -->
		<s:include value="/WEB-INF/plugins/jpwebdynamicform/apsadmin/jsp/message/modules/view/booleanAttribute.jsp" />
	</s:if>

	<s:elseif test="#attribute.type == 'ThreeState'">
		<!-- ############# ATTRIBUTO ThreeState ############# -->
		<s:include value="/WEB-INF/plugins/jpwebdynamicform/apsadmin/jsp/message/modules/view/threeStateAttribute.jsp" />
	</s:elseif>

	<s:elseif test="#attribute.type == 'Date'">
		<!-- ############# ATTRIBUTO ThreeState ############# -->
		<s:include value="/WEB-INF/plugins/jpwebdynamicform/apsadmin/jsp/message/modules/view/dateAttribute.jsp" />
	</s:elseif>

	<s:elseif test="#attribute.type == 'Number'">
		<!-- ############# ATTRIBUTO ThreeState ############# -->
		<s:include value="/WEB-INF/plugins/jpwebdynamicform/apsadmin/jsp/message/modules/view/numberAttribute.jsp" />
	</s:elseif>

	<s:elseif test="#attribute.type == 'Composite'">
		<!-- ############# ATTRIBUTO Composite ############# -->
		<s:include value="/WEB-INF/plugins/jpwebdynamicform/apsadmin/jsp/message/modules/view/compositeAttribute.jsp" />
	</s:elseif>

	<s:else>
		<!-- ############# ATTRIBUTO Monotext (Text, Longtext, Enumerator, ecc.) ############# -->
		<s:include value="/WEB-INF/plugins/jpwebdynamicform/apsadmin/jsp/message/modules/view/monotextAttribute.jsp" />
	</s:else>
	</dd>
</s:iterator>
</dl>
</div>
