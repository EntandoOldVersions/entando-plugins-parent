/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
* Entando is a free software; 
* You can redistribute it and/or modify it
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
package com.agiletec.plugins.jpcontentfeedback.aps.tags;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.tags.InternalServletTag;

import org.entando.entando.aps.system.services.page.Widget;

/**
 * Tag che consente la visualisualizzazione del blocco jpcontentFeedback per la publicazione del rating del contenuto,
 * dei commenti e del rating dei commenti
 * @author D.Cherchi
 *
 */
public class FeedbackIntroTag extends InternalServletTag {

	@Override
	protected void includeShowlet(RequestContext reqCtx, ResponseWrapper responseWrapper, org.entando.entando.aps.system.services.page.Widget showlet) throws ServletException, IOException {
		String actionPath = "/ExtStr2/do/jpcontentfeedback/FrontEnd/contentfeedback/intro.action";
		String requestActionPath = reqCtx.getRequest().getParameter(REQUEST_PARAM_ACTIONPATH);
		String currentFrameActionPath = reqCtx.getRequest().getParameter(REQUEST_PARAM_FRAMEDEST);
		Integer currentFrame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);

		if (requestActionPath != null && currentFrameActionPath != null && currentFrame.toString().equals(currentFrameActionPath)) {
			actionPath = requestActionPath;
		}

		StringBuffer params = new StringBuffer();

		// 1) showlet 2) tag 3)request
		String contentId = null;
		if (null != this.getContentId()) {
			contentId = this.getContentId();
		}


		if (null != contentId) {
			reqCtx.getRequest().setAttribute("currentContentId", contentId);
		}

		if (null != this.getReverseVotes() && this.getReverseVotes().equalsIgnoreCase("true")) {
			if (params.length() > 0) params.append("&");
			params.append("reverseVotes=true");
		}

		if (StringUtils.isNotBlank(this.getExtraParamsRedirect())) {
			String[] redirectParams = this.getExtraParamsRedirect().split(",");
			reqCtx.getRequest().setAttribute("extraParamNames", redirectParams);
		}
		actionPath = actionPath + "?" + params.toString();

		RequestDispatcher requestDispatcher = reqCtx.getRequest().getRequestDispatcher(actionPath);
		requestDispatcher.include(reqCtx.getRequest(), responseWrapper);

	}

	@Override
	public void release() {
		super.release();
		this.setContentId(null);
		this.setReverseVotes(null);
		this.setExtraParamsRedirect(null);
	}

	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}

	public String getReverseVotes() {
		return _reverseVotes;
	}
	public void setReverseVotes(String reverseVotes) {
		this._reverseVotes = reverseVotes;
	}

	public String getExtraParamsRedirect() {
		return _extraParamsRedirect;
	}
	public void setExtraParamsRedirect(String extraParamsRedirect) {
		this._extraParamsRedirect = extraParamsRedirect;
	}

	private String _contentId;
	private String _reverseVotes;
	private String _extraParamsRedirect;
}
