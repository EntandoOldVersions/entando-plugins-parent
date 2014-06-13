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
package com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.IMyPortalConfigManager;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.model.MyPortalConfig;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.CustomPageConfig;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.PageUserConfigBean;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.WidgetUpdateInfoBean;
import java.util.Map;
import org.entando.entando.plugins.jpmyportalplus.aps.system.services.pagemodel.IMyPortalPageModelManager;
import org.entando.entando.plugins.jpmyportalplus.aps.system.services.pagemodel.MyPortalFrameConfig;

/**
 * @author E.Santoboni
 */
@Aspect
public class PageUserConfigManager extends AbstractService implements IPageUserConfigManager {

	private static final Logger _logger = LoggerFactory.getLogger(PageUserConfigManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}

	@Override
	public PageUserConfigBean getUserConfig(UserDetails user) throws ApsSystemException {
		PageUserConfigBean pageUserBean = null;
		try {
			List<WidgetType> customizables = this.getMyPortalConfigManager().getCustomizableShowlets();
			for (int i = 0; i < customizables.size(); i++) {
				WidgetType type = customizables.get(i);
				String mainGroup = type.getMainGroup();
				if (null != mainGroup
						&& !mainGroup.equals(Group.FREE_GROUP_NAME)
						&& !this.getAuthorizationManager().isAuthOnGroup(user, mainGroup)) {
					this.getPageUserConfigDAO().removeUnauthorizedShowlet(user.getUsername(), type.getCode());
				}
			}
			pageUserBean = this.getPageUserConfigDAO().getUserConfig(user.getUsername());
		} catch (Throwable t) {
			_logger.error("Error reading user configuration", t);
			throw new ApsSystemException("Error reading user configuration", t);
		}
		return pageUserBean;
	}

	@Override
	@Deprecated
	public PageUserConfigBean getUserConfig(String username) throws ApsSystemException {
		PageUserConfigBean pageUserBean = null;
		try {
			pageUserBean = this.getPageUserConfigDAO().getUserConfig(username);
		} catch (Throwable t) {
			_logger.error("Error reading the user configuration", t);
			throw new ApsSystemException("Error reading the user configuration", t);
		}
		return pageUserBean;
	}

