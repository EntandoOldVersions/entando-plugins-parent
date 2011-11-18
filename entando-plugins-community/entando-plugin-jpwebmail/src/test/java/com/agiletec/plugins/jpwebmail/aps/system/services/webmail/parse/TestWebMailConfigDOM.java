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
package com.agiletec.plugins.jpwebmail.aps.system.services.webmail.parse;

import com.agiletec.plugins.jpwebmail.aps.ApsPluginBaseTestCase;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.WebMailConfig;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.parse.WebMailConfigDOM;

public class TestWebMailConfigDOM extends ApsPluginBaseTestCase {
	
	public void testGetItems() throws ApsSystemException {
		WebMailConfigDOM configDOM = new WebMailConfigDOM();
		WebMailConfig bean = configDOM.extractConfig(XML);
		assertEquals("smtpUsername", bean.getSmtpUserName());
		assertEquals("SMTP.EMAIL.COM", bean.getSmtpHost());
		assertEquals("smtpPassword", bean.getSmtpPassword());
		assertEquals("/cert/path/", bean.getCertificatePath());
		assertTrue(bean.isCertificateLazyCheck());
		assertTrue(bean.isDebug());
	}
	
	private String XML = 
		"<webMailConfig>" +		
		"	<certificates>" +	
		"		<enable>true</enable>" +	
		"		<lazyCheck>true</lazyCheck>" +	
		"		<certPath>/cert/path/</certPath>" +	
		"		<debugOnConsole>true</debugOnConsole>" +	
		"	</certificates>" +	
		"	<smtp debug=\"true\" >" +
		"		<host>SMTP.EMAIL.COM</host>" +
		"		<user>smtpUsername</user>" +
		"		<password>smtpPassword</password>" +
		"	</smtp>" +
		"	<imap>" +
		"		<host>imap.gmail.com</host>" +
		"		<protocol>imaps</protocol>" +
		"		<port>993</port>" +
		"	</imap>" +
		"</webMailConfig>";
	
}