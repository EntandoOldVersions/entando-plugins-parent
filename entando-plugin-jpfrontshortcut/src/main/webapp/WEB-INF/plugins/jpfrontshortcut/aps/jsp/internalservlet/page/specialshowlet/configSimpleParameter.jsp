<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% pageContext.setAttribute("random", (int) (Math.random() * 999999)); %>
<s:set var="random"><c:out value="${random}" /></s:set>
<div id="form-container" class="widget_form jpfrontshortcut-frameconfig-configSimpleParameter"> 
	<h2><s:text name="title.editFrame" />: <s:property value="frame" /> &ndash; <s:property value="currentPage.getModel().getFrames()[frame]"/></h2>
	<h3 class="margin-more-bottom margin-more-top"><s:text name="name.showlet" />:&#32;<s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" /></h3>
	<s:form action="saveConfigSimpleParameter" id="formform" theme="simple">
		<p class="noscreen">
			<wpsf:hidden name="pageCode" />
			<wpsf:hidden name="frame" />
			<wpsf:hidden name="showletTypeCode" value="%{showlet.type.code}" />
		</p>
		<fieldset>
			<legend>
				<s:text name="title.editFrame.settings" />
			</legend>
			<s:iterator value="showlet.type.typeParameters" var="showletParam" status="status" >
				<p>
					<label for="field<s:property value="#status.count+#random" />">
						<s:property value="#showletParam.name" />
					</label>
					<s:if test="%{#showletParam.descr.length()>0}">
						<em><s:property value="#showletParam.descr" /></em><br />
					</s:if>
					<wpsf:textfield useTabindexAutoIncrement="true" id="%{'field'+#status.count+#random}" name="%{#showletParam.name}" value="%{showlet.config[#showletParam.name]}" />
				</p>
			</s:iterator>
		</fieldset>
		<p>
			<sj:submit targets="form-container" value="%{getText('label.save')}" indicator="indicator" button="true" />
		</p>
	</s:form>
</div>