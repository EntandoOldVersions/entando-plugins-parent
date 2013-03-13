/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpshowletreplicator.aps.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Showlet;
import com.agiletec.aps.system.services.showlettype.ShowletType;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * @author E.Santoboni
 */
public class ShowletReplicatorTag extends TagSupport {
	
	@Override
	public int doEndTag() throws JspException {
		ServletRequest req =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) req.getAttribute(RequestContext.REQCTX);
		try {
			Showlet currentShowlet = (Showlet) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_SHOWLET);
			String pageCode = currentShowlet.getConfig().getProperty("pageCodeParam");
			IPageManager pageManager = (IPageManager) ApsWebApplicationUtils.getBean(SystemConstants.PAGE_MANAGER, this.pageContext);
			IPage targetPage = pageManager.getPage(pageCode);
			if (null != targetPage) {
				String frameIdString = currentShowlet.getConfig().getProperty("frameIdParam");
				int frameId = Integer.parseInt(frameIdString);
				Showlet[] showlets = targetPage.getShowlets();
				if (showlets.length>=frameId) {
					Showlet targetShowlet = targetPage.getShowlets()[frameId];
					if (null != targetShowlet) {
						reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_SHOWLET, targetShowlet);
						ShowletType showletType = targetShowlet.getType();
						if (showletType.isLogic()) {
							showletType = showletType.getParentType();
						}
						String pluginCode = showletType.getPluginCode();
						boolean isPluginShowlet = (null != pluginCode && pluginCode.trim().length()>0);
						StringBuffer jspPath = new StringBuffer("/WEB-INF/");
						if (isPluginShowlet) {
							jspPath.append("plugins/").append(pluginCode.trim()).append("/");
						}
						jspPath.append("aps/jsp/showlets/").append(showletType.getCode()).append(".jsp");
						
						this.pageContext.include(jspPath.toString());
					}
				}
			}
		} catch (Throwable t) {
			String msg = "Errore in preelaborazione showlets";
			ApsSystemUtils.logThrowable(t, this, "doEndTag", msg);
			throw new JspException(msg, t);
		}
		return EVAL_PAGE;
	}
	
}