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
package com.agiletec.plugins.jpactionlogger;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.agiletec.plugins.jpactionlogger.aps.system.services.actionlogger.TestActionLoggerDAO;
import com.agiletec.plugins.jpactionlogger.aps.system.services.actionlogger.TestActionLoggerManager;
import com.agiletec.plugins.jpactionlogger.apsadmin.actionlogger.TestActionLoggerAction;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for jpactionlogger");
		
		suite.addTestSuite(TestActionLoggerDAO.class);
		suite.addTestSuite(TestActionLoggerManager.class);
		
		suite.addTestSuite(TestActionLoggerAction.class);
		
		return suite;
	}
	
}