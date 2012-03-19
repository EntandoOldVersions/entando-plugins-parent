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
package com.agiletec.plugins.jpmyportalplus.apsadmin.config;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.showlettype.IShowletTypeManager;
import com.agiletec.aps.system.services.showlettype.ShowletType;
import com.agiletec.aps.system.services.showlettype.ShowletTypeParameter;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.portal.AbstractPortalAction;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.IMyPortalConfigManager;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.model.MyPortalConfig;

/**
 * @author E.Santoboni
 */
public class ConfigAction extends AbstractPortalAction implements IConfigAction {
	
	@Override
	public void validate() {
		super.validate();
		Set<String> showletTypeCodes = this.getShowletTypeCodes();
		IShowletTypeManager showletTypeManager = this.getShowletTypeManager();
		for (String showletCode : showletTypeCodes) {
			if (!this.isShowletAllowed(showletTypeManager.getShowletType(showletCode))) {
				this.addFieldError("showlets", this.getText("Errors.jpmyportalConfig.showlets.notValid", new String[] { showletCode }));
			}
		}
	}
	
	@Override
	public String edit() {
		try {
			MyPortalConfig config = this.getMyPortalConfigManager().getConfig();
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
			ShowletType type = this.getShowletTypeManager().getShowletType(showletCode);
			if (this.isShowletAllowed(type)) {
				this.getShowletTypeCodes().add(showletCode);
			} else {
				this.addFieldError("showletCode", this.getText("Errors.jpmyportalConfig.showletType.notValid", new String[] { showletCode }));
				return INPUT;
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
				this.getShowletTypeCodes().remove(showletCode);
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
			this.getMyPortalConfigManager().saveConfig(config);
			this.addActionMessage(this.getText("jpmyportalplus.message.configSavedOk"));
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private void populateForm(MyPortalConfig config) {
		Set<String> allowedShowlets = config.getAllowedShowlets();
		IShowletTypeManager showletTypeManager = this.getShowletTypeManager();
		Set<String> showlets = new TreeSet<String>();
		for (String showletCode : allowedShowlets) {
			if (this.isShowletAllowed(showletTypeManager.getShowletType(showletCode))) {
				showlets.add(showletCode);
			}
		}
		this.setShowletTypeCodes(showlets);
	}
	
	@Override
	protected void addFlavourShowletType(String mapCode, ShowletType type, Map<String, List<SelectItem>> mapping) {
		if (null == type) return;
		List<ShowletTypeParameter> typeParameters = type.getTypeParameters();
		if (!type.isUserType() && !type.isLogic() && (null != typeParameters && typeParameters.size() > 0)) return;
		if (type.getCode().equals(this.getMyPortalConfigManager().getVoidShowletCode())) return;
		super.addFlavourShowletType(mapCode, type, mapping);
	}
	
	private MyPortalConfig prepareConfig() throws ApsSystemException {
		MyPortalConfig config = new MyPortalConfig();
		config.setAllowedShowlets(this.getShowletTypeCodes());
		return config;
	}
	
	public ShowletType getShowletType(String showletCode) {
		return this.getShowletTypeManager().getShowletType(showletCode);
	}
	
	private boolean isShowletAllowed(ShowletType showletType) {
		return showletType != null && 
				(showletType.isLogic() || showletType.isUserType() || showletType.getTypeParameters()==null || showletType.getTypeParameters().isEmpty());
	}
	
	public Set<String> getShowletTypeCodes() {
		return _showletTypeCodes;
	}
	public void setShowletTypeCodes(Set<String> showletTypeCodes) {
		this._showletTypeCodes = showletTypeCodes;
	}
	
	public String getShowletCode() {
		return _showletCode;
	}
	public void setShowletCode(String showletCode) {
		this._showletCode = showletCode;
	}
	
	protected IMyPortalConfigManager getMyPortalConfigManager() {
		return _myPortalConfigManager;
	}
	public void setMyPortalConfigManager(IMyPortalConfigManager myPortalConfigManager) {
		this._myPortalConfigManager = myPortalConfigManager;
	}
	
	private Set<String> _showletTypeCodes = new TreeSet<String>();
	
	private String _showletCode;
	
	private IMyPortalConfigManager _myPortalConfigManager;
	
}