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

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.Showlet;
import com.agiletec.aps.tags.InternalServletTag;

/**
 * Tag che consente la visualisualizzazione del blocco jpcontentFeedback per la publicazione del rating del contenuto,
 * dei commenti e del rating dei commenti
 * @author D.Cherchi
 *
 */
public class FeedbackIntroTag extends InternalServletTag {

	@Override
	protected void includeShowlet(RequestContext reqCtx, ResponseWrapper responseWrapper, Showlet showlet) throws ServletException, IOException {
		String actionPath = "/ExtStr2/do/jpcontentfeedback/FrontEnd/contentfeedback/intro.action";
		String requestActionPath = reqCtx.getRequest().getParameter(REQUEST_PARAM_ACTIONPATH);
		String currentFrameActionPath = reqCtx.getRequest().getParameter(REQUEST_PARAM_FRAMEDEST);
		Integer currentFrame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
		if (requestActionPath != null && currentFrameActionPath != null && currentFrame.toString().equals(currentFrameActionPath)) {
			actionPath = requestActionPath;
		}
		RequestDispatcher requestDispatcher = reqCtx.getRequest().getRequestDispatcher(actionPath);
		requestDispatcher.include(reqCtx.getRequest(), responseWrapper);
	}

}
