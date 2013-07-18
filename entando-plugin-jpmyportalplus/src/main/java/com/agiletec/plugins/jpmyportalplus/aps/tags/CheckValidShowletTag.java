/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando Enterprise Edition software.
* You can redistribute it and/or modify it
* under the terms of the Entando's EULA
*
* See the file License for the specific language governing permissions
* and limitations under the License
*
*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpmyportalplus.aps.tags;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpmyportalplus.aps.system.JpmyportalplusSystemConstants;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.IMyPortalConfigManager;

/**
 * Toggle the visibility of the elements contained in body tag,
 * depending on the presents of showlet type into the allowed types of MyPortal Service.
 * Is possible to insert the result of the check in a variable placed in the page context.
 * @author E.Santoboni
 */
public class CheckValidShowletTag extends TagSupport {

	@Override
	public int doStartTag() throws JspException {
		IMyPortalConfigManager myPortalConfigManager = (IMyPortalConfigManager) ApsWebApplicationUtils.getBean(JpmyportalplusSystemConstants.MYPORTAL_CONFIG_MANAGER, pageContext);
		HttpServletRequest req =  (HttpServletRequest) this.pageContext.getRequest();
		try {
			boolean check = false;
			RequestContext reqCtx = (RequestContext) req.getAttribute(RequestContext.REQCTX);
			com.agiletec.aps.system.services.page.Widget currentShowlet = (com.agiletec.aps.system.services.page.Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_SHOWLET);
			if (null != currentShowlet && null != currentShowlet.getType()) {
				String typeCode = currentShowlet.getType().getCode();
				Set<String> showletTypeCodes = myPortalConfigManager.getConfig().getAllowedShowlets();
				check = showletTypeCodes.contains(typeCode);
			}
			if (null != this.getVar()) {
				this.pageContext.setAttribute(this.getVar(), new Boolean(check));
			}
			if (check) {
				return EVAL_BODY_INCLUDE;
			} else {
				return SKIP_BODY;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "doStartTag", "Error on doStartTag");
			throw new JspException("Error on doStartTag", t);
		}
	}

	public String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}

	private String _var;

}
