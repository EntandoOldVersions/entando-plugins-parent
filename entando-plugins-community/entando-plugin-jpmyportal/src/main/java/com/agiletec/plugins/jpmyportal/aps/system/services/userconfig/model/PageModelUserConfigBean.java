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
package com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.model;

import java.util.Map;

import com.agiletec.aps.system.services.page.Showlet;

public class PageModelUserConfigBean {
	
	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}
	
	public Map<String, Showlet[]> getConfig() {
		return _config;
	}
	public void setConfig(Map<String, Showlet[]> config) {
		this._config = config;
	}
	
	private String _username;
	private Map<String, Showlet[]> _config;
	
}