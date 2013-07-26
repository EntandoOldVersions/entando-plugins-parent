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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.org.entando.entando.aps.system.services.page.Widget;
import com.agiletec.aps.tags.ExecShowletTag;
import com.agiletec.plugins.jpmyportal.aps.system.JpmyportalSystemConstants;
import com.agiletec.plugins.jpmyportal.aps.system.services.pagemodel.Frame;
import com.agiletec.plugins.jpmyportal.aps.system.services.pagemodel.MyPortalPageModel;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.model.PageModelUserConfigBean;

/**
 * Tag per l'esecuzione preliminare delle showlet estendente {@link ExecShowletTag}.
 * Pubblica, ove possibile, le showlet contenute nella configurazione personalizzata dell'utente corrente.
 * Da usare esclusivamente nella main.jsp.
 */
public class MyPortalExecShowletTag extends ExecShowletTag {
	
	/**
	 * Prende il codice del modello di pagina corrente e, se c'è un utente loggato controlla che quest'ultimo abbia una configurazione 
	 * personalizzata. In tal caso quest'ultima viene posta in sessione.
	 * Contemporaneamente viene posta nel contesto della richiesta una lista contenente i frame configurabili estratta dalla configurazione
	 * del modello di pagina dato
	 */
	@Override
	protected void buildShowletOutput(IPage page, String[] showletOutput) throws JspException {
		//prende il codice del modello di pagina corrente
		HttpServletRequest req =  (HttpServletRequest) this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) req.getAttribute(RequestContext.REQCTX);
		try {
			BodyContent body = this.pageContext.pushBody();			
			PageModelUserConfigBean userConfigBean = (PageModelUserConfigBean) req.getSession().getAttribute(JpmyportalSystemConstants.SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG);
			org.entando.entando.aps.system.services.page.Widget[] showletsToRender = getShowletsToRender(page, userConfigBean);
			Map<Integer, Frame> swappableFrames = this.getSwappableFrames(page, showletsToRender);
			reqCtx.addExtraParam(JpmyportalSystemConstants.EXTRAPAR_CURRENT_PAGE, page.getModel());
			reqCtx.addExtraParam(JpmyportalSystemConstants.EXTRAPAR_CURRENT_PAGE_MODEL_SWAPPABLE_FRAMES, swappableFrames);
			for (int scan = 0; scan < showletsToRender.length; scan++) {
				reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME, new Integer(scan));
				body.clearBody();
				this.includeShowlet(reqCtx, showletsToRender[scan]);
				showletOutput[scan] = body.getString();
			}
		} catch (Throwable t) {
			String msg = "Errore in preelaborazione showlets";
			throw new JspException(msg, t);
		}
	}
	
	/**
	 * Crea una array contenente le showlet che verranno renderizzate nella pagina corrente, prendendole sia dalla configurazione di
	 * default che da quella personalizzata dell'utente.
	 * @param page
	 * @param configuration
	 * @return l'array con le showlet da renderizzare
	 */
	private org.entando.entando.aps.system.services.page.Widget[] getShowletsToRender(IPage page, PageModelUserConfigBean configuration) {
		org.entando.entando.aps.system.services.page.Widget[] defaultShowlets = page.getShowlets();
		org.entando.entando.aps.system.services.page.Widget[] customShowlets = null;
		int showletNumber = defaultShowlets.length;
		org.entando.entando.aps.system.services.page.Widget[] mergedShowlets = null;
		if (null == configuration) {
			return defaultShowlets;
		}
		customShowlets = configuration.getConfig().get(page.getModel().getCode());
		mergedShowlets = new org.entando.entando.aps.system.services.page.Widget[showletNumber];
		if (null != customShowlets) {
			for (int scan = 0; scan < showletNumber; scan++) {
				if (null == customShowlets[scan]) {
					mergedShowlets[scan] = defaultShowlets[scan];
				} else {
					mergedShowlets[scan] = customShowlets[scan];
				}
			}
			return mergedShowlets;
		}
		return defaultShowlets;
	}
	
	/**
	 * Costruisce la mappa dei frame che possono essere configurati dall'utente loggato, escludendo i frame con attributo 'locked' che
	 * non possono essere modificati e quelli senza showlet assegnata
	 * @param page la pagina corrente 
	 * @param showlets la mappa delle showlets da renderizzare nella pagina corrente
	 * @return la mappa dei frame che possono essere configurati dall'utente loggato
	 */
	private Map<Integer, Frame> getSwappableFrames(IPage page, org.entando.entando.aps.system.services.page.Widget[] showlets) {
		Map<Integer, Frame> swappableFrames = new HashMap<Integer, Frame>();
		Frame[] frames = ((MyPortalPageModel) page.getModel()).getFrameConfigs();
		for (int scan = 0; scan < showlets.length; scan++) {
			Frame currentFrame = frames[scan];
			if (null != currentFrame && !currentFrame.isLocked() && null != showlets[scan]) {
				swappableFrames.put(scan, currentFrame);
			}
		}
		return swappableFrames;
	}
	
}