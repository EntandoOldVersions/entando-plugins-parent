/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
* Entando is a free software; 
* You can redistribute it and/or modify it
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
package com.agiletec.plugins.jpmail.apsadmin.mail;

import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jpmail.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jpmail.util.JpmailTestHelper;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jpmail.aps.services.JpmailSystemConstants;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.agiletec.plugins.jpmail.aps.services.mail.MailConfig;

import com.opensymphony.xwork2.Action;

public class TestMailSenderConfigAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
	public void testViewSenders() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jpmail/MailConfig", "viewSenders");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		MailSenderConfigAction action = (MailSenderConfigAction) this.getAction();
		MailConfig mailConfig = action.getConfig();
		this.checkOriginaryConfig(mailConfig);
	}
	
	public void testNewSender() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jpmail/MailConfig", "newSender");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		MailSenderConfigAction action = (MailSenderConfigAction) this.getAction();
		assertEquals(ApsAdminSystemConstants.ADD, action.getStrutsAction());
	}
	
	public void testEditSender() throws Throwable {
		String result = this.executeEditSender("admin", "CODE2");
		assertEquals(Action.SUCCESS, result);
		MailSenderConfigAction action = (MailSenderConfigAction) this.getAction();
		assertEquals(ApsAdminSystemConstants.EDIT, action.getStrutsAction());
		assertEquals("CODE2", action.getCode());
		assertEquals("EMAIL2@EMAIL.COM", action.getMail());
		
		result = this.executeEditSender("admin", "CODE100");
		assertEquals(Action.ERROR, result);
	}
	
	public void testSaveSenderFailure() throws Throwable {
		String currentUser = "admin";
		
		String result = this.executeSaveSender(currentUser, "CODE1", "", ApsAdminSystemConstants.ADD);
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(2, fieldErrors.size());
		List codeErrors = fieldErrors.get("code");
		assertEquals(1, codeErrors.size());
		assertTrue(((String) codeErrors.get(0)).contains(this.getAction().getText("error.config.sender.code.duplicated")));
		List mailErrors = fieldErrors.get("mail");
		assertEquals(1, mailErrors.size());
		assertTrue(((String) mailErrors.get(0)).contains(this.getAction().getText("requiredstring")));
		
		result = this.executeSaveSender(currentUser, "CODE2", "wrongMailAddress", ApsAdminSystemConstants.EDIT);
		assertEquals(Action.INPUT, result);
		fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		mailErrors = fieldErrors.get("mail");
		assertEquals(1, mailErrors.size());
		assertTrue(((String) mailErrors.get(0)).contains(this.getAction().getText("error.config.sender.mail.notValid")));
	}
	
	public void testSaveSenderSuccessful() throws Throwable {
		String currentUser = "admin";
		try {
			String result = this.executeSaveSender(currentUser, "CODE1", "\"Indirizzo di Prova\" <mail@addresstest.it>", ApsAdminSystemConstants.EDIT);
			assertEquals(Action.SUCCESS, result);
			MailConfig config = this._mailManager.getMailConfig();
			assertNotNull(config.getSender("CODE1"));
			assertEquals("\"Indirizzo di Prova\" <mail@addresstest.it>", config.getSender("CODE1"));
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.resetConfig();
		}
	}
	
	public void testTrashSender() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jpmail/MailConfig", "trashSender");
		this.addParameter("code", "CODE1");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		MailConfig config = this._mailManager.getMailConfig();
		assertNotNull(config.getSender("CODE1"));
		assertNotNull(config.getSender("CODE2"));
	}
	
	public void testDeleteSender() throws Throwable {
		try {
			this.setUserOnSession("admin");
			this.initAction("/do/jpmail/MailConfig", "deleteSender");
			this.addParameter("code", "CODE1");
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			MailConfig config = this._mailManager.getMailConfig();
			assertNull(config.getSender("CODE1"));
			assertNotNull(config.getSender("CODE2"));
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.resetConfig();
		}
	}
	
	private void checkOriginaryConfig(MailConfig mailConfig) {
		assertEquals(2, mailConfig.getSenders().size());
		assertEquals("EMAIL1@EMAIL.COM", mailConfig.getSender("CODE1"));
		assertEquals("EMAIL2@EMAIL.COM", mailConfig.getSender("CODE2"));
		assertEquals(true, mailConfig.isDebug());
		assertTrue(mailConfig.getSmtpHost().length()>0);
		assertNotNull(mailConfig.getSmtpUserName());
		assertNotNull(mailConfig.getSmtpPassword());
	}
	
	private String executeEditSender(String currentUser, String code) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/jpmail/MailConfig", "editSender");
		this.addParameter("code", code);
		String result = this.executeAction();
		return result;
	}
	
	private String executeSaveSender(String currentUser, String code, String mail, int strutsAction) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/jpmail/MailConfig", "saveSender");
		this.addParameter("code", code);
		this.addParameter("mail", mail);
		this.addParameter("strutsAction", String.valueOf(strutsAction));
		String result = this.executeAction();
		return result;
	}
	
    private void init() throws Exception {
    	try {
    		ConfigInterface configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
    		this._mailManager = (IMailManager) this.getService(JpmailSystemConstants.MAIL_MANAGER);
    		this._helper = new JpmailTestHelper(configManager, this._mailManager);
		} catch (Exception e) {
			throw e;
		}
    }
	
    private JpmailTestHelper _helper;
    private IMailManager _mailManager;
	
}