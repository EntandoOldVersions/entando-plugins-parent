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
package com.agiletec.plugins.jpmyportalplus.aps.system.services.config.model;

import java.util.Set;

/**
 * Contains the map of the configuration of jpmyportalplus. 
 * @author M. Minnai E. Santoboni
 */
public class MyPortalConfig {
	
	public Set<String> getAllowedShowlets() {
		return _allowedShowlets;
	}
	public void setAllowedShowlets(Set<String> allowedShowlets) {
		this._allowedShowlets = allowedShowlets;
	}
	
	private Set<String> _allowedShowlets;
	
}