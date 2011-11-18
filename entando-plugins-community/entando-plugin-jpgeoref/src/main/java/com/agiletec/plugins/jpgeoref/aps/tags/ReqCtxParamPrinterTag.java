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
package com.agiletec.plugins.jpgeoref.aps.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;

/**
 * @author E.Santoboni
 */
public class ReqCtxParamPrinterTag extends TagSupport {

	/**
	 * End tag analysis.
	 */
	public int doEndTag() throws JspException {
		ServletRequest req =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) req.getAttribute(RequestContext.REQCTX);
		try {
			Object value = reqCtx.getExtraParam(this.getVar());
			if (value == null) value = "";
			this.pageContext.getOut().print(value);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "doStartTag");
			throw new JspException("Error in end tag", t);
		}
		return EVAL_PAGE;
	}

	/**
	 * Sets tag attribute
	 * @param var tag attribute
	 */
	public void setVar(String var) {
		this._var = var;
	}
	/**
	 * Returns tag attribute
	 * @return tag attribute
	 */
	public String getVar() {
		return _var;
	}


	private String _var; // tag attribute

}
