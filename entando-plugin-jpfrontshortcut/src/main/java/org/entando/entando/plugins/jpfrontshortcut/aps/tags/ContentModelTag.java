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
package org.entando.entando.plugins.jpfrontshortcut.aps.tags;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;

import javax.servlet.ServletRequest;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.entando.entando.aps.system.services.page.Widget;

/**
 * @author E.Santoboni
 */
public class ContentModelTag extends TagSupport {

	@Override
	public int doStartTag() throws JspException {
		ServletRequest request = this.pageContext.getRequest();
		try {
			RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
			org.entando.entando.aps.system.services.page.Widget showlet = (org.entando.entando.aps.system.services.page.Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_SHOWLET);
            ApsProperties showletConfig = showlet.getConfig();
            String modelId = this.extractModelId(showletConfig, reqCtx);
			if (null != modelId) {
				String var = this.getVar();
				if (null == var || "".equals(var)) {
					this.pageContext.getOut().print(modelId);
				} else {
					this.pageContext.setAttribute(this.getVar(), modelId);
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "doStartTag");
			throw new JspException("Error on do start tag", t);
		}
		this.release();
		return super.doStartTag();
	}

	protected String extractModelId(ApsProperties showletConfig, RequestContext reqCtx) {
		String modelId = (null != showletConfig) ? (String) showletConfig.get("modelId") : null;
		if (null == modelId) {
			modelId = reqCtx.getRequest().getParameter("modelId");
		}
		if (null == modelId && null != this.getContentId()) {
			IContentManager contentManager = (IContentManager) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_MANAGER, this.pageContext);
			modelId = contentManager.getDefaultModel(this.getContentId());
		}
		return modelId;
	}

	@Override
	public void release() {
		super.release();
		this._contentId = null;
		this._var = null;
	}

	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}

	public String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}

	private String _contentId;
	private String _var;

}
