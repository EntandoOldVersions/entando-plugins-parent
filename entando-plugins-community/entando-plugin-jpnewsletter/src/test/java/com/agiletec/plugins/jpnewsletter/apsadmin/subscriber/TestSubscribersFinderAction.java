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
package com.agiletec.plugins.jpnewsletter.apsadmin.subscriber;

import com.agiletec.plugins.jpnewsletter.apsadmin.ApsAdminPluginBaseTestCase;

import com.opensymphony.xwork2.Action;

public class TestSubscribersFinderAction extends ApsAdminPluginBaseTestCase {
	
	public void testGetSubscribers() throws Throwable{
		try {
			this.setUserOnSession("admin");
			this.initAction("/do/jpnewsletter/Subscriber", "list");
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
		} catch (Exception t) {
			throw t;
		}
	}
	
}