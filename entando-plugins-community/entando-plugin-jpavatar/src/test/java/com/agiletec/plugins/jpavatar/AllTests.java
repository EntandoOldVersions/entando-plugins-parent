/*
*
* Copyright 2009 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
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
* Copyright 2009 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
*/
package com.agiletec.plugins.jpavatar;

import com.agiletec.plugins.jpavatar.aps.system.services.avatar.TestAvatarManager;
import com.agiletec.plugins.jpavatar.apsadmin.avatar.TestAvatarAction;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite(  "Test for jpavatar");
		suite.addTestSuite(TestAvatarManager.class);
		suite.addTestSuite(TestAvatarAction.class);
		return suite;
	}
	
}
