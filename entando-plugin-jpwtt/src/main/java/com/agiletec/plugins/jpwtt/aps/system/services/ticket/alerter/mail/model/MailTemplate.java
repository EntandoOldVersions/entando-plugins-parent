/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando Enterprise Edition software.
* You can redistribute it and/or modify it
* under the terms of the Entando's EULA
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwtt.aps.system.services.ticket.alerter.mail.model;

import java.util.HashMap;
import java.util.Map;

public class MailTemplate {
	
	public String getOperation() {
		return _operation;
	}
	public void setOperation(String operation) {
		this._operation = operation;
	}
	
	public Map<String, String> getTemplateBodies() {
		return _templateBodies;
	}
	public void addTemplateBody(String type, String body) {
		_templateBodies.put(type, body);
	}
	
	private String _operation;
	
	private Map<String, String> _templateBodies = new HashMap<String, String>();
	
	public static final String TEMPLATE_TYPE_ADMIN = "admin";
	public static final String TEMPLATE_TYPE_USER = "user";
	public static final String TEMPLATE_TYPE_OPERATOR = "operator";
	public static final String TEMPLATE_TYPE_ALLOPERATORS = "allOperators";
	
}