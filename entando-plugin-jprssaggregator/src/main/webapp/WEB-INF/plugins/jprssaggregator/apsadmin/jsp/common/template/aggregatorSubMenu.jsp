<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:contentNegotiation mimeType="application/xhtml+xml" charset="utf-8"/>
<wp:ifauthorized permission="superuser">
<li><a href="<s:url action="intro" namespace="/do/jprssaggregator/Aggregator" />" id="menu_rssAggregator"><s:text name="jprssaggregator.name" /></a></li>
</wp:ifauthorized>