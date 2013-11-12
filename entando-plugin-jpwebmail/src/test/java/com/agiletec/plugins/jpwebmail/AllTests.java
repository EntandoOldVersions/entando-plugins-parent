/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando Enterprise Edition software.
* You can redistribute it and/or modify it
* under the terms of the Entando's EULA
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwebmail;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.TestWebMailManager;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.parse.TestWebMailConfigDOM;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for jpwebmail");
		
		suite.addTestSuite(TestWebMailConfigDOM.class);
		
		suite.addTestSuite(TestWebMailManager.class);
		
		return suite;
	}
	
}