	@Override
	public CustomPageConfig getGuestPageConfig(IPage page, HttpServletRequest request) throws ApsSystemException {
		CustomPageConfig customConfig = null;
		try {
			Cookie cookie = this.getCookieGuestConfig(page, request);
			if (null == cookie
					|| cookie.getValue() == null
					|| cookie.getValue().trim().length() == 0) {
				ApsSystemUtils.getLogger().trace("Cookie nullo o invalido per pagina " + page.getCode());
				return null;
			}
			MyPortalConfig mPortalConfig = this.getMyPortalConfigManager().getConfig();
			customConfig = new CustomPageConfig(cookie, page, this.getWidgetTypeManager(), this.getMyPortalPageModelManager(), 
					mPortalConfig.getAllowedShowlets(), this.getVoidShowletCode());
			for (int i = 0; i < customConfig.getConfig().length; i++) {
				Widget showlet = customConfig.getConfig()[i];
				if (null != showlet) {
					if (null != showlet.getType()) {
						String mainGroup = showlet.getType().getMainGroup();
						if (null != mainGroup && !mainGroup.equals(Group.FREE_GROUP_NAME)) {
							customConfig.getConfig()[i] = null;
							customConfig.getStatus()[i] = null;
						}
					} else {
						customConfig.getConfig()[i] = null;
						customConfig.getStatus()[i] = null;
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error reading the configuration of guest user", t);
			throw new ApsSystemException("Error reading the configuration of guest user", t);
		}
		return customConfig;
	}

	protected Cookie getCookieGuestConfig(IPage page, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (null == cookies) return null;
		Cookie configCookie = null;
		String expectedCookieName = this.getCookieName(page);
		for (int i=0; i<cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (cookie.getName().equals(expectedCookieName)) {
				configCookie = cookie;
				break;
			}
		}
		return configCookie;
	}

	private String getCookieName(IPage page) {
		return "guestPageConfig_" + page.getCode();
	}

	@Override
	public Widget[] getShowletsToRender(IPage page, Widget[] customShowlets) throws ApsSystemException {
		Widget[] mergedWidgets = null;
		try {
			Widget[] defaultWidgets = page.getWidgets();
			if (null == customShowlets) {
				return defaultWidgets;
			}
			if (defaultWidgets.length != customShowlets.length) {
				String message = "Page '" + page.getCode() + "' Frame numbers " +
					defaultWidgets.length + " not equals than custom showlet frames " + customShowlets.length;
				ApsSystemUtils.getLogger().error(message);
				return defaultWidgets;
			}
			String modelCode = page.getModel().getCode();
			Map<Integer, MyPortalFrameConfig> config = this.getMyPortalPageModelManager().getPageModelConfig(modelCode);
			int widgetNumber = defaultWidgets.length;
			mergedWidgets = new Widget[widgetNumber];
			for (int scan = 0; scan < widgetNumber; scan++) {
				Widget customWidget = customShowlets[scan];
				MyPortalFrameConfig frameConfig = (null != config) ? config.get(scan) : null;
				if (null == customWidget || (null == frameConfig) || (frameConfig.isLocked())) {
					mergedWidgets[scan] = defaultWidgets[scan];
				} else {
					mergedWidgets[scan] = customWidget;
				}
			}
		} catch (Throwable t) {
			_logger.error("Error building the widget array to render", t);
			throw new ApsSystemException("Error building the showlet array to render", t);
		}
		return mergedWidgets;
	}

	/**
	 * @deprecated Use {@link #getCustomizableWidgets(UserDetails)} instead
	 */
	@Override
	public List<WidgetType> getCustomizableShowlets(UserDetails user) throws ApsSystemException {
		return getCustomizableWidgets(user);
	}

	@Override
	public List<WidgetType> getCustomizableWidgets(UserDetails user) throws ApsSystemException {
		List<WidgetType> customizableShowletsForUser = new ArrayList<WidgetType>();
		if (null == user) return customizableShowletsForUser;
		try {
			List<WidgetType> customizableShowlets = this.getMyPortalConfigManager().getCustomizableShowlets();
			for (int i = 0; i < customizableShowlets.size(); i++) {
				WidgetType type = customizableShowlets.get(i);
				String mainGroup = type.getMainGroup();
				if (null == mainGroup
						|| mainGroup.equals(Group.FREE_GROUP_NAME)
						|| this.getAuthorizationManager().isAuthOnGroup(user, mainGroup)) {
					customizableShowletsForUser.add(type);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error extracting customizable widgets user '{}'", user.getUsername(), t);
			throw new ApsSystemException("Error extracting customizable widgets user '" + user.getUsername() + "'", t);
		}
		return customizableShowletsForUser;
	}

	@Override
	public void updateGuestPageConfig(IPage page, WidgetUpdateInfoBean[] updateInfos, HttpServletRequest request, HttpServletResponse response) throws ApsSystemException {
		try {
			CustomPageConfig pageConfig = this.getGuestPageConfig(page, request);
			if (null == pageConfig) {
				pageConfig = new CustomPageConfig(page.getCode(), page.getModel().getFrames().length);
			}
			pageConfig.update(updateInfos);
			String cookieName = this.getCookieName(page);
			Cookie newCookie = pageConfig.toCookie(cookieName);
			response.addCookie(newCookie);
		} catch (Throwable t) {
			_logger.error("Error building Guest Page Config", t);
			throw new ApsSystemException("Error building Guest Page Config", t);
		}
	}

	@Override
	public void updateUserPageConfig(String username, IPage page, WidgetUpdateInfoBean[] updateInfos) throws ApsSystemException {
		try {
			this.getPageUserConfigDAO().updateUserPageConfig(username, page, updateInfos);
		} catch (Throwable t) {
			_logger.error("Error building Page Config for user {}", username, t);
			throw new ApsSystemException("Error building User Page Config", t);
		}
	}

	@Override
	public void removeGuestPageConfig(IPage page, HttpServletRequest request, HttpServletResponse response) throws ApsSystemException {
		Cookie cookie = new Cookie(this.getCookieName(page), "");
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	@Override
	public void removeUserPageConfig(String username, IPage page) throws ApsSystemException {
		try {
			this.getPageUserConfigDAO().removeUserPageConfig(username, page.getCode(), null);
		} catch (Throwable t) {
			_logger.error("Error removing Page Config for user {}", username, t);
			throw new ApsSystemException("Error removing Page user Config", t);
		}
	}

	@Before("execution(* com.agiletec.aps.system.services.page.IPageManager.deletePage(..)) && args(pageCode)")
	public void removeUserPageConfig(String pageCode) throws ApsSystemException {
		this.removeConfig(pageCode, null);
	}

	@Before("execution(* com.agiletec.aps.system.services.page.IPageManager.joinShowlet(..)) && args(pageCode, showlet, pos)")
	public void removeUserPageConfig(String pageCode, Widget showlet, int pos) throws ApsSystemException {
		this.removeConfig(pageCode, pos);
	}

	@Before("execution(* com.agiletec.aps.system.services.page.IPageManager.removeShowlet(..)) && args(pageCode, pos)")
	public void removeUserPageConfig(String pageCode, int pos) throws ApsSystemException {
		this.removeConfig(pageCode, pos);
	}

	private void removeConfig(String pageCode, Integer pos) throws ApsSystemException {
		try {
			this.getPageUserConfigDAO().removeUserPageConfig(null, pageCode, pos);
		} catch (Throwable t) {
			_logger.error("Error cleaning Page Config from page:{} frame:{}", pageCode, pageCode, t);
			throw new ApsSystemException("Error removing Page user Config", t);
		}
	}

	@Override
	public WidgetType getVoidShowlet() {
		return this.getWidgetTypeManager().getWidgetType(this.getVoidShowletCode());
	}

	protected String getVoidShowletCode() {
		return this.getMyPortalConfigManager().getVoidShowletCode();
	}

	protected IPageUserConfigDAO getPageUserConfigDAO() {
		return _pageUserConfigDAO;
	}
	public void setPageUserConfigDAO(IPageUserConfigDAO pageModelUserConfigDAO) {
		this._pageUserConfigDAO = pageModelUserConfigDAO;
	}

	protected IPageModelManager getPageModelManager() {
		return _pageModelManager;
	}
	public void setPageModelManager(IPageModelManager pageModelManager) {
		this._pageModelManager = pageModelManager;
	}

	protected IMyPortalConfigManager getMyPortalConfigManager() {
		return _myPortalConfigManager;
	}
	public void setMyPortalConfigManager(IMyPortalConfigManager myPortalConfigManager) {
		this._myPortalConfigManager = myPortalConfigManager;
	}

	protected IAuthorizationManager getAuthorizationManager() {
		return _authorizationManager;
	}
	public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
		this._authorizationManager = authorizationManager;
	}
	
	public IWidgetTypeManager getWidgetTypeManager() {
		return _widgetTypeManager;
	}
	public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
		this._widgetTypeManager = widgetTypeManager;
	}
	
	protected IMyPortalPageModelManager getMyPortalPageModelManager() {
		return _myPortalPageModelManager;
	}
	public void setMyPortalPageModelManager(IMyPortalPageModelManager myPortalPageModelManager) {
		this._myPortalPageModelManager = myPortalPageModelManager;
	}
	
	private IPageUserConfigDAO _pageUserConfigDAO;
	private IPageModelManager _pageModelManager;
	private IMyPortalConfigManager _myPortalConfigManager;
	private IWidgetTypeManager _widgetTypeManager;

	private IAuthorizationManager _authorizationManager;
	private IMyPortalPageModelManager _myPortalPageModelManager;

}