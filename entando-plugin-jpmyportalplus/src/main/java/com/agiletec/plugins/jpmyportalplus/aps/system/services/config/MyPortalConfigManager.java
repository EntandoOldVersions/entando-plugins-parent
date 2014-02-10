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
package com.agiletec.plugins.jpmyportalplus.aps.system.services.config;

import java.util.List;

import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.aps.system.services.widgettype.events.WidgetTypeChangedEvent;
import org.entando.entando.aps.system.services.widgettype.events.WidgetTypeChangedObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jpmyportalplus.aps.system.JpmyportalplusSystemConstants;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.model.MyPortalConfig;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.parse.MyPortalPlusConfigDOM;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.IPageUserConfigDAO;

/**
 * @author E.Santoboni
 */
public class MyPortalConfigManager extends AbstractService implements IMyPortalConfigManager, WidgetTypeChangedObserver {

	private static final Logger _logger = LoggerFactory.getLogger(MyPortalConfigManager.class);
	
	@Override
	public void init() throws Exception {
		this.loadConfig();
		this.buildCustomizableShowletsSet();
		this.syncPageModelUserDatabase();
		_logger.debug("{} ready. the code of the'void widget is '{}'", this.getClass().getName(), _voidShowletCode);
	}
	
	@Override
	public void updateFromShowletTypeChanged(WidgetTypeChangedEvent event) {
		try {
			this.init();
		} catch (Throwable t) {
			_logger.error("Error on init after ShowletTypeChanged", t);
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
			_logger.error("error loading config", t);
			throw new ApsSystemException("error loading config", t);
		}
	}
	
	/**
	 * Get from the configuration the list of the showlets that can be customized in a page model.
	 */
	private void buildCustomizableShowletsSet() {
		List<WidgetType> showlets = this.getPageUserConfigDAO().buildCustomizableShowletsList(this.getConfig());
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
			_logger.error("Error saving myportal configuration", t);
			throw new ApsSystemException("Error saving myportal configuration", t);
		}
	}
	
	@Override
	public List<WidgetType> getCustomizableShowlets() {
		return _customizableShowlets;
	}
	public void setCustomizableShowlets(List<WidgetType> customizableShowlets) {
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
	
	private List<WidgetType> _customizableShowlets;
	
	private String _voidShowletCode;
	private ConfigInterface _configManager;
	private IPageUserConfigDAO _pageUserConfigDAO;
	
	private MyPortalConfig _config;
	
}