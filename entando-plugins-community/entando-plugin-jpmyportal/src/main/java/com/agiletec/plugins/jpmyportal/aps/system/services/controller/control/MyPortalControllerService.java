/*
 *
 * Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
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
 * Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
 *
 */
package com.agiletec.plugins.jpmyportal.aps.system.services.controller.control;

import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.controller.control.AbstractControlService;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpmyportal.aps.system.JpmyportalSystemConstants;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.IPageModelUserConfigManager;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.model.PageModelUserConfigBean;

public class MyPortalControllerService extends AbstractControlService {
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this._log.config(this.getClass().getName() + ": inizializzato");
	}    

	/**
	 * Questo controller si incarica di caricare in sessione, quando possibile, la configurazione personalizzata dell'utente correntemente loggato. 
	 * NOTA: la sessione viene esplicitamente ripulita da qualsiasi configurazione personalizzata non allineata all'utente corrente
	 */
	@Override
	public int service(RequestContext reqCtx, int status) {
		if (_log.isLoggable(Level.FINEST)) {
			_log.finest("Invocata " + this.getClass().getName());
		}
		int retStatus = ControllerManager.INVALID_STATUS;
		if (status == ControllerManager.ERROR) {
			return status;
		}
		try {
			HttpServletRequest req = reqCtx.getRequest();
			HttpSession session = req.getSession();
			UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			if (null == currentUser) {
				throw new ApsSystemException("no user on session");
			}
			PageModelUserConfigBean userConfigBean = (PageModelUserConfigBean) session.getAttribute(JpmyportalSystemConstants.SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG);
			if (!currentUser.getUsername().equals(SystemConstants.GUEST_USER_NAME) && 
					!currentUser.getUsername().equals(SystemConstants.ADMIN_USER_NAME)) {
				if ((null == userConfigBean || !currentUser.getUsername().equals(userConfigBean.getUsername()))) {
					userConfigBean = this.getPageModelUserConfigManager().getUserConfig(currentUser.getUsername());
					if (null != userConfigBean) {
						session.setAttribute(JpmyportalSystemConstants.SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG, userConfigBean);
					} else {
						session.removeAttribute(JpmyportalSystemConstants.SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG);
					}
				}
			} else {
				session.removeAttribute(JpmyportalSystemConstants.SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG);
			}
			retStatus = ControllerManager.CONTINUE;
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "service", "Error while processing the request");
			retStatus = ControllerManager.SYS_ERROR;
			reqCtx.setHTTPError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}        
		return retStatus;
	}

	public void setPageModelUserConfigManager(IPageModelUserConfigManager pageModelUserConfigManager) {
		this._pageModelUserConfigManager = pageModelUserConfigManager;
	}
	public IPageModelUserConfigManager getPageModelUserConfigManager() {
		return _pageModelUserConfigManager;
	}

	private IPageModelUserConfigManager _pageModelUserConfigManager;
}
