/*
*
* Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwscontent;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.TestWsContentEndpoint;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for the jAPS WS Content Plugin");
		suite.addTestSuite(TestWsContentEndpoint.class);
		return suite;
	}
}
