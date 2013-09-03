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
package com.agiletec.plugins.jpshowletreplicator.aps.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.entando.entando.aps.system.services.widgettype.WidgetType;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.util.ApsWebApplicationUtils;

import org.entando.entando.aps.system.services.page.IPage;
import org.entando.entando.aps.system.services.page.IPageManager;
import org.entando.entando.aps.system.services.page.Widget;

/**
 * @author E.Santoboni
 */
public class ShowletReplicatorTag extends TagSupport {

	@Override
	public int doEndTag() throws JspException {
		ServletRequest req =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) req.getAttribute(RequestContext.REQCTX);
		try {
			org.entando.entando.aps.system.services.page.Widget currentShowlet = (org.entando.entando.aps.system.services.page.Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_SHOWLET);
			String pageCode = currentShowlet.getConfig().getProperty("pageCodeParam");
			IPageManager pageManager = (IPageManager) ApsWebApplicationUtils.getBean(SystemConstants.PAGE_MANAGER, this.pageContext);
			IPage targetPage = pageManager.getPage(pageCode);
			if (null != targetPage) {
				String frameIdString = currentShowlet.getConfig().getProperty("frameIdParam");
				int frameId = Integer.parseInt(frameIdString);
				org.entando.entando.aps.system.services.page.Widget[] showlets = targetPage.getShowlets();
				if (showlets.length>=frameId) {
					org.entando.entando.aps.system.services.page.Widget targetShowlet = targetPage.getShowlets()[frameId];
					if (null != targetShowlet) {
						reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_SHOWLET, targetShowlet);
						WidgetType WidgetType = targetShowlet.getType();
						if (WidgetType.isLogic()) {
							WidgetType = WidgetType.getParentType();
						}
						String pluginCode = WidgetType.getPluginCode();
						boolean isPluginShowlet = (null != pluginCode && pluginCode.trim().length()>0);
						StringBuffer jspPath = new StringBuffer("/WEB-INF/");
						if (isPluginShowlet) {
							jspPath.append("plugins/").append(pluginCode.trim()).append("/");
						}
						jspPath.append("aps/jsp/showlets/").append(WidgetType.getCode()).append(".jsp");

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
