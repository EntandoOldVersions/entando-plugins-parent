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
package com.agiletec.plugins.jpcontentfeedback.aps.tags;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.Showlet;
import com.agiletec.aps.tags.InternalServletTag;
import com.agiletec.aps.tags.util.PagerVO;
import com.agiletec.aps.util.ApsProperties;

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

		StringBuffer params = new StringBuffer();
		
		/*
		if (null != showlet) {
			ApsProperties config = showlet.getConfig();
			if (null != config) {
				Iterator<Object> it = config.keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					String value = (String) config.get(key);
					if (params.length() > 0) params.append("&");
					//se il parametro è contentId ... viene gestito dopo
					if (key.equalsIgnoreCase("contentId")) {
						//params.append(key).append("=").append(contentId);
					} else {
						params.append(key).append("=").append(value);
					}
				}
			}
		}
		*/
		
		// 1) showlet 2) tag 3)request
		String contentId = null;
		if (showlet.getConfig().containsKey("contentId") && null != showlet.getConfig().getProperty("contentId")) {
			contentId = showlet.getConfig().getProperty("contentId");
		} else if (null != this.getContentId())	{
			contentId = this.getContentId();
		} else {
			contentId = reqCtx.getRequest().getParameter("contentId");								
		}
		if (params.length() > 0) params.append("&");
		params.append("contentId").append("=").append(contentId);

		
		if (null != this.getReverseVotes() && this.getReverseVotes().equalsIgnoreCase("true")) {
			if (params.length() > 0) params.append("&");
			params.append("reverseVotes=true");
		}
		

		if (null != this.getListViewerPagerObjectName()) {
			PagerVO pager = (PagerVO) pageContext.getAttribute(this.getListViewerPagerObjectName());
			pageContext.getRequest().setAttribute("listViewerPagerId", pager.getParamItemName());
			pageContext.getRequest().setAttribute("listViewerPagerValue", pager.getCurrItem());
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
		this.setListViewerPagerObjectName(null);
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

	public String getListViewerPagerObjectName() {
		return _listViewerPagerObjectName;
	}
	public void setListViewerPagerObjectName(String listViewerPagerObjectName) {
		this._listViewerPagerObjectName = listViewerPagerObjectName;
	}

	private String _contentId;
	private String _reverseVotes;
	private String _listViewerPagerObjectName;
}
