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
package com.agiletec.plugins.jpmyportalplus.aps.system.services.config;

import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.showlettype.ShowletType;
import com.agiletec.aps.system.services.showlettype.events.ShowletTypeChangedEvent;
import com.agiletec.aps.system.services.showlettype.events.ShowletTypeChangedObserver;
import com.agiletec.plugins.jpmyportalplus.aps.system.JpmyportalplusSystemConstants;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.model.MyPortalConfig;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.parse.MyPortalPlusConfigDOM;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.IPageUserConfigDAO;

/**
 * @author E.Santoboni
 */
public class MyPortalConfigManager extends AbstractService implements IMyPortalConfigManager, ShowletTypeChangedObserver {
	
	@Override
	public void init() throws Exception {
		this.loadConfig();
		this.buildCustomizableShowletsSet();
		this.syncPageModelUserDatabase();
		ApsSystemUtils.getLogger().config(this.getClass().getName() + ": initialized, the code of the'void showlet' is '"+_voidShowletCode+"'");
	}
	
	@Override
	public void updateFromShowletTypeChanged(ShowletTypeChangedEvent event) {
		try {
			this.init();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "updateFromShowletTypeChanged", "Error inittializating manager");
		}
	}
	
	private void loadConfig() throws ApsSystemException {
		try {
			String xml = this.getConfigManager().getConfigItem(JpmyportalplusSystemConstants.MYPORTALPLUS_CONFIG_ITEM);
			if (xml == null) {
				throw new ApsSystemException("Missing configuration item: "+ JpmyportalplusSystemConstants.MYPORTALPLUS_CONFIG_ITEM);
			}
			MyPortalPlusConfigDOM configDom = new MyPortalPlusConfigDOM();
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
		List<ShowletType> showlets = this.getPageUserConfigDAO().buildCustomizableShowletsList(this.getConfig());
		this.setCustomizableShowlets(showlets);
	}
	private void syncPageModelUserDatabase() {
		this.getPageUserConfigDAO().syncCustomization(this.getCustomizableShowlets(), this.getVoidShowletCode());
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
			MyPortalPlusConfigDOM configDom = new MyPortalPlusConfigDOM();
			String xml = configDom.createConfigXml(config);
			this.getConfigManager().updateConfigItem(JpmyportalplusSystemConstants.MYPORTALPLUS_CONFIG_ITEM, xml);
			this.setConfig(config);
			this.buildCustomizableShowletsSet();
			this.syncPageModelUserDatabase();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "saveConfig");
			throw new ApsSystemException("Error saving myportal configuration", t);
		}
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
	
	protected IPageUserConfigDAO getPageUserConfigDAO() {
		return _pageUserConfigDAO;
	}
	public void setPageUserConfigDAO(IPageUserConfigDAO pageModelUserConfigDAO) {
		this._pageUserConfigDAO = pageModelUserConfigDAO;
	}
	
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	
	private List<ShowletType> _customizableShowlets;
	
	private String _voidShowletCode;
	private ConfigInterface _configManager;
	private IPageUserConfigDAO _pageUserConfigDAO;
	
	private MyPortalConfig _config;
	
}