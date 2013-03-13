/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software; 
* you can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.model.ContentStatusChangedEventInfo;

/**
 * @author E.Santoboni
 */
public interface IWorkflowNotifierDAO {
	
	public void saveContentEvent(ContentStatusChangedEventInfo contentEvent) throws ApsSystemException;
	
	public Map<String, List<ContentStatusChangedEventInfo>> getEventsToNotify() throws ApsSystemException;
	
	public void signNotifiedEvents(List<ContentStatusChangedEventInfo> notifiedEvents) throws ApsSystemException;
	
}