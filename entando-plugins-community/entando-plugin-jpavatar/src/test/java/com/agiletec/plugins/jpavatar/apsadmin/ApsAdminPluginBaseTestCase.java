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
package com.agiletec.plugins.jpavatar.apsadmin;

import com.agiletec.ConfigTestUtils;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.plugins.jpavatar.PluginConfigTestUtils;

/**
 * BaseTestCase Class for jpuserprofile apsadmin tests.
 * You have to comment "getConfigUtils()" 
 * Methods for testing plugin into custom jAPS 2.0 project.
 */
public class ApsAdminPluginBaseTestCase extends ApsAdminBaseTestCase {
	
	@Override
	protected ConfigTestUtils getConfigUtils() {
		return new PluginConfigTestUtils();
	}
	
}
