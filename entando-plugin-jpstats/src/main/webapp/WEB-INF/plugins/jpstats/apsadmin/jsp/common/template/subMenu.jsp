<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:contentNegotiation mimeType="application/xhtml+xml" charset="utf-8"/>
<wp:ifauthorized permission="superuser">
<li><a href="<s:url action="entryPoint" namespace="/do/jpstats/Statistics" />" ><s:text name="jpstats.statistics"/></a></li>
</wp:ifauthorized>