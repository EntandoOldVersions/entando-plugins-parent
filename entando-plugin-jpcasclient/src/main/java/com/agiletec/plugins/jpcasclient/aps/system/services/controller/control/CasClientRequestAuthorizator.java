/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
* Entando is a free software; 
* You can redistribute it and/or modify it
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
package com.agiletec.plugins.jpcasclient.aps.system.services.controller.control;

import java.util.HashMap;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.controller.control.RequestAuthorizator;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.url.PageURL;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpcasclient.aps.system.services.auth.CasClientUtils;
import com.agiletec.plugins.jpcasclient.aps.system.services.config.CasClientConfig;
import com.agiletec.plugins.jpcasclient.aps.system.services.config.ICasClientConfigManager;

import org.entando.entando.aps.system.services.page.IPage;
import org.entando.entando.aps.system.services.page.IPageManager;

/**
 * Extends RequestAuthorizator for redirection to CAS pages if
 * current user has not authorization for the required page.
 * It switch to default behavior if the CAS plugin is disbled
 * (system param jpcasclient_extended_isactive)
 *
 * @author zuanni - G.Cocco
 * */
public class CasClientRequestAuthorizator extends RequestAuthorizator {


	@Override
	public void afterPropertiesSet() throws Exception {
		CasClientConfig casClientConfig = this.getCasConfigManager().getClientConfig();
		this.setCasClientConfig(casClientConfig);
		super.afterPropertiesSet();
	}

	@Override
	public int service(RequestContext reqCtx, int status) {
		if (_log.isLoggable(Level.FINEST)) {
			_log.finest("Invoked " + this.getClass().getName());
		}
		int retStatus = ControllerManager.INVALID_STATUS;
		if (status == ControllerManager.ERROR) {
			return status;
		}
		try {
			boolean isActive = this.getCasClientConfig().isActive();

			if (!isActive) {
				// if cas client is disactivate normal Authorization on request
				return super.service(reqCtx, retStatus);
			} else {
				HttpServletRequest req = reqCtx.getRequest();
				HttpSession session = req.getSession();
				IPage currentPage =
					(IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
				UserDetails currentUser =
					(UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
				boolean authorized = this.getAuthManager().isAuth(currentUser, currentPage);
				if (authorized) {
					retStatus = ControllerManager.CONTINUE;
				} else if (SystemConstants.GUEST_USER_NAME.equals(currentUser.getUsername())) {
					_log.info("CAS - user not authorized and guest");
					CasClientUtils casClientUtils = new CasClientUtils();
					String loginBaseUrl = this.getCasClientConfig().getCasLoginURL();
					StringBuffer loginUrl = new StringBuffer(loginBaseUrl);
					loginUrl.append("?service=");
					PageURL pageUrl = this.getUrlManager().createURL(reqCtx);
					String serviceUrl = casClientUtils.getURLStringWithoutTicketParam(pageUrl, reqCtx);
					loginUrl.append(serviceUrl);
					_log.info("CAS - Redirecting to " + loginUrl.toString());
					reqCtx.addExtraParam(RequestContext.EXTRAPAR_REDIRECT_URL, loginUrl.toString());
					retStatus = ControllerManager.REDIRECT;
				} else {
					_log.info("CAS - user authenticated but not authorized");
					Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
	            	String notAuthPageCode = this.getCasClientConfig().getNotAuthPage();
	            	IPage page = this.getPageManager().getPage(notAuthPageCode);
	            	String url = this.getUrlManager().createUrl(page, currentLang, new HashMap<String, String>());
	            	_log.info("CAS - Redirecting to " + url);
	            	reqCtx.addExtraParam(RequestContext.EXTRAPAR_REDIRECT_URL, url);
	            	retStatus = ControllerManager.REDIRECT;
	            }
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "service", "Error in processing the request");
			retStatus = ControllerManager.ERROR;
		}
		return retStatus;
	}

	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}
	public IPageManager getPageManager() {
		return _pageManager;
	}

	public ICasClientConfigManager getCasConfigManager() {
		return _casConfigManager;
	}
	public void setCasConfigManager(ICasClientConfigManager casClientConfigManager) {
		this._casConfigManager = casClientConfigManager;
	}

	public CasClientConfig getCasClientConfig() {
		return _casClientConfig;
	}
	public void setCasClientConfig(CasClientConfig casClientConfig) {
		this._casClientConfig = casClientConfig;
	}

	private IPageManager _pageManager;
	private ICasClientConfigManager _casConfigManager;
	private CasClientConfig _casClientConfig;

}
