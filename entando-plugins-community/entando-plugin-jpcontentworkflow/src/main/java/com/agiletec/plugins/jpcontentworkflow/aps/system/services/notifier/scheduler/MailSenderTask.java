/*
*
* Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
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
package com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.scheduler;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.IWorkflowNotifierManager;

/**
 * @author E.Santoboni
 */
public class MailSenderTask extends Task {
	
	public MailSenderTask(IWorkflowNotifierManager notifierManager) {
		this._notifierManager = notifierManager;
	}
	
	@Override
	public void execute() {
		try {
			this._notifierManager.sendMails();
		} catch (ApsSystemException e) {
			ApsSystemUtils.logThrowable(e, this, "execute");
		}
	}
	
	private IWorkflowNotifierManager _notifierManager;
	
}