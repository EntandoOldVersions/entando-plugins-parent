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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpmyportalplus.aps.system.JpmyportalplusSystemConstants;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.pagemodel.Frame;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.pagemodel.MyPortalPageModel;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.IPageUserConfigManager;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.CustomPageConfig;
import com.agiletec.plugins.jpmyportalplus.aps.tags.util.FrameSelectItem;

import org.entando.entando.aps.system.services.page.IPage;
import org.entando.entando.aps.system.services.page.Widget;

/**
 * Returns the list of select items to use in the select inside the frame swap function of each widget
 * @author E.Santoboni
 */
public class FrameSelectItemTag extends TagSupport {

	@Override
	public int doStartTag() throws JspException {
		RequestContext reqCtx = (RequestContext) this.pageContext.getRequest().getAttribute(RequestContext.REQCTX);
		List<FrameSelectItem> selectItems = new ArrayList<FrameSelectItem>();
		IPageUserConfigManager pageUserConfigManager = (IPageUserConfigManager) ApsWebApplicationUtils.getBean(JpmyportalplusSystemConstants.PAGE_USER_CONFIG_MANAGER, pageContext);
		try {
			Integer currentFrame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
			IPage currentPage = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
			MyPortalPageModel pageModel = (MyPortalPageModel) currentPage.getModel();
			Integer currentColumnId = pageModel.getFrameConfigs()[currentFrame].getColumn();
			if (null == currentColumnId) {
				return super.doStartTag();
			}
			Lang currentLang = (Lang) this.pageContext.getSession().getAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_LANG);
			Widget[] customShowletConfig = this.getCustomShowletConfig(currentPage, pageUserConfigManager);
			Widget[] showletsToRender = pageUserConfigManager.getShowletsToRender(currentPage, customShowletConfig);

			String voidShowletCode = pageUserConfigManager.getVoidShowlet().getCode();
			for (int i = 0; i < showletsToRender.length; i++) {
				Frame frame = pageModel.getFrameConfigs()[i];
				Integer columnId = frame.getColumn();
				if (frame.isLocked() || null == columnId || i == currentFrame.intValue()) continue;
				Widget showlet = showletsToRender[i];
				if (columnId.equals(currentColumnId)) {
					if (showlet != null && !showlet.getType().getCode().equals(voidShowletCode)) {
						FrameSelectItem item = new FrameSelectItem(currentColumnId, columnId,
								showlet, i, currentLang);
						selectItems.add(item);
					}
				} else {
					if (showlet == null || showlet.getType().getCode().equals(voidShowletCode)) {
						boolean check = this.check(selectItems, columnId);
						if (!check) {
							FrameSelectItem item = new FrameSelectItem(currentColumnId, columnId,
									null, i, currentLang);
							selectItems.add(item);
						}
					}
				}
			}
			this.pageContext.setAttribute(this.getVar(), selectItems);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "doStartTag");
			throw new JspException("Errore inizializzazione tag", t);
		}
		return super.doStartTag();
	}

	private boolean check(List<FrameSelectItem> selectItems, Integer columnId) {
		for (Iterator iterator = selectItems.iterator(); iterator.hasNext();) {
			FrameSelectItem frameSelectItem = (FrameSelectItem) iterator.next();
			if (columnId.equals(frameSelectItem.getColumnIdDest())) {
				return true;
			}
		}
		return false;
	}

	protected Widget[] getCustomShowletConfig(IPage currentPage, IPageUserConfigManager pageUserConfigManager) throws Throwable {
		Widget[] customShowlets = null;
		try {
			CustomPageConfig customPageConfig =
				(CustomPageConfig) this.pageContext.getSession().getAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_PAGE_CONFIG);
			if (customPageConfig != null && !customPageConfig.getPageCode().equals(currentPage.getCode())) {
				//throw new RuntimeException("Current page '" + currentPage.getCode()
				//		+ "' not equals then pageCode of custom config param '" + customPageConfig.getPageCode() + "'");
				ApsSystemUtils.getLogger().severe("Current page '" + currentPage.getCode()
						+ "' not equals then pageCode of custom config param '" + customPageConfig.getPageCode() + "'");
				return null;
			}
			if (null != customPageConfig) {
				customShowlets = customPageConfig.getConfig();
			}
		} catch (Throwable t) {
			String message = "Errore in estrazione custom showlets";
			ApsSystemUtils.logThrowable(t, this, "getCustomShowletConfig", message);
			throw new ApsSystemException(message, t);
		}
		return customShowlets;
	}

	public String getVar() {
		return var;
	}
	public void setVar(String var) {
		this.var = var;
	}

	private String var;

}
