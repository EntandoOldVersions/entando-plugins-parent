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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpmyportal.aps.system.services.pagemodel.Frame;
import com.agiletec.plugins.jpmyportal.aps.system.services.pagemodel.MyPortalPageModel;

/**
 * Tag che verifica che il frame corrente sia tra quelli personalizzabili dall'utente corrente.
 * Il frame è personalizzabile se non è bloccato (locked="false" nel modello di pagina) e se l'utente corrente non è né guest né admin.
 * Se il frame è personalizzabile verrà valutato il body del tag.
 */
public class CustomizableFrameTag extends TagSupport {
	
	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest req =  (HttpServletRequest) this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) req.getAttribute(RequestContext.REQCTX);
		UserDetails currentUser = (UserDetails) this.pageContext.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		// se l'utente è GUEST o ADMIN allora esci immediatamente
		if (currentUser.getUsername().equals(SystemConstants.ADMIN_USER_NAME) || 
				currentUser.getUsername().equals(SystemConstants.GUEST_USER_NAME)) {
			return SKIP_BODY;
		}
		try {
			IPage page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
			MyPortalPageModel currentPageModel =  (MyPortalPageModel) page.getModel();
			if (null != currentPageModel) {
				Integer frameId = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
				if (null != frameId) {
					Frame frame = currentPageModel.getFrameConfigs()[frameId.intValue()];
					if (null != frame && !frame.isLocked()) {
						return EVAL_BODY_INCLUDE;
					}
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "doStartTag");
			throw new JspException("Errore inizializzazione tag \"CustomizableFrameTag\"", t);
		}
		return SKIP_BODY;
	}
	
}