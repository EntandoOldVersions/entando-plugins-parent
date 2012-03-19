/*
 *
 * Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
 *
 * This file is part of Entando software.
 * jAPS is a free software; 
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
package com.agiletec.plugins.jpmyportal.apsadmin.myportal.config;

import java.util.Set;
import java.util.TreeSet;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.showlettype.IShowletTypeManager;
import com.agiletec.aps.system.services.showlettype.ShowletType;
import com.agiletec.apsadmin.portal.AbstractPortalAction;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.IPageModelUserConfigManager;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.model.MyPortalConfig;

/**
 * Classe contenente le azioni per la configurazione del servizio MyPortal (PageModelUserConfigManager).
 * @author E.Santoboni - E.Mezzano
 */
public class ConfigAction extends AbstractPortalAction implements IConfigAction {
	
	@Override
	public void validate() {
		super.validate();
		Set<String> showlets = this.getShowlets();
		IShowletTypeManager showletTypeManager = this.getShowletTypeManager();
		for (String showletCode : showlets) {
			if (!this.isShowletAllowed(showletTypeManager.getShowletType(showletCode))) {
				this.addFieldError("showlets", this.getText("errors.myportalConfig.showlets.notValid", new String[] { showletCode }));
			}
		}
	}
	
	@Override
	public String edit() {
		try {
			MyPortalConfig config = this.getPageModelUserConfigManager().getConfig();
			this.populateForm(config);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String addShowlet() {
		try {
			String showletCode = this.getShowletCode();
			if (showletCode != null && showletCode.length() > 0 && 
					this.isShowletAllowed(this.getShowletTypeManager().getShowletType(showletCode))) {
				this.getShowlets().add(showletCode);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String removeShowlet() {
		try {
			String showletCode = this.getShowletCode();
			if (showletCode != null) {
				this.getShowlets().remove(showletCode);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String save() {
		try {
			MyPortalConfig config = this.prepareConfig();
			this.getPageModelUserConfigManager().saveConfig(config);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private void populateForm(MyPortalConfig config) {
		this.setAjaxEnabled(new Boolean(config.isAjaxEnabled()));
		Set<String> allowedShowlets = config.getAllowedShowlets();
		IShowletTypeManager showletTypeManager = this.getShowletTypeManager();
		Set<String> showlets = new TreeSet<String>();
		for (String showletCode : allowedShowlets) {
			if (this.isShowletAllowed(showletTypeManager.getShowletType(showletCode))) {
				showlets.add(showletCode);
			}
		}
		this.setShowlets(showlets);
	}
	
	private MyPortalConfig prepareConfig() throws ApsSystemException {
		MyPortalConfig config = new MyPortalConfig();
		config.setAjaxEnabled(this.getAjaxEnabled()!=null && this.getAjaxEnabled().booleanValue());
		config.setAllowedShowlets(this.getShowlets());
		return config;
	}
	
	public ShowletType getShowletType(String showletCode) {
		return this.getShowletTypeManager().getShowletType(showletCode);
	}
	
	private boolean isShowletAllowed(ShowletType showletType) {
		return showletType!=null && (showletType.isLogic() || showletType.getTypeParameters()==null || showletType.getTypeParameters().isEmpty());
	}
	
	public Boolean getAjaxEnabled() {
		return _ajaxEnabled;
	}
	public void setAjaxEnabled(Boolean ajaxEnabled) {
		this._ajaxEnabled = ajaxEnabled;
	}
	
	public Set<String> getShowlets() {
		return _showlets;
	}
	public void setShowlets(Set<String> showlets) {
		this._showlets = showlets;
	}
	
	public String getShowletCode() {
		return showletCode;
	}
	public void setShowletCode(String showletCode) {
		this.showletCode = showletCode;
	}
	
	protected IPageModelUserConfigManager getPageModelUserConfigManager() {
		return _pageModelUserConfigManager;
	}
	public void setPageModelUserConfigManager(IPageModelUserConfigManager pageModelUserConfigManager) {
		this._pageModelUserConfigManager = pageModelUserConfigManager;
	}
	
	private Boolean _ajaxEnabled;
	private Set<String> _showlets = new TreeSet<String>();
	private String showletCode;
	
	private IPageModelUserConfigManager _pageModelUserConfigManager;
	
}