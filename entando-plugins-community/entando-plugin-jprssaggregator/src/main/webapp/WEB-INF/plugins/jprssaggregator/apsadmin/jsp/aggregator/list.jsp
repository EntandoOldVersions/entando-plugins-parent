<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<s:set var="targetNS" value="%{'/do/jprssaggregator/Aggregator'}" />
<h1><s:text name="jprssaggregator.title.rssAggregator" /><s:include value="/WEB-INF/apsadmin/jsp/common/inc/operations-context-general.jsp" /></h1>
<div id="main">

	<h2><s:text name="jprssaggregator.title.rssAggregator.rssManagement" /></h2>
	
	<%-- ACTION ERRORS --%>
	<s:if test="hasActionErrors()">
		<div class="message message_error">
			<h3><s:text name="message.title.ActionErrors" /></h3>
			<ul>
				<s:iterator value="actionErrors">
					<li><s:property/></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	
	<%-- FIELD ERRORS --%>
	<s:if test="hasFieldErrors()">
		<div class="message message_error">
			<h3><s:text name="message.title.FieldErrors" /></h3>	
			<ul>
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
					<li><s:property/></li>
				</s:iterator>
			</s:iterator>
			</ul>
		</div>
	</s:if>
	<%-- 
	<p>
		<a href="<s:url action="new"></s:url>"><s:text name="jprssaggregator.title.rssAggregator.add" /></a>
	</p>
	--%>
	
	<table class="generic" id="contentListTable">
		<caption><span><s:text name="jprssaggregator.title.rssAggregator.list" /></span></caption> 
		<tr>
			<%-- 
				<th><abbr title="Codice">Cod.</abbr></th>
			--%>
			<th><abbr title="<s:text name="jprssaggregator.rssAggregator.description" />"><s:text name="jprssaggregator.rssAggregator.descr" /></abbr></th>
			<th><s:text name="jprssaggregator.rssAggregator.url" /></th>
			<th><s:text name="jprssaggregator.rssAggregator.delay" /></th>
			<th><abbr title="<s:text name="jprssaggregator.rssAggregator.lastUpdate" />"><s:text name="jprssaggregator.rssAggregator.updated" /></abbr></th>
			<th><abbr title="<s:text name="jprssaggregator.rssAggregator.actions" />">&ndash;</abbr></th>
		</tr>
		<s:set var="aggregatorItems" value="aggregatorItems" />
		<s:iterator value="aggregatorItems" id="item">
		  <tr>
		    <td title="Code: <s:property value="#item.code" />"><a href="<s:url action="edit"><s:param name="code" value="#item.code"/></s:url>" title="<s:text name="label.edit" />:&#32;<s:property value="#item.descr" />"><s:property value="#item.descr" /></a></td>
		    <td><s:property value="#item.link" /></td> 
		    <td>
		    	<%//FIXME fare in modo che il valore delay del singolo canale sia compatibile con le chiavi della mappa "delays" %>
		    	<s:property value="%{delays.get(#item.delay.intValue())}"/>
		    </td>
		    <td class="centerText"><s:date name="#item.lastUpdate" format="dd/MM/yyyy HH:mm:ss" /></td> 
		    <td>
				<a href="<s:url action="edit"><s:param name="code" value="#item.code"/></s:url>" title="<s:text name="label.edit" />:&#32;<s:property value="#item.descr" />"><s:text name="label.edit" /></a>
				<a href="<s:url action="delete"><s:param name="code" value="#item.code"/></s:url>" title="<s:text name="label.remove" />:&#32;<s:property value="#item.descr" />"><s:text name="label.remove" /></a>
				<a href="<s:url action="syncronize"><s:param name="code" value="#item.code"/></s:url>" title="<s:text name="label.rssSync" />:&#32;<s:property value="#item.descr" />"><s:text name="label.rssSync" /></a>
			</td>
		  </tr>
		</s:iterator>
	</table>

</div>