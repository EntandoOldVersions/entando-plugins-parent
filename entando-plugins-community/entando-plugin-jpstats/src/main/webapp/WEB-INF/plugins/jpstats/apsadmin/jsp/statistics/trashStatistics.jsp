<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<h1><a href="<s:url action="entryPoint" />" title="<s:text name="note.goToSomewhere" />: <s:text name="jpstats.header.statistics" />"><s:text name="jpstats.header.statistics" /></a></h1>
<div id="main">
	<h2><s:text name="jpstats.title.stats.trash" /></h2>
	<s:form action="delete">
		<p>
			<s:hidden name="startDate" ></s:hidden>
			<s:hidden name="endDate" ></s:hidden>
			<s:text name="jpstas.note.delete.are.you.sure" />
			<em><s:date name="startDate" format="dd/MM/yyyy HH:mm" />&#32;&mdash;&#32;<s:date name="endDate" format="dd/MM/yyyy HH:mm" /></em>
			&#32;
			?
			&#32;
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.confirm')}" cssClass="button" />
		</p>
	</s:form>

</div>