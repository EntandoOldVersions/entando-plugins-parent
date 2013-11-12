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
package com.agiletec.plugins.jpwebdynamicform.apsadmin.message.config;

import java.util.List;

import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.SmallMessageType;

public interface INotifierConfigAction {
	
	public List<SmallMessageType> getMessageTypes();
	
	public String edit();
	
	public String addAddress();
	
	public String removeAddress();
	
	public String save();
	
	public static final int RECIPIENT_TO = 1;
	public static final int RECIPIENT_CC = 2;
	public static final int RECIPIENT_BCC = 3;
	
}