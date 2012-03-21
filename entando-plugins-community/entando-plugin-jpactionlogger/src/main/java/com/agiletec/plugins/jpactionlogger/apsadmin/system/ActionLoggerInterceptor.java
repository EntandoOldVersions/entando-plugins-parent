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
package com.agiletec.plugins.jpactionlogger.apsadmin.system;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpactionlogger.aps.system.JpactionloggerSystemConstants;
import com.agiletec.plugins.jpactionlogger.aps.system.services.actionlogger.IActionLoggerManager;
import com.agiletec.plugins.jpactionlogger.aps.system.services.actionlogger.model.ActionRecord;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class ActionLoggerInterceptor extends AbstractInterceptor {
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		try {
			ActionRecord actionRecord = this.buildActionRecord(invocation);
			this.getActionLoggerManager().addActionRecord(actionRecord);			
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "intercept");
		}
		return invocation.invoke();
	}
	
	/**
	 * Build an {@link ActionRecord} object related to the current action
	 * @param invocation
	 * @return an {@link ActionRecord} for the current action
	 */
	private ActionRecord buildActionRecord(ActionInvocation invocation) {
		ActionRecord record = new ActionRecord();
		String username = this.getCurrentUsername();
		String namespace = invocation.getProxy().getNamespace();
		String actionName = invocation.getProxy().getActionName();
		String params = this.getParametersString();
		record.setUsername(username);
		record.setNamespace(namespace);
		record.setActionName(actionName);
		record.setParams(params);
		return record;
	}
	
	/**
	 * Gets the username of the user in session
	 * @return the username of the current user
	 */
	private String getCurrentUsername() {
		String username = null;
		UserDetails currentUser = this.getCurrentUser();
		if (null == currentUser) {
			username = "ANONYMOUS";
		} else {
			username = currentUser.getUsername();
		}
		return username;
	}
	
	/**
	 * Resrituisce una stringa contentente tutti i paramentri in request.
	 * @return
	 */
	private String getParametersString() {
		String[] paramsToExclude = this.getExcludeParams().split(",");
		StringBuffer params = new StringBuffer();
		this.getRequest().getParameterMap();
		Map<String, String[]> reqParams = this.getRequest().getParameterMap();
		if (null != reqParams && !reqParams.isEmpty()) {
			for (Entry<String, String[]> pair : reqParams.entrySet()) {
				String key = pair.getKey();
				if (!this.isParamToExclude(key, paramsToExclude)) {
					params.append(key).append("=");
					String[] values = pair.getValue();
					if (null != values) {
						for (int i = 0; i < values.length; i++) {
							params.append(values[i]).append(",");
						}
					}
					params.deleteCharAt(params.length() - 1);
					params.append("\n");
				}
			}
		}
		return params.toString();
	}
	
	private boolean isParamToExclude(String key, String[] paramsToExclude) {
		for (int i = 0; i < paramsToExclude.length; i++) {
			if (key.equals(paramsToExclude[i])) {
				return true;
			}
		}
		return false;
	}

	private UserDetails getCurrentUser() {
		HttpSession session = this.getRequest().getSession();
		UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		return currentUser;
	}
	
	private HttpServletRequest getRequest() {
		HttpServletRequest request = ServletActionContext.getRequest();
		return request;
	}
	
	private IActionLoggerManager getActionLoggerManager() {
		return (IActionLoggerManager) ApsWebApplicationUtils.getBean(JpactionloggerSystemConstants.ACTION_LOGGER_MANAGER, this.getRequest());
	}
	
	public String getExcludeParams() {
		return _excludeParams;
	}
	public void setExcludeParams(String excludeParams) {
		this._excludeParams = excludeParams;
	}
	
	/**
	 * a list of comma separated params to exclude from logging
	 */
	private String _excludeParams = "";
	
}