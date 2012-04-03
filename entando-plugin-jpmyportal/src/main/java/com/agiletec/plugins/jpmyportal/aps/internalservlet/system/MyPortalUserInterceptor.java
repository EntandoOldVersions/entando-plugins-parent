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
package com.agiletec.plugins.jpmyportal.aps.internalservlet.system;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.InterceptorMadMax;
import com.opensymphony.xwork2.ActionInvocation;

public class MyPortalUserInterceptor extends InterceptorMadMax {
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpSession session = ServletActionContext.getRequest().getSession();
		UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		if (currentUser==null || SystemConstants.GUEST_USER_NAME.equals(currentUser.getUsername()) 
				|| SystemConstants.ADMIN_USER_NAME.equals(currentUser.getUsername())) {
			return this.getErrorResultName();
		}
		return super.intercept(invocation);
	}
	
}