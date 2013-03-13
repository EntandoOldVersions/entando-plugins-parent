/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpmyportal;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.agiletec.plugins.jpmyportal.aps.internalservlet.myportal.TestMyPortalAction;
import com.agiletec.plugins.jpmyportal.aps.system.services.controller.control.TestMyPortalControllerService;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.TestPageModelUserConfigManager;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.parse.TestMyPortalConfigDOM;
import com.agiletec.plugins.jpmyportal.apsadmin.myportal.config.TestConfigAction;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for jAPS Plugin jpmyportal");
		
		suite.addTestSuite(TestMyPortalConfigDOM.class);
		suite.addTestSuite(TestPageModelUserConfigManager.class);
		
		suite.addTestSuite(TestMyPortalControllerService.class);
		
		suite.addTestSuite(TestMyPortalAction.class);
		suite.addTestSuite(TestConfigAction.class);
		
		return suite;
	}
	
}