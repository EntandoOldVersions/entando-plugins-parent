<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpmpp" uri="/jpmyportalplus-apsadmin-core" %>
<wpmpp:showletType key="%{#showletType.key}" property="swappable" var="swappableVar" />
<td class="icon">
	<s:if test="#swappableVar > -1">
	<s:if test="#swappableVar == 1">
		<img
			src="<wp:resourceURL/>plugins/jpmyportalplus/administration/common/img/icons/22x22/showlet-is-swappable.png" 
			alt="<s:text name="jpmyportalplus.showlet.swappable" />"
			title="<s:property value="#showletType.value" />: <s:text name="jpmyportalplus.showlet.swappable" />" 
		/>
	</s:if>
	<s:else>
		<img
			src="<wp:resourceURL/>plugins/jpmyportalplus/administration/common/img/icons/22x22/showlet-isnot-swappable.png" 
			alt="<s:text name="jpmyportalplus.showlet.not.swappable" />"
			title="<s:property value="#showletType.value" />: <s:text name="jpmyportalplus.showlet.not.swappable" />" 
		/>
	</s:else>
	</s:if>
	<s:else><abbr title="<s:text name="jpmyportalplus.showlet.na" />">&ndash;</abbr></s:else>
	<s:set var="swappableVar" value="null" />
</td>