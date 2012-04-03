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

import java.util.Set;

/**
 * Contains the map of the configuration of jpmyportal. 
 * @author M. Minnai E. Santoboni
 */
public class MyPortalConfig {
	
	public boolean isAjaxEnabled() {
		return _ajaxEnabled;
	}
	public void setAjaxEnabled(boolean ajaxEnabled) {
		this._ajaxEnabled = ajaxEnabled;
	}
	
	public Set<String> getAllowedShowlets() {
		return _allowedShowlets;
	}
	public void setAllowedShowlets(Set<String> allowedShowlets) {
		this._allowedShowlets = allowedShowlets;
	}
	
	private boolean _ajaxEnabled;
	private Set<String> _allowedShowlets;
	
}