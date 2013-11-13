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
package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jpcontentfeedback.aps.system.JpcontentfeedbackSystemConstants;

public class ContentFeedbackManager extends AbstractService implements IContentFeedbackManager {

	@Override
	public void init() throws Exception {
		this.loadConfigs();
		ApsSystemUtils.getLogger().info(this.getClass().getName() + " ready");
	}

	private void loadConfigs() throws ApsSystemException {
		try {
			ConfigInterface configManager = this.getConfigManager();
			String xml = configManager.getConfigItem(JpcontentfeedbackSystemConstants.GLOBAL_CONFIG_ITEM);
			if (xml == null) {
				throw new ApsSystemException("Configuration item not present: " + JpcontentfeedbackSystemConstants.GLOBAL_CONFIG_ITEM);
			}
			this.setConfig(new ContentFeedbackConfig(xml));
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "loadConfigs");
			throw new ApsSystemException("Errore loading config", t);
		}
	}

	public void updateConfig(IContentFeedbackConfig config) throws ApsSystemException {
		try {
			String xml = config.toXML();
			this.getConfigManager().updateConfigItem(JpcontentfeedbackSystemConstants.GLOBAL_CONFIG_ITEM, xml);
			this.setConfig(config);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "updateConfig");
			throw new ApsSystemException("Error updating ContentFeedback config", t);
		}
	}

	@Override
	public boolean isCommentActive() {
		if (null == this.getConfig()) {
			ApsSystemUtils.getLogger().error("ContentFeedbackConfig is null");
			return false;
		}
		String value = this.getConfig().getComment();
		return null != value && value.equalsIgnoreCase("true");
	}

	@Override
	public boolean allowAnonymousComment() {
		if (null == this.getConfig()) {
			ApsSystemUtils.getLogger().error("ContentFeedbackConfig is null");
			return false;
		}
		if (!this.isCommentActive()) return false;
		String value = this.getConfig().getAnonymousComment();
		return null != value && value.equalsIgnoreCase("true");
	}
	
	@Override
	public boolean isCommentModerationActive() {
		if (null == this.getConfig()) {
			ApsSystemUtils.getLogger().error("ContentFeedbackConfig is null");
			return false;
		}
		String value = this.getConfig().getModeratedComment();
		return null != value && value.equalsIgnoreCase("true");
	}
	
	
	@Override
	public boolean isRateContentActive() {
		if (null == this.getConfig()) {
			ApsSystemUtils.getLogger().error("ContentFeedbackConfig is null");
			return false;
		}
		String value = this.getConfig().getRateContent();
		return null != value && value.equalsIgnoreCase("true");
	}

	@Override
	public boolean isRateCommentActive() {
		if (null == this.getConfig()) {
			ApsSystemUtils.getLogger().error("ContentFeedbackConfig is null");
			return false;
		}
		String value = this.getConfig().getRateComment();
		return isCommentActive() && null != value && value.equalsIgnoreCase("true");
	}
	

	public IContentFeedbackConfig getConfig() {
		return _config;
	}
	public void setConfig(IContentFeedbackConfig config) {
		this._config = config;
	}

	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}

	private IContentFeedbackConfig _config;
	private ConfigInterface _configManager;

}
