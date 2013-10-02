<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<h1 class="panel panel-default title-page"><span class="panel-body display-block"><s:text name="title.eMailManagement" />&#32;/&#32;<s:text name="title.eMailManagement.sendersConfig" /></span></h1>
<div id="main">	
	<p>
            <a class="btn btn-default margin-base-bottom" href="<s:url action="newSender" />" ><span class="icon icon-plus-sign"> <s:text name="label.senders.new" /></span></a>
	</p>
	
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
	
	<s:if test="%{senderCodes.size()==0}">
		<p><s:text name="label.senders.none" /></p>
	</s:if>
	<s:else>
		<table class="table table-bordered" />
			<tr>
                                <th class="text-center text-nowap col-xs-6 col-sm-3 col-md-3 col-lg-3 "><abbr title="<s:text name="label.remove" />">&ndash;</abbr></th>
				<th><s:text name="code" /></th>
				<th><s:text name="mail" /></th>
			</tr>
			<s:iterator value="%{config.senders.entrySet()}" id="sender">
				<tr>
                                    	<td class="text-center text-nowrap">
                                            <a class="btn btn-warning btn-xs" href="<s:url action="trashSender" ><s:param name="code" value="#sender.key" /></s:url>" title="<s:text name="label.remove" />: <s:property value="#sender.value" />">
                                                    <span class="sr-only"></span>
                                                    <span class="icon icon-remove-circle"></span>
						</a>
					</td>
					<td><code><s:property value="#sender.key"/></code></td>
					<td>
						<a href="<s:url action="editSender" ><s:param name="code" value="#sender.key" /></s:url>" title="<s:text name="label.edit" />: <s:property value="#sender.value" />">
							<s:property value="#sender.value" />
						</a>
					</td>
				</tr>
			</s:iterator>
		</table>
	</s:else>
</div>