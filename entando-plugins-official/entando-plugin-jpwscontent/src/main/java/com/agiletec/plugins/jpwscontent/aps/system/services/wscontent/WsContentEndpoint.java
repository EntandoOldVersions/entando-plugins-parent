/*
*
* Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
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
* Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
*/
package com.agiletec.plugins.jpwscontent.aps.system.services.wscontent;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.rpc.ServiceException;

import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.plugins.jpwscontent.aps.system.services.JpwscontentSystemConstants;
/**
 * Endpoint of the webService 
 */
public class WsContentEndpoint extends ServletEndpointSupport implements IWsContentManager {

	@Override
	public int addContent(WsContentEnvelope envelope) throws Throwable {
		int status = IWsContentManager.RECIVING_NONE;
		Logger log = ApsSystemUtils.getLogger();
		if (log.isLoggable(Level.FINE)) {
			log.fine("addContent invoked");
		}
		try {
			status = this.getWsContentManager().addContent(envelope);
			
		} catch (Throwable t) {
			status = IWsContentManager.RECIVING_FAILURE;
			ApsSystemUtils.logThrowable(t, this, "addContent");
			throw new Throwable("error adding a content");
		}
		return status;
	}

	@Override
	public WsContentEnvelope getContent(String contentId) throws Throwable {
		WsContentEnvelope envelope = null;
		Logger log = ApsSystemUtils.getLogger();
		if (log.isLoggable(Level.FINE)) {
			log.fine("getContent invoked. contentId=" + contentId);
		}
		try {
			envelope = this.getWsContentManager().getContent(contentId);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContent");
			throw new Throwable("error getting content " + contentId);
		}		
		return envelope;
	}

	@Override
	protected void onInit() throws ServiceException {
		super.onInit();
		IWsContentManager wsContentManager = (IWsContentManager) this.getApplicationContext().getBean(JpwscontentSystemConstants.WSCONTENT_MANAGER);
		this.setWsContentManager(wsContentManager);
		ApsSystemUtils.getLogger().info(this.getClass().getName() + " inizializzato");
	}

	public void setWsContentManager(IWsContentManager wsContentManager) {
		this._wsContentManager = wsContentManager;
	}
	public IWsContentManager getWsContentManager() {
		return _wsContentManager;
	}

	private IWsContentManager _wsContentManager;
}
