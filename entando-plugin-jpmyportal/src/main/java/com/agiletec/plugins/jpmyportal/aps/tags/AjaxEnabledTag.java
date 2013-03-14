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
package com.agiletec.plugins.jpmyportal.aps.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpmyportal.aps.system.JpmyportalSystemConstants;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.IPageModelUserConfigManager;

/**
 * Tag che si occupa di controllare se il modulo Ajax è abilitato. 
 * Se l'attributo var è valorizzato inserisce il risultato del controllo in una variabile del page context con nome dell'attributo var.
 * Se il modulo Ajax è abilitato verrà valutato il body del tag.
 * @author E.Mezzano
 */
public class AjaxEnabledTag extends TagSupport {
	
	@Override
	public int doStartTag() throws JspException {
		try {
			IPageModelUserConfigManager pageModelUserConfigManager = (IPageModelUserConfigManager) ApsWebApplicationUtils.getBean(JpmyportalSystemConstants.PAGE_MODEL_USER_CONFIG_MANAGER, this.pageContext);
			boolean ajaxEnabled = pageModelUserConfigManager.getConfig().isAjaxEnabled();
			String var = this.getVar();
			if (null != var && var.length()>0) {
				this.pageContext.setAttribute(var, new Boolean(ajaxEnabled));
			}
			if (ajaxEnabled) {
				return EVAL_BODY_INCLUDE;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "doStartTag");
			throw new JspException("Errore inizializzazione tag", t);
		}
		return SKIP_BODY;
	}
	
	@Override
	public void release() {
		super.release();
		this._var = null;
	}
	
	public String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}
	
	private String _var;
	
}