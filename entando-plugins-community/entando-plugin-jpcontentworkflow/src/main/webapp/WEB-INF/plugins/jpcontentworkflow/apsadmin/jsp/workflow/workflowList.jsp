<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<h1><s:text name="title.workflowManagement" /></h1>
<div id="main">
	<table class="generic">
		<caption><span><s:text name="content.types.list" /></span></caption>
		<tr>
			<th>
				<s:text name="label.contentType" />
			</th> 
			<th>
				<s:text name="label.editRole" />
			</th>
			<th>
				<s:text name="label.editSteps" />
			</th>
		</tr>
		<s:iterator var="contentType" value="contentTypes">
			<tr> 
				<td><s:property value="#contentType.descr"/></td>
				<td class="centerText icon">
					<a 	href="<s:url action="editRole" ><s:param name="typeCode" value="%{#contentType.code}" /></s:url>" 
						title="<s:text name="label.editRole" />:&#32;<s:text name="label.contentType" />" >
							<img src="<wp:resourceURL />administration/common/img/icons/users.png" alt="<s:text name="label.editMainRole" />"  />
					</a>     
				</td>
				<td class="centerText icon">
					<a 	href="<s:url action="editSteps" ><s:param name="typeCode" value="%{#contentType.code}" /></s:url>" 
						title="<s:text name="label.editSteps" />:&#32;<s:text name="label.contentType" />" >
						<img src="<wp:resourceURL />administration/common/img/icons/22x22/content-inspect-work.png" alt="<s:text name="label.editSteps" />" />
					</a>
				</td>
			</tr>	
		</s:iterator>
	</table>
</div>