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
package com.agiletec.plugins.jpcontentfeedback.aps.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.Showlet;
import com.agiletec.aps.util.ApsProperties;

/**
 * Tag di utilità per la verifica dei parametri di configurazione della showlet
 * Visualizza il corpo del tag se la showlet corrente è configurata per la visualizzazione del corrispettivo componente
 * specificato come attributo del tag.
 * @version 1.0
 * @author D.Cherchi
 */
public class CheckOptionContentFeedbackTag extends TagSupport {

	public int doStartTag() throws JspException {
		try {
			boolean isAuthorized = false;
			ServletRequest request =  this.pageContext.getRequest();
			RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
			Showlet showlet = (Showlet) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_SHOWLET);
			ApsProperties config = showlet.getConfig();
			if (this.getParam()!=null && this.getParam().length()>0){
				String value = config.getProperty(this.getParam());
				if (value!= null && value.equalsIgnoreCase("true")){
					isAuthorized = true;
				}
			}
			if (null != this.getVar()) {
				this.pageContext.setAttribute(this.getVar(), new Boolean(isAuthorized));
			}
			if (isAuthorized) {
				return EVAL_BODY_INCLUDE;
			} else {
				return SKIP_BODY;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "doStartTag");
			throw new JspException("Error during tag initialization ", t);
		}
	}

	/**
	 * Setta il nome del parametro tramite il quale settare nella request
	 * il buleano rappresentativo del risultato del controllo di autorizzazione.
	 * @param resultParamName Il nome del parametro.
	 */
	public void setVar(String var) {
		this._var = var;
	}

	/**
	 * Restituisce il nome del parametro tramite il quale settare nella request
	 * il buleano rappresentativo del risultato del controllo di autorizzazione.
	 * @return Il nome del parametro.
	 */
	public String getVar() {
		return _var;
	}

	public void setParam(String param) {
		this._param = param;
	}

	public String getParam() {
		return _param;
	}

	private String _param;
	private String _var;

}
