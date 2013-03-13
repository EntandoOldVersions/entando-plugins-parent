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
package com.agiletec.plugins.jpactionlogger.apsadmin.actionlogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jpactionlogger.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jpactionlogger.util.JpactionloggerTestHelper;

import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jpactionlogger.aps.system.JpactionloggerSystemConstants;
import com.agiletec.plugins.jpactionlogger.aps.system.services.actionlogger.IActionLoggerManager;
import com.agiletec.plugins.jpactionlogger.aps.system.services.actionlogger.model.ActionRecord;
import com.agiletec.plugins.jpactionlogger.apsadmin.actionlogger.IActionLoggerAction;
import com.opensymphony.xwork2.Action;

public class TestActionLoggerAction extends ApsAdminPluginBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
		this._helper.cleanRecords();
	}

	public void testList() throws Throwable {
		this.executeDummyAction("admin", null);
		String result = this.executeList("admin");
		assertEquals(Action.SUCCESS, result);
		List<Integer> ids = ((IActionLoggerAction) this.getAction()).getActionRecords();
		assertEquals(1, ids.size());
		ActionRecord record = this._actionLoggerManager.getActionRecord(ids.get(0).intValue());

		assertEquals("admin", record.getUsername());
		assertEquals("ping", record.getActionName());
		assertEquals("/do/jpactionlogger/Test", record.getNamespace());
		assertEquals("", record.getParams());
	}

	public void testSearch() throws Throwable {
		ActionRecord record1 = this._helper.createActionRecord(1, "username1", "actionName1", 
				"namespace1", DateConverter.parseDate("01/01/2009 00:00", "dd/MM/yyyy HH:mm"), "params1");
		ActionRecord record2 = this._helper.createActionRecord(2, "username2", "actionName2", 
				"namespace2", DateConverter.parseDate("02/01/2009 10:00", "dd/MM/yyyy HH:mm"), "params2");
		ActionRecord record3 = this._helper.createActionRecord(3, "username123", "actionName123", 
				"namespace123", DateConverter.parseDate("03/01/2009 12:00", "dd/MM/yyyy HH:mm"), "params123");
		this._helper.addActionRecord(record1);
		this._helper.addActionRecord(record2);
		this._helper.addActionRecord(record3);

		Map<String, String> params = this.createSearchParams("name", "Name", "space", "arams", null, null);
		String result = this.executeSearch("admin", params);
		assertEquals(Action.SUCCESS, result);
		List<Integer> ids = ((IActionLoggerAction) this.getAction()).getActionRecords();
		this.compareIds(new Integer [] { 1, 2, 3 }, ids);

		params = this.createSearchParams("name", "Name", "space", "arams", "03/01/2009", null);
		result = this.executeSearch("admin", params);
		assertEquals(Action.SUCCESS, result);
		ids = ((IActionLoggerAction) this.getAction()).getActionRecords();
		this.compareIds(new Integer [] { 3 }, ids);

		params = this.createSearchParams(null, null, null, null, null, "02/01/2009");
		result = this.executeSearch("admin", params);
		assertEquals(Action.SUCCESS, result);
		ids = ((IActionLoggerAction) this.getAction()).getActionRecords();
		this.compareIds(new Integer [] { 1, 2 }, ids);

		params = this.createSearchParams(null, "Name", null, null, "02/01/2009", "02/01/2009");
		result = this.executeSearch("admin", params);
		assertEquals(Action.SUCCESS, result);
		ids = ((IActionLoggerAction) this.getAction()).getActionRecords();
		this.compareIds(new Integer [] { 2 }, ids);

		params = this.createSearchParams("name 1 2 3", "Name 1 2 3", "space 1 2 3", "arams 1 2 3", "01/01/2009", "03/01/2009");
		result = this.executeSearch("admin", params);
		assertEquals(Action.SUCCESS, result);
		ids = ((IActionLoggerAction) this.getAction()).getActionRecords();
		this.compareIds(new Integer [] { 3 }, ids);
	}

	public void testSearch_2() throws Throwable {
		Map<String, String> searchParams = this.createSearchParams("usernamePARAM", "actionNamePARAM", "namespacePARAM", "paramsPARAM", "01/01/2009", "03/01/2009");
		//the param password must be not logged. See xml configuration
		searchParams.put("password", "password");
		String result = this.executeSearch("admin", searchParams);
		assertEquals(Action.SUCCESS, result);
		List<Integer> ids = ((IActionLoggerAction) this.getAction()).getActionRecords();
		this.compareIds(new Integer [] {}, ids);

		this.executeDummyAction("admin", searchParams);
		ids = this._actionLoggerManager.getActionRecords(null);
		assertEquals(1, ids.size());
		ActionRecord record = this._actionLoggerManager.getActionRecord(ids.get(0).intValue());

		assertEquals("admin", record.getUsername());
		assertEquals("ping", record.getActionName());
		assertEquals("/do/jpactionlogger/Test", record.getNamespace());
		String actionParams = record.getParams();
		assertTrue(actionParams.contains("username=usernamePARAM"));
		assertTrue(actionParams.contains("actionName=actionNamePARAM"));
		assertTrue(actionParams.contains("namespace=namespacePARAM"));
		assertTrue(actionParams.contains("params=paramsPARAM"));
		assertTrue(actionParams.contains("start=01/01/2009"));
		assertTrue(actionParams.contains("end=03/01/2009"));
		assertFalse(actionParams.contains("password"));
	}

	public void testDelete() throws Throwable {
		ActionRecord record1 = this._helper.createActionRecord(1, "username1", "actionName1", 
				"namespace1", DateConverter.parseDate("01/01/2009 00:00", "dd/MM/yyyy HH:mm"), "params1");
		ActionRecord record2 = this._helper.createActionRecord(2, "username2", "actionName2", 
				"namespace2", DateConverter.parseDate("02/01/2009 10:00", "dd/MM/yyyy HH:mm"), "params2");
		ActionRecord record3 = this._helper.createActionRecord(3, "username123", "actionName123", 
				"namespace123", DateConverter.parseDate("03/01/2009 12:00", "dd/MM/yyyy HH:mm"), "params123");
		this._helper.addActionRecord(record1);
		this._helper.addActionRecord(record2);
		this._helper.addActionRecord(record3);
		assertNotNull(this._actionLoggerManager.getActionRecord(record1.getId()));
		assertNotNull(this._actionLoggerManager.getActionRecord(record2.getId()));
		assertNotNull(this._actionLoggerManager.getActionRecord(record3.getId()));

		String result = this.executeDelete("admin", record1.getId());
		assertEquals(Action.SUCCESS, result);
		assertNull(this._actionLoggerManager.getActionRecord(record1.getId()));

		result = this.executeDelete("admin", record2.getId());
		assertEquals(Action.SUCCESS, result);
		assertNull(this._actionLoggerManager.getActionRecord(record2.getId()));

		result = this.executeDelete("admin", record3.getId());
		assertEquals(Action.SUCCESS, result);
		assertNull(this._actionLoggerManager.getActionRecord(record3.getId()));
	}

	private String executeList(String username) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpactionlogger/ActionLogger", "list");
		String result = this.executeAction();
		return result;
	}

	private String executeDummyAction(String username, Map<String, String> params) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpactionlogger/Test", "ping");
		if (null != params) {
			this.addParameters(params);
		}
		String result = this.executeAction();
		return result;
	}

	private String executeSearch(String username, Map<String, String> params) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpactionlogger/ActionLogger", "search");
		this.addParameters(params);
		String result = this.executeAction();
		return result;
	}

	private String executeDelete(String username, int id) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpactionlogger/ActionLogger", "delete");
		this.addParameter("id", String.valueOf(id));
		String result = this.executeAction();
		return result;
	}

	private void compareIds(Integer[] expected, List<Integer> received) {
		assertEquals(expected.length, received.size());
		for (Integer id : expected) {
			if (!received.contains(id)) {
				fail("Id \"" + id + "\" not found");
			}
		}
	}

	private Map<String, String> createSearchParams(String username, String actionName, 
			String namespace, String params, String start, String end) {
		Map<String, String> searchParams = new HashMap<String, String>();
		if (username != null) {
			searchParams.put("username", username);
		}
		if (actionName != null) {
			searchParams.put("actionName", actionName);
		}
		if (namespace != null) {
			searchParams.put("namespace", namespace);
		}
		if (params != null) {
			searchParams.put("params", params);
		}
		if (start != null) {
			searchParams.put("start", start);
		}
		if (end != null) {
			searchParams.put("end", end);
		}
		return searchParams;
	}

	private void init() {
		this._actionLoggerManager = (IActionLoggerManager) this.getService(JpactionloggerSystemConstants.ACTION_LOGGER_MANAGER);
		this._helper = new JpactionloggerTestHelper(this.getApplicationContext());
	}

	@Override
	protected void tearDown() throws Exception {
		this._helper.cleanRecords();
		super.tearDown();
	}

	private IActionLoggerManager _actionLoggerManager;
	private JpactionloggerTestHelper _helper;

}