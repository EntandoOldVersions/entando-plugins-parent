<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="#attribute.type == 'Coords'">
<p class="important"><s:property value="#attribute.name" /><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" />:</p>
<!-- ############# ATTRIBUTO Coords ############# -->
<s:include value="/WEB-INF/plugins/jpgeoref/apsadmin/jsp/content/modules/coordsAttribute.jsp" />
</s:if>
