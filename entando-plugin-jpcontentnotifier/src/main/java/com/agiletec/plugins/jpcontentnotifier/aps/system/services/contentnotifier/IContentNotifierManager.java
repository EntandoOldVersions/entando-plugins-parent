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
package com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.model.ContentMailInfo;
import com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.model.NotifierConfig;

public interface IContentNotifierManager {

	public NotifierConfig getConfig();

	public void updateNotifierConfig(NotifierConfig config) throws ApsSystemException;

	public void sendEMails() throws ApsSystemException;

	public List<ContentMailInfo> getContentsToNotify() throws ApsSystemException;

}
