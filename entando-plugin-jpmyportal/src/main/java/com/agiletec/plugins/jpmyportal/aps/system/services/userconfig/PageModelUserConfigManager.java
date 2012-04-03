/*
 *
 * Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
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
 * Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
 *
 */
package com.agiletec.plugins.jpmyportal.aps.system.services.userconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.page.Showlet;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.showlettype.IShowletTypeManager;
import com.agiletec.aps.system.services.showlettype.ShowletType;
import com.agiletec.plugins.jpmyportal.aps.system.JpmyportalSystemConstants;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.model.MyPortalConfig;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.model.PageModelUserConfigBean;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.parse.MyPortalConfigDOM;

public class PageModelUserConfigManager extends AbstractService implements IPageModelUserConfigManager {
	
	@Override
	public void init() throws Exception {
		this.loadConfig();
		this.buildCustomizableShowletsSet();
		this.syncPageModelUserDatabase();
		ApsSystemUtils.getLogger().config(this.getClass().getName() + ": initialized, the code of the'void showlet' is '"+_voidShowletCode+"'");
	}
	
	private void loadConfig() throws ApsSystemException {
		try {
			String xml = this.getConfigManager().getConfigItem(JpmyportalSystemConstants.MYPORTAL_CONFIG_ITEM);
			if (xml == null) {
				throw new ApsSystemException("Missing configuration item: "+ JpmyportalSystemConstants.MYPORTAL_CONFIG_ITEM);
			}
			MyPortalConfigDOM configDom = new MyPortalConfigDOM();
			this.setConfig(configDom.extractConfig(xml));
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "loadConfig");
			throw new ApsSystemException("Error during initialization", t);
		}
	}
	
	/**
	 * Get from the configuration the list of the showlets that can be customized in a page model.
	 */
	private void buildCustomizableShowletsSet() {
		List<ShowletType> showlets = new ArrayList<ShowletType>();
		this.setCustomizableShowlets(showlets);
		if (null == this.getConfig()) return;
		List<ShowletType> allShowlets = this.getShowletTypeManager().getShowletTypes();
		if (null != allShowlets) {
			Set<String> allowedShowletsCode = this.getConfig().getAllowedShowlets();
			if (allowedShowletsCode != null && allowedShowletsCode.size() > 0) {
				for (ShowletType currentType : allShowlets) {
					if (allowedShowletsCode.contains(currentType.getCode())) {
						showlets.add(currentType);
					}
				}
			}
		}
	}
	
	private void syncPageModelUserDatabase() {
		this.getPageModelUserConfigDAO().syncCustomization(getCustomizableShowlets(), this.getVoidShowletCode());
	}
	
	@Override
	public MyPortalConfig getConfig() {
		return _config;
	}
	protected void setConfig(MyPortalConfig config) {
		this._config = config;
	}
	
	@Override
	public void saveConfig(MyPortalConfig config) throws ApsSystemException {
		try {
			MyPortalConfigDOM configDom = new MyPortalConfigDOM();
			String xml = configDom.createConfigXml(config);
			this.getConfigManager().updateConfigItem(JpmyportalSystemConstants.MYPORTAL_CONFIG_ITEM, xml);
			this.setConfig(config);
			this.buildCustomizableShowletsSet();
			this.syncPageModelUserDatabase();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "saveConfig");
			throw new ApsSystemException("Error saving myportal configuration", t);
		}
	}
	
	/**
	 * Create an object sometimes called 'customization bean', that will contain all the customizations of 
	 * the page model(s) of the given user. If necessary it creates an empty map for the given page model. 
	 * @param username The username associated to the configuration
	 * @param pageModelCode the current page model
	 * @throws ApsSystemException in case of error
	 */
	private PageModelUserConfigBean adjustPageModelUserConfigurationBean(PageModelUserConfigBean customization, String username, String pageModelCode) throws ApsSystemException {
		Map<String, Showlet[]> map = null;
		if (null == username || null == pageModelCode) {
			throw new ApsSystemException("Error creating the customization bean");
		}
		if (null != customization && customization.getConfig().containsKey(pageModelCode)) {
			return customization;
		} else {
			if (null == customization) {
				customization = new PageModelUserConfigBean();
				customization.setUsername(username);
				map = new HashMap<String, Showlet[]>(); 
			} else {
				map = customization.getConfig();
			}
		}
		int frames = this.getPageModelManager().getPageModel(pageModelCode).getFrames().length;
		Showlet[] showlets = new Showlet[frames];
		map.put(pageModelCode, showlets);
		customization.setConfig(map);
		return customization;
	}
	
	@Override
	public PageModelUserConfigBean getUserConfig(String username) throws ApsSystemException {
		PageModelUserConfigBean pageModelUserBean = null;
		if (username == null) {
			return null;
		}
		try {
			if (!username.equals(SystemConstants.ADMIN_USER_NAME) && !username.equals(SystemConstants.GUEST_USER_NAME)) {
				pageModelUserBean = this.getPageModelUserConfigDAO().getUserConfig(username);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getUserConfig");
			throw new ApsSystemException("Error reading the user configuration", t);
		}
		return pageModelUserBean;
	}
	
	@Override
	public void removeUserConfig(String username, String pageModelCode,	int framepos) 
		throws ApsSystemException {
		try {
			this.getPageModelUserConfigDAO().removeUserConfig(username, pageModelCode, framepos);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeUserConfig");
			throw new ApsSystemException("Error removing user configuration", t);
		}
	}
	
	@Override
	public void saveUserConfig(String username, String pageModelCode, int framepos, Showlet showlet) throws ApsSystemException {
		try {
			this.getPageModelUserConfigDAO().saveUserConfig(username, pageModelCode, framepos, showlet);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "saveUserConfig");
			throw new ApsSystemException("Error saving user configuration", t);
		}
	}
	
	@Override
	public void updateUserConfig(String username, String pageModelCode,	int framepos, Showlet showlet) throws ApsSystemException {
		try {
			this.getPageModelUserConfigDAO().updateUserConfig(username, pageModelCode, framepos, showlet);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "updateUserConfig");
			throw new ApsSystemException("Error updating user configuration", t);
		}
	}
	
	@Override
	public PageModelUserConfigBean swapShowlets(PageModelUserConfigBean customization, String username, 
			String pageModelCode, int firstFrame, int secondFrame, Showlet[] showlets) throws ApsSystemException {
		try {
			customization = this.adjustPageModelUserConfigurationBean(customization, username, pageModelCode);
			if (!customization.getUsername().equals(username)) {
				throw new ApsSystemException("ERROR: user mismatch detected; the current user is not the owner of the configuration bean");
			}
			return this.getPageModelUserConfigDAO().swapShowlets(customization, pageModelCode, firstFrame, secondFrame, showlets);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "swapShowlets");
			throw new ApsSystemException("Error while swapping showlets", t);
		}
	}
	
	@Override
	public PageModelUserConfigBean voidFrame(PageModelUserConfigBean customization, String username, 
				String pageModelCode, int targetFrame) throws ApsSystemException {
		try {
			customization = this.adjustPageModelUserConfigurationBean(customization, username, pageModelCode);
			if (!customization.getUsername().equals(username)) {
				throw new ApsSystemException("ERROR: user mismatch detected; the current user is not the owner of the configuration bean");
			}
			return this.getPageModelUserConfigDAO().voidFrame(customization, pageModelCode, targetFrame, this.getVoidShowletCode());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "voidFrame");
			throw new ApsSystemException("Error voiding the frame", t);
		}
	}
	
	@Override
	public PageModelUserConfigBean assignShowletToFrame(PageModelUserConfigBean customization, String username, 
			String pageModelCode, int targetFrame, Showlet showlet) throws ApsSystemException {
		try {
			customization = this.adjustPageModelUserConfigurationBean(customization, username, pageModelCode);
			if (!customization.getUsername().equals(username)) {
				throw new ApsSystemException("ERROR: user mismatch detected; the current user is not the owner of the configuration bean");
			}
			customization = this.getPageModelUserConfigDAO().assignShowletToFrame(customization, pageModelCode, targetFrame, showlet);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "assignShowletToFrame");
			throw new ApsSystemException("Errore detected while assigning a showlet to a frame", t);
		}
		return customization;
	}
	
	@Override
	public List<ShowletType> getCustomizableShowlets() {
		return _customizableShowlets;
	}
	public void setCustomizableShowlets(List<ShowletType> customizableShowlets) {
		this._customizableShowlets = customizableShowlets;
	}
	
	@Override
	public String getVoidShowletCode() {
		return _voidShowletCode;
	}
	public void setVoidShowletCode(String voidShowletCode) {
		this._voidShowletCode = voidShowletCode;
	}
	
	protected IPageModelUserConfigDAO getPageModelUserConfigDAO() {
		return _pageModelUserConfigDAO;
	}
	public void setPageModelUserConfigDAO(IPageModelUserConfigDAO pageModelUserConfigDAO) {
		this._pageModelUserConfigDAO = pageModelUserConfigDAO;
	}
	
	protected IPageModelManager getPageModelManager() {
		return _pageModelManager;
	}
	public void setPageModelManager(IPageModelManager pageModelManager) {
		this._pageModelManager = pageModelManager;
	}
	
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	
	protected IShowletTypeManager getShowletTypeManager() {
		return _showletTypeManager;
	}
	public void setShowletTypeManager(IShowletTypeManager showletTypeManager) {
		this._showletTypeManager = showletTypeManager;
	}
	
	private List<ShowletType> _customizableShowlets;
	
	private String _voidShowletCode;
	private ConfigInterface _configManager;
	private IPageModelUserConfigDAO _pageModelUserConfigDAO;
	private IPageModelManager _pageModelManager;
	private IShowletTypeManager _showletTypeManager;
	
	private MyPortalConfig _config;
	
}