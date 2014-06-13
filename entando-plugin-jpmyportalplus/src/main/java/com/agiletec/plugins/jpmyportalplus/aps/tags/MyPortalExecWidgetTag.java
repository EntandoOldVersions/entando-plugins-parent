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
package com.agiletec.plugins.jpmyportalplus.aps.tags;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class MyPortalExecWidgetTag /*extends ExecWidgetTag*/ {
	
	private static final Logger _logger = LoggerFactory.getLogger(MyPortalExecWidgetTag.class);
	/*
	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest req =  (HttpServletRequest) this.pageContext.getRequest();
		try {
			HttpSession session = req.getSession();
			RequestContext reqCtx = (RequestContext) req.getAttribute(RequestContext.REQCTX);
			Lang lang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
			req.getSession().setAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_LANG, lang);
			UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			if (!currentUser.getUsername().equals(SystemConstants.GUEST_USER_NAME)) {
				PageUserConfigBean userConfigBean = (PageUserConfigBean) session.getAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_USER_PAGE_CONFIG);
				if (null == userConfigBean || !currentUser.getUsername().equals(userConfigBean.getUsername())) {
					IPageUserConfigManager pageUserConfigManager = (IPageUserConfigManager) ApsWebApplicationUtils.getBean(JpmyportalplusSystemConstants.PAGE_USER_CONFIG_MANAGER, pageContext);
					userConfigBean = pageUserConfigManager.getUserConfig(currentUser);
					if (null != userConfigBean) {
						session.setAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_USER_PAGE_CONFIG, userConfigBean);
					} else {
						session.removeAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_USER_PAGE_CONFIG);
					}
				}
			} else {
				session.removeAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_USER_PAGE_CONFIG);
			}
		} catch (Throwable t) {
			String msg = "Error detected during preprocessing doStartTag";
			_logger.error("Error in doStartTag", t);
			throw new JspException(msg, t);
		}
		return super.doStartTag();
	}
	*/
	/**
	 * @deprecated Use {@link #buildWidgetOutput(IPage,String[])} instead
	 */
	/*
	@Override
	protected void buildShowletOutput(IPage page, String[] showletOutput) throws JspException {
		buildWidgetOutput(page, showletOutput);
	}

	@Override
	protected void buildWidgetOutput(IPage page, String[] showletOutput) throws JspException {
		HttpServletRequest req =  (HttpServletRequest) this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) req.getAttribute(RequestContext.REQCTX);
		try {
			IPageUserConfigManager pageUserConfigManager = (IPageUserConfigManager) ApsWebApplicationUtils.getBean(JpmyportalplusSystemConstants.PAGE_USER_CONFIG_MANAGER, pageContext);
			CustomPageConfig customPageConfig = null;
			UserDetails currentUser = (UserDetails) this.pageContext.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			if (currentUser.getUsername().equals(SystemConstants.GUEST_USER_NAME)) {
				customPageConfig = pageUserConfigManager.getGuestPageConfig(page, req);
			} else {
				PageUserConfigBean userConfigBean = (PageUserConfigBean) req.getSession().getAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_USER_PAGE_CONFIG);
				if (null != userConfigBean) {
					customPageConfig = userConfigBean.getConfig().get(page.getCode());
				}
			}
			if (null == customPageConfig || customPageConfig.getConfig() == null || !customPageConfig.getPageCode().equals(page.getCode())) {
				req.getSession().removeAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_PAGE_CONFIG);
				super.buildWidgetOutput(page, showletOutput);
				return;
			}
			req.getSession().setAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_PAGE_CONFIG, customPageConfig);
			Widget[] customShowlets = customPageConfig.getConfig();
			Widget[] showletsToRender = pageUserConfigManager.getShowletsToRender(page, customShowlets);
			List<IFrameDecoratorContainer> decorators = this.extractDecorators();
			BodyContent body = this.pageContext.pushBody();
			for (int scan = 0; scan < showletsToRender.length; scan++) {
				reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME, new Integer(scan));
				body.clearBody();
				this.includeWidget(reqCtx, showletsToRender[scan], decorators);
				showletOutput[scan] = body.getString();
			}
		} catch (Throwable t) {
			_logger.error("Error detected preprocessing widget", t);
			String msg = "Error detected preprocessing showlet";
			throw new JspException(msg, t);
		}
	}
	*/
}