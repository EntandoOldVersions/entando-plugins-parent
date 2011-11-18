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
package com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.parse;

import java.util.Collection;
import java.util.Set;

import com.agiletec.plugins.jpmyportal.aps.ApsPluginBaseTestCase;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.model.MyPortalConfig;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.parse.MyPortalConfigDOM;

public class TestMyPortalConfigDOM extends ApsPluginBaseTestCase {
	
	public void testGetItems() throws ApsSystemException {
		MyPortalConfigDOM myPortalConfigDOM = new MyPortalConfigDOM(); 
		MyPortalConfig config = myPortalConfigDOM.extractConfig(XML);
		assertTrue(config.isAjaxEnabled());
		Set<String> showlets = config.getAllowedShowlets();
		this.compareCodes(new String[] { "content_viewer", "content_viewer_list", "login_form" }, showlets);
		
		String newXml = myPortalConfigDOM.createConfigXml(config);
		MyPortalConfig newConfig = myPortalConfigDOM.extractConfig(newXml);
		assertTrue(newConfig.isAjaxEnabled());
		showlets = newConfig.getAllowedShowlets();
		this.compareCodes(new String[] { "content_viewer", "content_viewer_list", "login_form" }, showlets);
		
	}
	
	private void compareCodes(String[] expected, Collection<String> received) {
		assertEquals(expected.length, received.size());
		for (String code : expected) {
			if (!received.contains(code)) {
				fail("Expected \"" + code + "\" not found");
			}
		}
	}
	
	private final String XML = 
		"<myportalConfig ajaxEnabled=\"true\" >" +
		"	<showlets>" +
		"		<showlet code=\"content_viewer_list\" />" +
		"		<showlet code=\"content_viewer\" />" +
		"		<showlet code=\"login_form\" />" +
		"	</showlets>" +
		"</myportalConfig>";
	
}