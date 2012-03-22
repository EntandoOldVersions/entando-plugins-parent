<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1><s:text name="title.contentfeedbackManagement" /></h1>

<div id="main">
    <h2><s:text name="title.contentfeedbackSettings" /></h2>

    <s:form action="update">
        <s:if test="hasActionErrors()">
            <div class="message message_error">
                <h3><s:text name="message.title.ActionErrors" /></h3>
                <ul>
                    <s:iterator value="actionErrors">
                        <li><s:property escape="false" /></li>
                    </s:iterator>
                </ul>
            </div>
        </s:if>

        <s:if test="hasFieldErrors()">
            <div class="message message_error">
                <h3><s:text name="message.title.FieldErrors" /></h3>
                <ul>
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li><s:property escape="false" /></li>
                        </s:iterator>
                    </s:iterator>
                </ul>
            </div>
        </s:if>

        <s:if test="hasActionMessages()">
            <div class="message message_confirm">
                <h3><s:text name="messages.confirm" /></h3>
                <ul>
                    <s:iterator value="actionMessages">
                        <li><s:property/></li>
                    </s:iterator>
                </ul>
            </div>
        </s:if>

        <fieldset class="margin-more-top">
            <legend><s:text name="label.config" /></legend>
            <p>
                <label for="jpcontentfeedback_rateContent" class="basic-mint-label"><s:text name="jpcontentfeedback.label.rateContent" />:</label>
                <input type="checkbox" class="radiocheck" id="jpcontentfeedback_rateContent" 
                       name="config.rateContent" value="true" <s:if test="config.rateContent">checked="checked"</s:if> />
                <label for="jpcontentfeedback_rateContent"><s:text name="label.active" /></label>
            </p>

            <p>
                <label for="jpcontentfeedback_comment" class="basic-mint-label"><s:text name="jpcontentfeedback.label.comment" />:</label>
                <input type="checkbox" class="radiocheck" id="jpcontentfeedback_comment" 
                       name="config.comment" value="true" <s:if test="config.comment">checked="checked"</s:if> />
                <label for="jpcontentfeedback_comment"><s:text name="label.active" /></label>
            </p>

            <p>
                <label for="jpcontentfeedback_rateComment" class="basic-mint-label"><s:text name="jpcontentfeedback.label.rateComment" />:</label>
                <input type="checkbox" class="radiocheck" id="jpcontentfeedback_rateComment" 
                       name="config.rateComment" value="true" <s:if test="config.rateComment">checked="checked"</s:if> />
                <label for="jpcontentfeedback_rateComment"><s:text name="label.active" /></label>
            </p>

            <p>
                <label for="jpcontentfeedback_anonymousComment" class="basic-mint-label"><s:text name="jpcontentfeedback.label.anonymousComment" />:</label>
                <input type="checkbox" class="radiocheck" id="jpcontentfeedback_anonymousComment" 
                       name="config.anonymousComment" value="true" <s:if test="config.anonymousComment">checked="checked"</s:if> />
                <label for="jpcontentfeedback_anonymousComment"><s:text name="label.active" /></label>
            </p>
        </fieldset>

        <p class="centerText">
            <wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" />
        </p>
    </s:form>
</div>