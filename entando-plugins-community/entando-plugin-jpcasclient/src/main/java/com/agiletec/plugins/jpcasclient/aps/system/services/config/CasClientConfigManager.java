package com.agiletec.plugins.jpcasclient.aps.system.services.config;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jpcasclient.CasClientPluginSystemCostants;
import com.agiletec.plugins.jpcasclient.aps.system.services.config.parse.ConfigDOM;

public class CasClientConfigManager extends AbstractService implements ICasClientConfigManager {

	@Override
	public void init() throws Exception {
		String configItem = this.getConfigManager().getConfigItem(CasClientPluginSystemCostants.JPCASCLIENT_CONFIG_ITEM);
		ConfigDOM configDOM = new ConfigDOM();
		CasClientConfig casClientConfig = configDOM.extractConfig(configItem);
		this.setClientConfig(casClientConfig);
	}
	
	@Override
	protected void release() {
		this.setClientConfig(null);
	}
	
	@Override
	public void updateConfig(CasClientConfig config) throws ApsSystemException {
		ConfigDOM configDOM = new ConfigDOM();
		String configurationItem;
		try {
			configurationItem = configDOM.createConfigXml(config);
			this.getConfigManager().updateConfigItem(CasClientPluginSystemCostants.JPCASCLIENT_CONFIG_ITEM, configurationItem);
			this.setClientConfig(config);
		} catch (ApsSystemException t) {
			ApsSystemUtils.logThrowable(t, this, "updateConfig");
			throw new ApsSystemException("Errore in fase di aggiornamento configurazione mail", t);
		}
	}

	public ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}

	@Override
	public CasClientConfig getClientConfig() {
		return _clientConfig;
	}
	private void setClientConfig(CasClientConfig clientConfig) {
		this._clientConfig = clientConfig;
	}

	private ConfigInterface _configManager;
	private CasClientConfig _clientConfig;
	
}
