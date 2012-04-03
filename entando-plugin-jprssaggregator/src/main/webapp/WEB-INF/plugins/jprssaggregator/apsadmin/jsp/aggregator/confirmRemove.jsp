<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<wp:contentNegotiation mimeType="application/xhtml+xml" charset="utf-8" />
<s:set var="targetNS" value="%{'/do/jprssaggregator/Aggregator'}" />
<h1><s:text name="jprssaggregator.title.rssAggregator" /><s:include value="/WEB-INF/apsadmin/jsp/common/inc/operations-context-general.jsp" /></h1>
<div id="main">
	<h2><s:text name="jprssaggregator.title.rssAggregator.add" /></h2>
	<s:form>
		<p class="noscreen">
			<wpsf:hidden name="code" />
		</p>
		
		<p>
			<s:text name="message.are.you.sure.to.delete" /> <em><s:property value="code"/></em>&#32;?
			<%//TODO insert description of the current item here. %>
			&#32;
			<wpsf:submit useTabindexAutoIncrement="true" cssClass="button" action="doDelete" value="%{getText('label.confirm')}" />
		</p>
	</s:form>

</div>



