<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<fieldset>
	<legend><s:text name="jpmyportalplus.widget.swappableflag.long" /></legend>
	<s:if test="strutsAction != 2 || #showletTypeVar.logic || 
		null == #showletTypeVar.typeParameters || #showletTypeVar.typeParameters.size() == 0">
	<p>
		<wpsf:checkbox id="jpmyportalplus_swappable" name="swappable" />
		<label for="jpmyportalplus_swappable"><s:text name="jpmyportal.label.widget.swappable" /></label>
	</p>
	</s:if>
	<s:else><p><s:text name="jpmyportalplus.widget.not.compatible" /></p></s:else>
</fieldset>