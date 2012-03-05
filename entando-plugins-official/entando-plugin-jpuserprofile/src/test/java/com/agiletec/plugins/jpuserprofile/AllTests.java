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
package com.agiletec.plugins.jpuserprofile;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.agiletec.plugins.jpuserprofile.aps.internalservlet.common.TestFrontCommonAction;
import com.agiletec.plugins.jpuserprofile.aps.internalservlet.common.TestFrontCurrentUserProfileAction;
import com.agiletec.plugins.jpuserprofile.aps.system.services.TestUserManager;
import com.agiletec.plugins.jpuserprofile.aps.system.services.TestUserProfileManager;
import com.agiletec.plugins.jpuserprofile.apsadmin.common.TestCurrentUserProfileAction;
import com.agiletec.plugins.jpuserprofile.apsadmin.profile.TestUserProfileFinderAction;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for jAPS Profile Plugin");
		
		suite.addTestSuite(TestFrontCommonAction.class);
		suite.addTestSuite(TestFrontCurrentUserProfileAction.class);

		suite.addTestSuite(TestUserProfileManager.class);
		suite.addTestSuite(TestUserManager.class);
		
		suite.addTestSuite(TestCurrentUserProfileAction.class);
		suite.addTestSuite(TestUserProfileFinderAction.class);
		
		return suite;
	}
	
}
