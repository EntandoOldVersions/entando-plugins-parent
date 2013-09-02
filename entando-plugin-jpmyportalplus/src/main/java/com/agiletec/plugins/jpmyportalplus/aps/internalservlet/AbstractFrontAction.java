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
package com.agiletec.plugins.jpmyportalplus.aps.internalservlet;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpmyportalplus.aps.system.JpmyportalplusSystemConstants;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.IPageUserConfigManager;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.CustomPageConfig;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.PageUserConfigBean;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.ShowletUpdateInfoBean;

/**
 * @author E.Santoboni
 */
public abstract class AbstractFrontAction extends BaseAction implements IFrontAction, ServletResponseAware {

	@Override
	public String swapFrames() {
		try {
			IPage currentPage = this.getCurrentPage();
			CustomPageConfig config = this.getCustomPageConfig();
			Widget[] customShowlets = (null == config || config.getConfig() == null) ? null : config.getConfig();
			Widget[] showletsToRender = this.getPageUserConfigManager().getShowletsToRender(currentPage, customShowlets);

			Widget showletToMove = showletsToRender[this.getStartFramePos()];
			Integer statusShowletToMoveInteger = this.getCustomShowletStatus() != null ? this.getCustomShowletStatus()[this.getStartFramePos()] : null;
			int statusShowletToMove = (statusShowletToMoveInteger == null) ? 0 : statusShowletToMoveInteger;
			ShowletUpdateInfoBean frameTargetUpdate =
				new ShowletUpdateInfoBean(this.getTargetFramePos(), showletToMove, statusShowletToMove);
			this.addUpdateInfoBean(frameTargetUpdate);
			Widget showletOnFrameDest = showletsToRender[this.getTargetFramePos()];
			Integer statusShowletOnFrameDestInteger = this.getCustomShowletStatus() != null ? this.getCustomShowletStatus()[this.getTargetFramePos()] : null;
			int statusShowletOnFrameDest = (statusShowletOnFrameDestInteger == null) ? 0 : statusShowletOnFrameDestInteger;
			ShowletUpdateInfoBean frameStartUpdate =
				new ShowletUpdateInfoBean(this.getStartFramePos(), showletOnFrameDest, statusShowletOnFrameDest);
			this.addUpdateInfoBean(frameStartUpdate);

			this.executeUpdateUserConfig(currentPage);
			this.updateSessionParams();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "swapFrames", "Error on swapFrames");
			return FAILURE;
		}
		return SUCCESS;
	}

	protected boolean executeResetFrame() throws ApsSystemException {
		try {
			IPage currentPage = this.getCurrentPage();
			ShowletUpdateInfoBean resetFrame =
				new ShowletUpdateInfoBean(this.getFrameToEmpty(), this.getShowletVoid(), IPageUserConfigManager.STATUS_OPEN);
			this.addUpdateInfoBean(resetFrame);
			this.executeUpdateUserConfig(currentPage);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "executeResetFrame", "Error on executeResetFrame");
			return false;
		}
		return true;
	}

	protected boolean executeCloseFrame() {
		return this.executeResizeFrame(IPageUserConfigManager.STATUS_CLOSE);
	}

	protected boolean executeOpenFrame() {
		return this.executeResizeFrame(IPageUserConfigManager.STATUS_OPEN);
	}

	protected boolean executeResizeFrame(int status) {
		try {
			IPage currentPage = this.getCurrentPage();
			CustomPageConfig config = this.getCustomPageConfig();
			Widget[] customShowlets = (null == config || config.getConfig() == null) ? null : config.getConfig();
			Widget[] showletsToRender = this.getPageUserConfigManager().getShowletsToRender(currentPage, customShowlets);
			Widget showlet = showletsToRender[this.getFrameToResize()];
			if (null == showlet) return true;
			ShowletUpdateInfoBean resizingFrame =
				new ShowletUpdateInfoBean(this.getFrameToResize(), showlet, status);
			this.addUpdateInfoBean(resizingFrame);
			this.executeUpdateUserConfig(currentPage);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "executeResizeFrame", "Error on resize frame");
			return false;
		}
		return true;
	}

	protected void updateSessionParams() throws Throwable {
		ShowletUpdateInfoBean[] infos = this.getUpdateInfos();
		if (null == infos || infos.length == 0) {
			return;
		}
		Logger log = ApsSystemUtils.getLogger();
		try {
			//AGGIORNARE SE L'UTENTE CORRENTE è DIVERSO DA GUEST
			//public static final String SESSIONPARAM_CURRENT_CUSTOM_USER_PAGE_CONFIG = "jpmyportalplus_currentCustomUserPageConfig";
			//AGGIORARE SEMPRE
			//public static final String SESSIONPARAM_CURRENT_CUSTOM_PAGE_CONFIG = "jpmyportalplus_currentCustomPageConfig";
			IPage currentPage = this.getCurrentPage();
			UserDetails currentUser = super.getCurrentUser();
			if (null == currentUser) {
				currentUser = this.getUserManager().getGuestUser();
				this.getRequest().getSession().setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, currentUser);
			}
			if (!currentUser.getUsername().equals(SystemConstants.GUEST_USER_NAME)) {
				CustomPageConfig customUserPageConfig = null;
				PageUserConfigBean pageUserConfigBean =
					(PageUserConfigBean) this.getRequest().getSession().getAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_USER_PAGE_CONFIG);
				if (null == pageUserConfigBean) {
					log.info("No Page User Config by user " + currentUser.getUsername());
					pageUserConfigBean = this.createNewPageUserConfig(infos, currentUser, currentPage);
					this.getRequest().getSession().setAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_USER_PAGE_CONFIG, pageUserConfigBean);
					customUserPageConfig = pageUserConfigBean.getConfig().get(currentPage.getCode());
				} else {
					customUserPageConfig = pageUserConfigBean.getConfig().get(currentPage.getCode());
					if (null == customUserPageConfig) {
						customUserPageConfig = this.createNewPageConfig(infos, currentPage);
						pageUserConfigBean.getConfig().put(currentPage.getCode(), customUserPageConfig);
					} else {
						this.updatePageConfig(customUserPageConfig, infos);
					}
				}
				this.getRequest().getSession().setAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_PAGE_CONFIG, customUserPageConfig);
			} else {
				CustomPageConfig customGuestPageConfig =
					(CustomPageConfig) this.getRequest().getSession().getAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_PAGE_CONFIG);
				if (null != customGuestPageConfig) {
					this.updatePageConfig(customGuestPageConfig, infos);
				} else {
					customGuestPageConfig = this.createNewPageConfig(infos, currentPage);
					this.getRequest().getSession().setAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_PAGE_CONFIG, customGuestPageConfig);
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "updateSessionParams", "Error on updateSessionParams");
			throw new ApsSystemException("Error on updating session params", t);
		}
	}

	protected Widget getShowletVoid() {
		Widget voidShowlet = new Widget();
		voidShowlet.setType(this.getPageUserConfigManager().getVoidShowlet());
		voidShowlet.setConfig(new ApsProperties());
		return voidShowlet;
	}

	protected boolean executeUpdateUserConfig(IPage currentPage) throws ApsSystemException {
		try {
			UserDetails currentUser = super.getCurrentUser();
			if (currentUser.getUsername().equals(SystemConstants.GUEST_USER_NAME)) {
				this.getPageUserConfigManager().updateGuestPageConfig(currentPage, this.getUpdateInfos(), this.getRequest(), this.getResponse());
			} else {
				this.getPageUserConfigManager().updateUserPageConfig(currentUser.getUsername(), currentPage, this.getUpdateInfos());
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "executeUpdateUserConfig", "Error on executeUpdateUserConfig");
			return false;
		}
		return true;
	}

	private PageUserConfigBean createNewPageUserConfig(ShowletUpdateInfoBean[] infos, UserDetails currentUser, IPage currentPage) {
		PageUserConfigBean bean = new PageUserConfigBean(currentUser.getUsername());
		CustomPageConfig pageConfig = this.createNewPageConfig(infos, currentPage);
		bean.getConfig().put(currentPage.getCode(), pageConfig);
		return bean;
	}

	private void updatePageConfig(CustomPageConfig customUserPageConfig, ShowletUpdateInfoBean[] infos) {
		for (int i = 0; i < infos.length; i++) {
			ShowletUpdateInfoBean updateInfo = infos[i];
			customUserPageConfig.getConfig()[updateInfo.getFramePos()] = updateInfo.getShowlet();
			customUserPageConfig.getStatus()[updateInfo.getFramePos()] = updateInfo.getStatus();
		}
	}

	private CustomPageConfig createNewPageConfig(ShowletUpdateInfoBean[] infos, IPage currentPage) {
		CustomPageConfig pageConfig = new CustomPageConfig(currentPage.getCode(), currentPage.getModel().getFrames().length);
		this.updatePageConfig(pageConfig, infos);
		return pageConfig;
	}

	protected Widget[] getCustomShowletConfig() throws Throwable {
		Widget[] customShowlets = null;
		try {
			CustomPageConfig customPageConfig = this.getCustomPageConfig();
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

	protected Integer[] getCustomShowletStatus() throws Throwable {
		Integer[] customShowletStatus = null;
		try {
			CustomPageConfig customPageConfig = this.getCustomPageConfig();
			if (null != customPageConfig) {
				customShowletStatus = customPageConfig.getStatus();
			}
		} catch (Throwable t) {
			String message = "Errore in estrazione custom showlet status";
			ApsSystemUtils.logThrowable(t, this, "getCustomShowletStatus", message);
			throw new ApsSystemException(message, t);
		}
		return customShowletStatus;
	}

	protected CustomPageConfig getCustomPageConfig() {
		IPage currentPage = this.getCurrentPage();
		CustomPageConfig config = (CustomPageConfig) this.getRequest().getSession().getAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_PAGE_CONFIG);
		if (config != null && !config.getPageCode().equals(currentPage.getCode())) {
			ApsSystemUtils.getLogger().severe("Current page '" + currentPage
					+ "' not equals then pageCode of custom config param '" + config.getPageCode() + "'");
			return null;
		}
		return config;
	}

	protected IPage getCurrentPage() {
		return this.getPageManager().getPage(this.getCurrentPageCode());
	}

	protected Lang getCurrentSessionLang() {
		return (Lang) this.getRequest().getSession().getAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_LANG);
	}

	protected void addUpdateInfoBean(ShowletUpdateInfoBean toAdd) {
		ShowletUpdateInfoBean[] infos = this.getUpdateInfos();
		int len = infos.length;
		ShowletUpdateInfoBean[] newInfos = new ShowletUpdateInfoBean[len + 1];
		for(int i=0; i < len; i++){
			newInfos[i] = infos[i];
		}
		newInfos[len] = toAdd;
		this.setUpdateInfos(newInfos);
	}

	public String getCurrentPageCode() {
		return _currentPageCode;
	}
	public void setCurrentPageCode(String currentPageCode) {
		this._currentPageCode = currentPageCode;
	}

	public Integer getStartFramePos() {
		return _startFramePos;
	}
	public void setStartFramePos(Integer startFramePos) {
		this._startFramePos = startFramePos;
	}

	public Integer getTargetFramePos() {
		return _targetFramePos;
	}
	public void setTargetFramePos(Integer targetFramePos) {
		this._targetFramePos = targetFramePos;
	}

	public Integer getFrameToEmpty() {
		return _frameToEmpty;
	}
	public void setFrameToEmpty(Integer frameToEmpty) {
		this._frameToEmpty = frameToEmpty;
	}

	public Integer getFrameToResize() {
		return _frameToResize;
	}
	public void setFrameToResize(Integer frameToResize) {
		this._frameToResize = frameToResize;
	}

	protected ShowletUpdateInfoBean[] getUpdateInfos() {
		return _updateInfos;
	}
	protected void setUpdateInfos(ShowletUpdateInfoBean[] updateInfos) {
		this._updateInfos = updateInfos;
	}

	protected HttpServletResponse getResponse() {
		return _servletResponse;
	}
	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this._servletResponse = servletResponse;
	}

	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	protected IPageUserConfigManager getPageUserConfigManager() {
		return _pageUserConfigManager;
	}
	public void setPageUserConfigManager(IPageUserConfigManager pageUserConfigManager) {
		this._pageUserConfigManager = pageUserConfigManager;
	}

	protected IUserManager getUserManager() {
		return _userManager;
	}
	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}

	public IWidgetTypeManager getWidgetTypeManager() {
		return _widgetTypeManager;
	}

	public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
		this._widgetTypeManager = widgetTypeManager;
	}

	private String _currentPageCode;

	private Integer _startFramePos;
	private Integer _targetFramePos;
	private Integer _frameToEmpty;
	private Integer _frameToResize;

	private ShowletUpdateInfoBean[] _updateInfos = new ShowletUpdateInfoBean[0];

	private HttpServletResponse _servletResponse;

	private IPageManager _pageManager;
	private IPageUserConfigManager _pageUserConfigManager;
	private IUserManager _userManager;
	private IWidgetTypeManager _widgetTypeManager;

}
