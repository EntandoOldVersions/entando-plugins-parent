/*
*
* Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of jAPS software.
* jAPS is a free software; 
* you can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.model.NotifierConfig;

/**
 * @author E.Santoboni
 */
public interface IWorkflowNotifierManager {
	
	public NotifierConfig getNotifierConfig() throws ApsSystemException;
	
	public void saveNotifierConfig(NotifierConfig notifierConfig) throws ApsSystemException;
	
	public void sendMails() throws ApsSystemException;
	
}