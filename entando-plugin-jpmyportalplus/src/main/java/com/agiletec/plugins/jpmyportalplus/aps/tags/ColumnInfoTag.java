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
package com.agiletec.plugins.jpmyportalplus.aps.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.pagemodel.Frame;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.pagemodel.MyPortalPageModel;

/**
 * @author E.Santoboni
 */
public class ColumnInfoTag extends TagSupport {
	
	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		try {
			RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
			IPage currPage = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
			String value = null;
			if (null == this.getParamName() || this.getParamName().equals(PARAM_NAME_FIRST_FRAME_ID)) {
				MyPortalPageModel model = (MyPortalPageModel) currPage.getModel();
				Frame[] frames = model.getFrameConfigs();
				for (int i = 0; i < frames.length; i++) {
					Frame frame = frames[i];
					//FIXME attenzione: getColumn può essere null
					if (null != frame.getColumn() && (frame.getColumn().equals(this.getColumnId()))) {
						value = String.valueOf(frame.getPos());
					}
					if (null != value) break;
				}
			}
			if (null != value) {
				String var = this.getVar();
				if (null == var || "".equals(var)) {
					this.pageContext.getOut().print(value);
				} else {
					this.pageContext.setAttribute(this.getVar(), value);
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "doStartTag");
			throw new JspException("Errore inizializzazione tag", t);
		}
		return super.doStartTag();
	}
	
	public Integer getColumnId() {
		return _columnId;
	}
	public void setColumnId(Integer columnId) {
		this._columnId = columnId;
	}
	
	public String getParamName() {
		return _paramName;
	}
	public void setParamName(String paramName) {
		this._paramName = paramName;
	}
	
	public String getVar() {
		return var;
	}
	public void setVar(String var) {
		this.var = var;
	}
	
	private Integer _columnId;
	private String _paramName;
	private String var;
	
	public static final String PARAM_NAME_FIRST_FRAME_ID = "firstFrameId";
	
}
