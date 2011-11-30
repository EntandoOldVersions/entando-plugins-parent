package com.agiletec.plugins.jpcasclient.aps.system.services.config;

import com.agiletec.aps.system.exception.ApsSystemException;

public interface ICasClientConfigManager {

	public CasClientConfig getClientConfig();

	public void updateConfig(CasClientConfig config) throws ApsSystemException;

}
