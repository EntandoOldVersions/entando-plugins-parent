<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<s:include value="/WEB-INF/apsadmin/jsp/common/template/defaultExtraResources.jsp" />
<s:include value="/WEB-INF/apsadmin/jsp/common/template/extraresources/inc/snippet-calendar.jsp" />
<script type="text/javascript">
<!--//--><![CDATA[//><!--
//per tab di contenuto
window.addEvent('domready', function(){
	 var tabSet = new Taboo({
			tabs: "tab",
			tabTogglers: "tab-toggle",
			activeTabClass: "tab-current"
		});
});
//--><!]]></script>
