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
package com.agiletec.plugins.jpmail.apsadmin.mail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jpmail.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jpmail.util.JpmailTestHelper;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jpmail.aps.services.JpmailSystemConstants;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.agiletec.plugins.jpmail.aps.services.mail.MailConfig;
import com.agiletec.plugins.jpmail.apsadmin.mail.SmtpConfigAction;
import com.opensymphony.xwork2.Action;

public class TestSmtpConfigAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
	/**
	 * Tests the 'edit' action.
	 * @throws Throwable
	 */
	public void testEdit() throws Throwable {
		MailConfig config = this._mailManager.getMailConfig();
		
		this.setUserOnSession("admin");
		this.initAction("/do/jpmail/MailConfig", "editSmtp");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		SmtpConfigAction action = (SmtpConfigAction) this.getAction();
		assertEquals(config.isDebug(), action.isDebug());
		assertEquals(config.getSmtpHost(), action.getSmtpHost());
		assertEquals(config.getSmtpUserName(), action.getSmtpUserName());
		assertEquals(config.getSmtpPassword(), action.getSmtpPassword());
	}
	
	/**
	 * Tests the 'save' action with not successful result.
	 * @throws Throwable
	 */
	public void testSaveFailure() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		String result = this.executeSave("admin", params);
		assertEquals(Action.INPUT, result);
		assertEquals(1, this.getAction().getFieldErrors().size());
		assertEquals(0, this.getAction().getActionErrors().size());
		
		params.put("debug", "true");
		params.put("smtpUserName", "username");
		params.put("smtpPassword", "password");
		result = this.executeSave("admin", params);
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertEquals(1, fieldErrors.get("smtpHost").size());
	}
	
	/**
	 * Tests the 'save' action with successful result.
	 * @throws Throwable
	 */
	public void testSaveSuccessful() throws Throwable {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("smtpHost", "host");
			params.put("smtpUserName", "username");
			params.put("smtpPassword", "password");
			String result = this.executeSave("admin", params);
			assertEquals(Action.SUCCESS, result);
			
			MailConfig config = this._mailManager.getMailConfig();
			assertEquals("host", config.getSmtpHost());
			assertNull(config.getSmtpPort());
			assertNull(config.getSmtpTimeout());
			assertEquals("username", config.getSmtpUserName());
			assertEquals("password", config.getSmtpPassword());
			
			params.put("smtpPort", "2525");
			params.put("smtpTimeout", "2000");
			result = this.executeSave("admin", params);
			assertEquals(Action.SUCCESS, result);
			config = this._mailManager.getMailConfig();
			assertEquals("host", config.getSmtpHost());
			assertEquals(new Integer(2525), config.getSmtpPort());
			assertEquals(new Integer(2000), config.getSmtpTimeout());
			assertEquals("username", config.getSmtpUserName());
			assertEquals("password", config.getSmtpPassword());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.resetConfig();
		}
	}
	
	protected String executeSave(String currentUser, Map<String, String> params) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/jpmail/MailConfig", "saveSmtp");
		this.addParameters(params);
		String result = this.executeAction();
		return result;
	}
	
    protected void init() throws Exception {
    	try {
    		ConfigInterface configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
    		this._mailManager = (IMailManager) this.getService(JpmailSystemConstants.MAIL_MANAGER);
    		this._helper = new JpmailTestHelper(configManager, this._mailManager);
		} catch (Exception e) {
			throw e;
		}
    }
	
	protected JpmailTestHelper _helper;
	protected IMailManager _mailManager;
	
}