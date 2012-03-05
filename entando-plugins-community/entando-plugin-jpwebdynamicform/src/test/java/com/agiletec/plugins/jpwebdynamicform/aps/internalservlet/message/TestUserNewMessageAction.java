/*
*
* Copyright 2010 AgileTec S.r.l. (http://www.agiletec.it) All rights reserved.
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
* Copyright 2010 AgileTec S.r.l. (http://www.agiletec.it) All rights reserved.
*
*/
package com.agiletec.plugins.jpwebdynamicform.aps.internalservlet.message;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jpwebdynamicform.apsadmin.ApsAdminPluginBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.plugins.jpmail.aps.services.JpmailSystemConstants;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.agiletec.plugins.jpmail.aps.services.mail.MailManager;
import com.agiletec.plugins.jpwebdynamicform.aps.internalservlet.message.UserNewMessageAction;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.IMessageSearcherDAO;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Message;
import com.opensymphony.xwork2.Action;

public class TestUserNewMessageAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.activeMailManager(false);
	}
	
	public void testCreateNew() throws Throwable {
		String result = this.executeNew("mainEditor", "");
		assertEquals("voidTypeCode", result);
		result = this.executeNew("mainEditor", "PER");
		assertEquals(Action.SUCCESS, result);
		UserNewMessageAction action = (UserNewMessageAction) this.getAction();
		Message message = action.getMessage();
		assertEquals("PER", message.getTypeCode());
	}

	public void testSend() throws Throwable {
		String result = this.executeNew(SystemConstants.GUEST_USER_NAME, "PER");
		assertEquals(Action.SUCCESS, result);
		Map<String, String> params = new HashMap<String, String>();
		result = this.executeSend(SystemConstants.GUEST_USER_NAME, params);
		assertEquals("expiredMessage", result);

		params.put("typeCode", "PER");
		result = this.executeSend(SystemConstants.GUEST_USER_NAME, params);
		assertEquals(Action.INPUT, result);
		UserNewMessageAction action = (UserNewMessageAction) this.getAction();
		Collection<String> fieldErrors = action.getFieldErrors().keySet();
		assertEquals(4, fieldErrors.size());
		assertTrue(fieldErrors.contains("Name"));
		assertTrue(fieldErrors.contains("Surname"));
		assertTrue(fieldErrors.contains("eMail"));
		assertTrue(fieldErrors.contains("Note"));

		params.put("Name", "MyName");
		params.put("Surname", "MySurname");
		params.put("Note", "MyNote");
		params.put("eMail", "MyEmail@inesistente.itte");
		result = this.executeSend(SystemConstants.GUEST_USER_NAME, params);
		assertEquals(Action.SUCCESS, result);
		List<String> messagesId = this._messageManager.loadMessagesId(null);
		assertEquals(1, messagesId.size());
		Message message = this._messageManager.getMessage(messagesId.get(0));
		assertEquals(SystemConstants.GUEST_USER_NAME, message.getUsername());

		params.put("eMail", "MyEmail");
		result = this.executeNew("mainEditor", "PER");
		assertEquals(Action.SUCCESS, result);
		result = this.executeSend(SystemConstants.GUEST_USER_NAME, params);
		assertEquals(Action.INPUT, result);
		fieldErrors = this.getAction().getFieldErrors().keySet();
		assertEquals(1, fieldErrors.size());
		assertTrue(fieldErrors.contains("eMail"));
		messagesId = this._messageManager.loadMessagesId(null);
		assertEquals(1, messagesId.size());

		result = this.executeNew("mainEditor", "PER");
		assertEquals(Action.SUCCESS, result);
		params.put("eMail", "MyEmail@inesistente.itte");
		result = this.executeSend("mainEditor", params);
		assertEquals(Action.SUCCESS, result);

		EntitySearchFilter[] filters = new EntitySearchFilter[] { new EntitySearchFilter(IMessageSearcherDAO.USERNAME_FILTER_KEY, false, "mainEditor", false)};
		messagesId = this._messageManager.loadMessagesId(filters);
		assertEquals(1, messagesId.size());
		message = this._messageManager.getMessage(messagesId.get(0));
		assertEquals("mainEditor", message.getUsername());
	}

	private String executeNew(String username, String typeCode) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpwebdynamicform/Message/User", "new");
		this.addParameter("typeCode", typeCode);
		String result = this.executeAction();
		return result;
	}

	private String executeSend(String username, Map<String, String> params) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpwebdynamicform/Message/User", "send");
		this.addParameters(params);
		String result = this.executeAction();
		return result;
	}
	
	@Override
	protected void tearDown() throws Exception {
		this.activeMailManager(true);
		super.tearDown();
	}
	
	private void activeMailManager(boolean active) {
		IMailManager mailManager = (IMailManager) this.getService(JpmailSystemConstants.MAIL_MANAGER);
		if (mailManager instanceof MailManager) {
			((MailManager) mailManager).setActive(active);
		}
	}
	
}