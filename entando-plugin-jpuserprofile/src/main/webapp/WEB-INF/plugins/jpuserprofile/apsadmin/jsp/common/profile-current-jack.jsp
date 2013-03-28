<%@ taglib prefix="s" uri="/struts-tags" %>

<h2 class="margin-more-bottom"><s:text name="jpuserprofile.title.changeUserProfile" /></h2>
<fieldset>
<legend><span><s:text name="label.info" /></span></legend>
<s:action name="edit" namespace="/do/jpuserprofile/CurrentUser/Profile" executeResult="true"></s:action>
</fieldset>