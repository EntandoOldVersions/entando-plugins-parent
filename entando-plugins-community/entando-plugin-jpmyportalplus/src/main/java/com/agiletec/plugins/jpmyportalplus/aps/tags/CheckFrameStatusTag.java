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
package com.agiletec.plugins.jpmyportalplus.aps.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.plugins.jpmyportalplus.aps.system.JpmyportalplusSystemConstants;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.IPageUserConfigManager;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.CustomPageConfig;

/**
 * @author E.Santoboni
 */
public class CheckFrameStatusTag extends TagSupport {
	
	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest req =  (HttpServletRequest) this.pageContext.getRequest();
		Integer[] customShowletStatus = null;
		try {
			CustomPageConfig customPageConfig = 
				(CustomPageConfig) req.getSession().getAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_PAGE_CONFIG);
			if (null != customPageConfig) {
				customShowletStatus = customPageConfig.getStatus();
			}
			RequestContext reqCtx = (RequestContext) req.getAttribute(RequestContext.REQCTX);
			Integer currentFrame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
			if (customShowletStatus != null) {
				int status = customShowletStatus[currentFrame] == null ? 0 : customShowletStatus[currentFrame].intValue();
				if (IPageUserConfigManager.STATUS_CLOSE == status) {
					return EVAL_BODY_INCLUDE;
				} else if (IPageUserConfigManager.STATUS_OPEN == status) {
					return SKIP_BODY;
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getCustomShowletStatus", "Error on doStartTag");
			throw new JspException("Error on doStartTag", t);
		}
		return SKIP_BODY;
	}
	
}
