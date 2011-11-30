package com.agiletec.plugins.jpcasclient.apsadmin.config;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpcasclient.aps.system.services.config.CasClientConfig;
import com.agiletec.plugins.jpcasclient.aps.system.services.config.ICasClientConfigManager;

public class ConfigAction extends BaseAction implements IConfigAction {
	
	@Override
	public String edit() {
		try {
			CasClientConfig config = this.getCasClientConfigManager().getClientConfig();
			this.setConfig(config);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String save() {
		try {
			this.getCasClientConfigManager().updateConfig(this.getConfig());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	

	public CasClientConfig getConfig() {
		return _config;
	}
	public void setConfig(CasClientConfig config) {
		this._config = config;
	}

	public ICasClientConfigManager getCasClientConfigManager() {
		return _casClientConfigManager;
	}
	public void setCasClientConfigManager(ICasClientConfigManager casClientConfigManager) {
		this._casClientConfigManager = casClientConfigManager;
	}

	private CasClientConfig _config;
	private ICasClientConfigManager _casClientConfigManager;

}