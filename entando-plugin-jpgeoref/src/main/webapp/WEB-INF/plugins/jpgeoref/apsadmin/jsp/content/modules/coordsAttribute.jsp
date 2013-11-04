<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<s:if test="#lang.default">
    <div class="margin-small-bottom">
        <div class="input-group">
            <span class="input-group-addon">X</span>
            <wpsf:textfield useTabindexAutoIncrement="true" id="x_%{#attributeTracer.getFormFieldName(#attribute)}"
                            name="x_%{#attributeTracer.getFormFieldName(#attribute)}" value="%{#attribute.x}"
                            maxlength="254" cssClass="text" cssClass="form-control" />
        </div>
    </div>
    <div class="margin-small-bottom">
        <div class="input-group">
            <span class="input-group-addon">Y</span>
            <wpsf:textfield useTabindexAutoIncrement="true" id="y_%{#attributeTracer.getFormFieldName(#attribute)}"
                            name="y_%{#attributeTracer.getFormFieldName(#attribute)}" value="%{#attribute.y}"
                            maxlength="254" cssClass="text" cssClass="form-control" />
        </div>
    </div>
    <div class="margin-small-bottom">
        <div class="input-group">
            <span class="input-group-addon">Z</span>
            <wpsf:textfield useTabindexAutoIncrement="true" id="z_%{#attributeTracer.getFormFieldName(#attribute)}"
                            name="z_%{#attributeTracer.getFormFieldName(#attribute)}" value="%{#attribute.z}"
                            maxlength="254" cssClass="text" cssClass="form-control" />
        </div>
    </div>
    <div id="mapcontainer_<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />" class="jpgeoref-mapcontainer"></div>
</s:if>
<s:else>
    <div class="alert alert-info"><s:text name="note.editContent.doThisInTheDefaultLanguage.must" /></div>
</s:else>
