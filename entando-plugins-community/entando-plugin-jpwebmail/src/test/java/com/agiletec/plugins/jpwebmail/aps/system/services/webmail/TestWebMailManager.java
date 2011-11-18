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
package com.agiletec.plugins.jpwebmail.aps.system.services.webmail;

import com.agiletec.plugins.jpwebmail.aps.ApsPluginBaseTestCase;

import com.agiletec.plugins.jpwebmail.aps.system.JpwebmailSystemConstants;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.IWebMailManager;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.WebMailConfig;

public class TestWebMailManager extends ApsPluginBaseTestCase {
	
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
	public void testGetConfig() throws Throwable {
		WebMailConfig config = this._webMailManager.loadConfig();
		assertEquals("out.virgilio.it", config.getSmtpHost());
		assertEquals("", config.getSmtpUserName());
		assertEquals("", config.getSmtpPassword());
	}
	
    private void init() throws Exception {
    	try {
    		_webMailManager = (IWebMailManager) this.getService(JpwebmailSystemConstants.WEBMAIL_MANAGER);
		} catch (Exception e) {
			throw e;
		}
    }
	
	private IWebMailManager _webMailManager = null;
	
}