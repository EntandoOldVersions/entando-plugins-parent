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
package org.entando.entando.plugins.jpfrontshortcut.aps.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;

/**
 * Restituisce il valore di un parametro del contesto della richiesta.
 * Il Parametro è restituito in funzione della showlet nella quale il tag è inserito.
 * @author E.Santoboni
 */
public class RequestContextParamTag extends TagSupport {
	
	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		try {
			RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
			Object value = reqCtx.getExtraParam(this.getParam());
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
	
	@Override
	public void release() {
		super.release();
		this._param = null;
		this._var = null;
	}
	
	public String getParam() {
		return _param;
	}
	public void setParam(String param) {
		this._param = param;
	}
	public String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}
	
	private String _param;
	private String _var;
	
}