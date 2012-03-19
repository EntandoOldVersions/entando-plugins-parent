/*
*
* Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpmyportal.aps.internalservlet.myportal.ajax;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Showlet;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpmyportal.aps.internalservlet.myportal.MyPortalAction;
import com.agiletec.plugins.jpmyportal.aps.system.JpmyportalSystemConstants;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.model.PageModelUserConfigBean;

public class MyPortalAjaxAction extends MyPortalAction {
	
	@Override
	public String swapFrames() {
		HttpServletRequest req = this.getRequest();			
		PageModelUserConfigBean userConfiguration = (PageModelUserConfigBean) req.getSession().getAttribute(JpmyportalSystemConstants.SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG);
		IPage page =  this.getPageManager().getPage(this.getCurrentPage());
		String currentPageModelCode = page.getModel().getCode();
		Showlet[] defaultShowlets = page.getShowlets();
		try {
			int firstFrame = this.getFrameSource();
			int frameToSwapWith = this.getFrameDest();
			Showlet[] meshUp = null;
			Showlet[] customShowlets = null;
			UserDetails currentUser = (UserDetails) req.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			if (null != userConfiguration) {
				meshUp = new Showlet[defaultShowlets.length];
				customShowlets = userConfiguration.getConfig().get(currentPageModelCode);
				for (int scan = 0; scan < defaultShowlets.length; scan++) {
					if (null == customShowlets[scan]) {
						meshUp[scan]=defaultShowlets[scan];
					} else {
						meshUp[scan]=customShowlets[scan];
					}
				}
			} else {
				meshUp = defaultShowlets;				
			}			
			userConfiguration = doFrameSwap(userConfiguration, currentUser.getUsername(), currentPageModelCode, firstFrame, frameToSwapWith, meshUp);
			req.getSession().setAttribute(JpmyportalSystemConstants.SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG, userConfiguration);
		} catch (Throwable t) {
			String message = "Errore nell'azione \"swapFrames\"";
			ApsSystemUtils.logThrowable(t, this, "swapFrames", message);
			this.setRetval("false");
			return SUCCESS;
		}
		this.setRetval("true");
		return SUCCESS; 
	}

	public String emptyCustomizableShowlet() {
		HttpServletRequest req = this.getRequest();		
		PageModelUserConfigBean userConfiguration = (PageModelUserConfigBean) req.getSession().getAttribute(JpmyportalSystemConstants.SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG);		
		UserDetails currentUser = (UserDetails) req.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		try {
			String currentPage = this.getCurrentPage();
			int currentFrame = this.getFrameSource();
			IPage page =  this.getPageManager().getPage(currentPage);
			String currentPageModel = page.getModel().getCode();
			userConfiguration = this.getPageModelUserConfigManager().voidFrame(userConfiguration, currentUser.getUsername(), currentPageModel, currentFrame);
			req.getSession().setAttribute(JpmyportalSystemConstants.SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG, userConfiguration);

		} catch (Throwable t) {
			String message = "Errore nell'azione \"emptyCustomizableShowlet\"";
			ApsSystemUtils.logThrowable(t, this, "emptyCustomizableShowlet", message);
			this.setRetval("false");
			return SUCCESS;
		}
		this.setRetval("true");
		return SUCCESS;
	}
	
	private PageModelUserConfigBean doFrameSwap(PageModelUserConfigBean customization, String username, String pageModelCode, int firstFrame, int secondFrame, Showlet[] showlets) {
		try {
			return this.getPageModelUserConfigManager().swapShowlets(customization, username, pageModelCode, firstFrame, secondFrame, showlets);			
		} catch (Throwable t) {
			String message = "Errore scambiando i frames in \"doFrameSwap\"";
			ApsSystemUtils.logThrowable(t, this, "doFrameSwap", message);
			throw new RuntimeException(message, t);
		} 
	}
	
	public void setRetval(String retval) {
		this._retval = retval;
	}
	public String getRetval() {
		return _retval;
	}
	
	private String _retval;
	
}