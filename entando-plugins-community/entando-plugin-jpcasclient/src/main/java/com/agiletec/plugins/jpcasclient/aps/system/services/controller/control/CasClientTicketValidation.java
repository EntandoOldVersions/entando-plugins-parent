/*
*
* Copyright 2009 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
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
* Copyright 2009 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
*/
package com.agiletec.plugins.jpcasclient.aps.system.services.controller.control;

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.client.validation.Assertion;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.services.baseconfig.BaseConfigManager;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.controller.control.AbstractControlService;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.system.services.url.PageURL;
import com.agiletec.plugins.jpcasclient.CasClientPluginSystemCostants;
import com.agiletec.plugins.jpcasclient.aps.system.services.auth.CasClientUtils;

/**
 * Control Service for CAS ticket validation
 * 
 * @author zuanni G.Cocco
 * */
public class CasClientTicketValidation extends AbstractControlService {

	@Override
	public void afterPropertiesSet() throws Exception {
		String urlCasValidate = this.getConfigManager().getParam(CasClientPluginSystemCostants.JPCASCLIENT_VALIDATE_URL);
		_ticketValidationUtil = new CasClientTicketValidationUtil(urlCasValidate);
		this._log.config(this.getClass().getName() + ": initialization");
	}
	
	/**
	 * the errors of control sub-service must be ignored, 
	 * the required page will be erogated anyway
	 * */
	@Override
	public int service(RequestContext reqCtx, int status) {
		HttpServletRequest request = reqCtx.getRequest();
		int retStatus = ControllerManager.CONTINUE;
		if (status == ControllerManager.ERROR) {
			return status;
		}
		String isActive = 
			this.getConfigManager().getParam(CasClientPluginSystemCostants.JPCASCLIENT_EXTENDED_ISACTIVE);
		if (!isActive.equals("true")) {
			return retStatus;			
		}
		try {
			String ticket = request.getParameter("ticket");
			if (null != ticket && ticket.length() > 0 ) {
				CasClientUtils casClientUtils = new CasClientUtils();
				PageURL pageUrl = this.getUrlManager().createURL(reqCtx);
				String serviceUrl = casClientUtils.getURLStringWithoutTicketParam(pageUrl, reqCtx);
				_log.info(" service " + serviceUrl + ", ticket " + ticket);
				Assertion assertion;
				assertion = _ticketValidationUtil.validateTicket(serviceUrl, ticket);
				request.getSession().setAttribute(CasClientPluginSystemCostants.JPCASCLIENT_CONST_CAS_ASSERTION, assertion);
				
//				redirect to current page without ticket parameter in the url
// 				to avoid involuntary submits
				reqCtx.addExtraParam(RequestContext.EXTRAPAR_REDIRECT_URL, serviceUrl.toString());
				retStatus = ControllerManager.REDIRECT;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "service", "Error in processing the request");
		}
		return retStatus;
	}
	
	
	public IURLManager getUrlManager() {
		return _urlManager;
	}
	public void setUrlManager(IURLManager urlManager) {
		this._urlManager = urlManager;
	}
	
	public void setConfigManager(BaseConfigManager _configManager) {
		this._configManager = _configManager;
	}
	public BaseConfigManager getConfigManager() {
		return _configManager;
	}

	private CasClientTicketValidationUtil _ticketValidationUtil;
	private IURLManager _urlManager;
	private BaseConfigManager _configManager;
	
}