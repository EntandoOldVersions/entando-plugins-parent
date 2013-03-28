<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:form>
	<fieldset>
	<legend><span><s:text name="label.info" /></span></legend>
	<s:include value="/WEB-INF/plugins/jpuserprofile/apsadmin/jsp/common/inc/profile-formFields.jsp" />
	<p>
		<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" action="save" />
	</p>
	</fieldset>
</s:form>