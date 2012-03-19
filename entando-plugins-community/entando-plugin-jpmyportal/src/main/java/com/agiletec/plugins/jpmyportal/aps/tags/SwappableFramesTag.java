/*
 *
 * Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
 *
 * This file is part of Entando software.
 * jAPS is a free software; 
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
package com.agiletec.plugins.jpmyportal.aps.tags;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.plugins.jpmyportal.aps.system.JpmyportalSystemConstants;
import com.agiletec.plugins.jpmyportal.aps.system.services.pagemodel.Frame;
import com.agiletec.plugins.jpmyportal.aps.system.services.pagemodel.MyPortalPageModel;

/**
 * Tag che popola, per il frame corrente, una mappa contenente tutti i frame personalizzabili (con l'esclusione del frame corrente).
 * La mappa viene inserita in una variabile del page context con nome dell'attributo var.
 */
public class SwappableFramesTag extends TagSupport {
	
	/**
	 * Genera la lista dei frame personalizzabili con l'esclusione del frame che ha invocato il tag. Nell'improbabile caso in cui il frame
	 * invocante NON sia contenuto nella mappa salvata nel contesto della request, verrà lanciata un'eccezione.
	 *
	 * @throws JspException in caso di errori nella gestione della mappa.
	 */
	@Override
	public int doStartTag() throws JspException {
		RequestContext reqCtx = (RequestContext) this.pageContext.getRequest().getAttribute(RequestContext.REQCTX);
		Map<Integer, Frame> reqCtxFramesMap = (Map<Integer, Frame>) reqCtx.getExtraParam(JpmyportalSystemConstants.EXTRAPAR_CURRENT_PAGE_MODEL_SWAPPABLE_FRAMES);
		Map<Integer, Frame> localFramesMap = new HashMap<Integer, Frame>();
		IPage page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
		if (null != page ) {
			MyPortalPageModel currentPageModel =  (MyPortalPageModel) page.getModel();
			if (null != currentPageModel && null != reqCtxFramesMap) {
				Integer frameId = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
				localFramesMap.putAll(reqCtxFramesMap);
				// rimuovi il frame corrente dalla lista
				if (localFramesMap.containsKey(frameId.intValue())) {
					localFramesMap.remove(frameId.intValue());
					if (localFramesMap.isEmpty()) {
						localFramesMap = null;
					}
				} else {
					throw new JspException("Errore nella mappa dei frame personalizzabili in \"SwappableFramesTag\"");
				}
				this.pageContext.setAttribute(this.getVar(), localFramesMap);
			}
		}
		return SKIP_BODY;
	}
	
	public String getVar() {
		return _var;
	}
	public void setVar(String ctxName) {
		this._var = ctxName;
	}
	
	/**
	 * Il nome con il quale la lista è posta nel contesto
	 */
	private String _var;
	
}