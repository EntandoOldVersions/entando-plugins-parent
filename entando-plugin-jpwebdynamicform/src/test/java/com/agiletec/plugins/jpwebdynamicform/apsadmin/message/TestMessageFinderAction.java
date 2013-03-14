/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwebdynamicform.apsadmin.message;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jpwebdynamicform.apsadmin.ApsAdminPluginBaseTestCase;

import com.agiletec.aps.util.DateConverter;
import com.agiletec.apsadmin.system.entity.IApsEntityFinderAction;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Answer;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Message;
import com.opensymphony.xwork2.Action;

public class TestMessageFinderAction extends ApsAdminPluginBaseTestCase {

	public void testList() throws Throwable {
		String username = "admin";
		String result = this.executeList(username, "PER");
		assertEquals(Action.SUCCESS, result);
		List<String> messageIds = ((IApsEntityFinderAction) this.getAction()).getSearchResult();
		assertEquals(0, messageIds.size());

		Date currentDate = new Date();
		Message message1 = this._helper.createMessage(null, "mainEditor", "it", currentDate, "MyName", "MySurname", "MyAddress", "MyEmail1@aaaaaaaaaaaa.ittt", "MyNotes");
		this._helper.addMessage(message1);
		Message message2 = this._helper.createMessage(null, "admin", "it", currentDate, "MyCompany2", "MyAddress2", "MyEmail2@aaaaaaaaaaaa.ittt", "MyNotes");
		this._helper.addMessage(message2);
		Message message3 = this._helper.createMessage(null, "mainEditor", "it", currentDate, "MyCompany3", "MyAddress3", "MyEmail3@aaaaaaaaaaaa.ittt", "MyNotes");
		this._helper.addMessage(message3);
		Message message4 = this._helper.createMessage(null, null, "it", currentDate, "MyCompany4", "MyAddress4", "MyEmail4@aaaaaaaaaaaa.ittt", "MyNotes");
		this._helper.addMessage(message4);

		result = this.executeList(username, "PER");
		assertEquals(Action.SUCCESS, result);
		messageIds = ((IApsEntityFinderAction) this.getAction()).getSearchResult();
		this.checkMessageIds(messageIds, new String[] { message1.getId() });

		result = this.executeList(username, "COM");
		assertEquals(Action.SUCCESS, result);
		messageIds = ((IApsEntityFinderAction) this.getAction()).getSearchResult();
		this.checkMessageIds(messageIds, new String[] { message2.getId(), message3.getId(), message4.getId() });
	}

	public void testSearch() throws Throwable {
		String username = "admin";
		Date today = new Date();
		Date yesterday = new Date(today.getTime()-86400000);
		Date tomorrow = new Date(today.getTime()+86400000);
		Message message1 = this._helper.createMessage(null, "mainEditor", "it", today, "MyName", "MySurname", "MyAddress", "MyEmail1@aaaaaaaaaaaa.ittt", "MyNotes");
		this._helper.addMessage(message1);
		Message message2 = this._helper.createMessage(null, "admin", "it", yesterday, "MyCompany2", "MyAddress2", "MyEmail2@aaaaaaaaaaaa.ittt", "MyNotes");
		this._helper.addMessage(message2);
		Message message3 = this._helper.createMessage(null, "mainEditor", "it", today, "MyCompany3", "MyAddress3", "MyEmail3@aaaaaaaaaaaa.ittt", "MyNotes");
		this._helper.addMessage(message3);
		Message message4 = this._helper.createMessage(null, null, "it", tomorrow, "MyCompany4", "MyAddress4", "MyEmail4@aaaaaaaaaaaa.ittt", "MyNotes");
		this._helper.addMessage(message4);
		Answer answer = this._helper.createAnswer("1", message4.getId(), "mainEditor", new Date(), "text1");
		this._helper.addAnswer(answer);

		Map<String, String> params = new HashMap<String, String>();
		params.put("entityTypeCode", "PER");
		String result = this.executeSearch(username, params);
		assertEquals(Action.SUCCESS, result);
		List<String> messageIds = ((IApsEntityFinderAction) this.getAction()).getSearchResult();
		this.checkMessageIds(messageIds, new String[] { message1.getId() });

		params.put("entityTypeCode", "COM");
		result = this.executeSearch(username, params);
		assertEquals(Action.SUCCESS, result);
		messageIds = ((IApsEntityFinderAction) this.getAction()).getSearchResult();
		this.checkMessageIds(messageIds, new String[] { message2.getId(), message3.getId(), message4.getId() });

		params.put("from", DateConverter.getFormattedDate(today, "dd/MM/yyyy"));
		result = this.executeSearch(username, params);
		assertEquals(Action.SUCCESS, result);
		messageIds = ((IApsEntityFinderAction) this.getAction()).getSearchResult();
		this.checkMessageIds(messageIds, new String[] { message3.getId(), message4.getId() });

		params.put("to", DateConverter.getFormattedDate(yesterday, "dd/MM/yyyy"));
		result = this.executeSearch(username, params);
		assertEquals(Action.SUCCESS, result);
		messageIds = ((IApsEntityFinderAction) this.getAction()).getSearchResult();
		this.checkMessageIds(messageIds, new String[] { });

		params.put("from", DateConverter.getFormattedDate(yesterday, "dd/MM/yyyy"));
		params.put("to", DateConverter.getFormattedDate(today, "dd/MM/yyyy"));
		result = this.executeSearch(username, params);
		assertEquals(Action.SUCCESS, result);
		messageIds = ((IApsEntityFinderAction) this.getAction()).getSearchResult();
		this.checkMessageIds(messageIds, new String[] { message2.getId(), message3.getId() });

		params.put("to", DateConverter.getFormattedDate(tomorrow, "dd/MM/yyyy"));
		result = this.executeSearch(username, params);
		assertEquals(Action.SUCCESS, result);
		messageIds = ((IApsEntityFinderAction) this.getAction()).getSearchResult();
		this.checkMessageIds(messageIds, new String[] { message2.getId(), message3.getId(), message4.getId() });

		params.put("answered", "1");
		result = this.executeSearch(username, params);
		assertEquals(Action.SUCCESS, result);
		messageIds = ((IApsEntityFinderAction) this.getAction()).getSearchResult();
		this.checkMessageIds(messageIds, new String[] { message4.getId() });

		params.put("answered", "0");
		result = this.executeSearch(username, params);
		assertEquals(Action.SUCCESS, result);
		messageIds = ((IApsEntityFinderAction) this.getAction()).getSearchResult();
		this.checkMessageIds(messageIds, new String[] { message2.getId(), message3.getId() });

		params.put("author", "mainEditor");
		result = this.executeSearch(username, params);
		assertEquals(Action.SUCCESS, result);
		messageIds = ((IApsEntityFinderAction) this.getAction()).getSearchResult();
		this.checkMessageIds(messageIds, new String[] { message3.getId() });
	}

	private void checkMessageIds(List<String> received, String[] expected) {
		assertEquals(expected.length, received.size());
		for (int i=0; i<expected.length; i++) {
			assertTrue(received.contains(expected[i]));
		}
	}

	private String executeList(String username, String typeCode) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpwebdynamicform/Message/Operator", "list");
		this.addParameter("entityTypeCode", typeCode);
		String result = this.executeAction();
		return result;
	}

	private String executeSearch(String username, Map<String, String> params) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpwebdynamicform/Message/Operator", "search");
		this.addParameters(params);
		String result = this.executeAction();
		return result;
	}

}