<%@ taglib prefix="s" uri="/struts-tags" %>
<!-- ############# ATTRIBUTO ImageMap ############# -->
<s:if test="#attribute.type == 'ImageMap'">
	<s:include value="/WEB-INF/plugins/jpimagemap/apsadmin/jsp/content/modules/imageMapAttribute.jsp" />
</s:if>