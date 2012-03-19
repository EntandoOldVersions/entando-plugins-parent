/*
*
* Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* jAPS is a free software; 
* you can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpmyportal.apsadmin.myportal.config;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.agiletec.plugins.jpmyportal.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jpmyportal.util.JpmyportalTestHelper;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jpmyportal.aps.system.JpmyportalSystemConstants;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.IPageModelUserConfigManager;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.model.MyPortalConfig;
import com.agiletec.plugins.jpmyportal.apsadmin.myportal.config.ConfigAction;
import com.opensymphony.xwork2.Action;

public class TestConfigAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
        this._helper.initUserPageConfig();
    }
	
	@Override
	protected void tearDown() throws Exception {
		this._helper.cleanUserPageConfig();
		super.tearDown();
	}
	
	private void init() {
		this._pageModelUserConfigManager = (IPageModelUserConfigManager) this.getService(JpmyportalSystemConstants.PAGE_MODEL_USER_CONFIG_MANAGER);
		this._helper = new JpmyportalTestHelper(this.getApplicationContext());
	}
	
	public void testEdit() throws Throwable {
		String result = this.executeEdit("admin");
		assertEquals(Action.SUCCESS, result);
		this.verifyAction(true, new String[] { "jpmyportal_void" }, true, true); // Rimuove le showlet non consentite
	}
	
	public void testAddShowlet() throws Throwable {
		String result = this.executeAddShowlet("admin", new String[] {}, "content_viewer", true, false); // Showlet non consentita
		assertEquals(Action.SUCCESS, result);
		this.verifyAction(false, new String[] { }, true, false);
		
		result = this.executeAddShowlet("admin", new String[] {}, "logic_type", true, false);
		assertEquals(Action.SUCCESS, result);
		this.verifyAction(false, new String[] { "logic_type" }, true, false);
		
		result = this.executeAddShowlet("admin", new String[] { "content_viewer" }, "content_viewer_list", false, true); // Showlet non consentita
		assertEquals(Action.SUCCESS, result);
		this.verifyAction(false, new String[] { "content_viewer" }, false, true);
		
		result = this.executeAddShowlet("admin", new String[] { "content_viewer" }, "messages_system", false, true); // Showlet non consentita
		assertEquals(Action.SUCCESS, result);
		this.verifyAction(false, new String[] { "content_viewer", "messages_system" }, false, true);
		
		result = this.executeAddShowlet("admin", new String[] { "content_viewer", "content_viewer_list" }, "content_viewer_list", false, false);
		assertEquals(Action.SUCCESS, result);
		this.verifyAction(false, new String[] { "content_viewer", "content_viewer_list" }, false, false);
		
		result = this.executeAddShowlet("admin", new String[] { "content_viewer", "content_viewer_list" }, "showlet_inesistente", true, true);
		assertEquals(Action.SUCCESS, result);
		this.verifyAction(false, new String[] { "content_viewer", "content_viewer_list" }, true, true);
	}
	
	public void testRemoveShowlet() throws Throwable {
		String result = this.executeRemoveShowlet("admin", new String[] {}, "content_viewer", true, false);
		assertEquals(Action.SUCCESS, result);
		this.verifyAction(false, new String[] { }, true, false);
		
		result = this.executeRemoveShowlet("admin", new String[] { "content_viewer", "content_viewer_list" }, "content_viewer_list", false, true);
		assertEquals(Action.SUCCESS, result);
		this.verifyAction(false, new String[] { "content_viewer" }, false, true);
		
		result = this.executeRemoveShowlet("admin", new String[] { "content_viewer" }, "content_viewer_list", false, false);
		assertEquals(Action.SUCCESS, result);
		this.verifyAction(false, new String[] { "content_viewer" }, false, false);
		
		result = this.executeRemoveShowlet("admin", new String[] { "content_viewer", "content_viewer_list" }, "showlet_inesistente", true, true);
		assertEquals(Action.SUCCESS, result);
		this.verifyAction(false, new String[] { "content_viewer", "content_viewer_list" }, true, true);
	}
	
	public void testSave() throws Throwable {
		ConfigInterface configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
		String originaryConfig = configManager.getConfigItem(JpmyportalSystemConstants.MYPORTAL_CONFIG_ITEM);
		try {
			String result = this.executeSave("admin", true, new String[] { "login_form", "content_viewer", "logic_type", "formAction" }, false, true);
			assertEquals(Action.INPUT, result);
			Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
			assertEquals(1, fieldErrors.size());
			List<String> showletErrors = fieldErrors.get("showlets");
			assertEquals(2, showletErrors.size());
			assertTrue(showletErrors.contains(this.getAction().getText("errors.myportalConfig.showlets.notValid", new String[] { "content_viewer" })));
			assertTrue(showletErrors.contains(this.getAction().getText("errors.myportalConfig.showlets.notValid", new String[] { "formAction" })));
			
			result = this.executeSave("admin", false, new String[] { "messages_system", "login_form" }, true, false);
			assertEquals(Action.SUCCESS, result);
			MyPortalConfig config = this._pageModelUserConfigManager.getConfig();
			assertFalse(config.isAjaxEnabled());
			Set<String> showlets = config.getAllowedShowlets();
			this.compareCodes(new String[] { "messages_system", "login_form" }, showlets);
			
			result = this.executeSave("admin", true, new String[] { "login_form", "jpmyportal_void", "logic_type" }, false, true);
			assertEquals(Action.SUCCESS, result);
			config = this._pageModelUserConfigManager.getConfig();
			assertTrue(config.isAjaxEnabled());
			showlets = config.getAllowedShowlets();
			this.compareCodes(new String[] { "login_form", "jpmyportal_void", "logic_type" }, showlets);
		} catch (Throwable t) {
			throw t;
		} finally {
			configManager.updateConfigItem(JpmyportalSystemConstants.MYPORTAL_CONFIG_ITEM, originaryConfig);
		}
	}
	
	private void compareCodes(String[] expected, Collection<String> received) {
		assertEquals(expected.length, received.size());
		for (String code : expected) {
			if (!received.contains(code)) {
				fail("Expected \"" + code + "\" not found");
			}
		}
	}
	
	private void verifyAction(boolean ajaxEnabled, String[] showlets, boolean testAllowed, boolean testValid) {
		ConfigAction action = (ConfigAction) this.getAction();
		assertEquals(ajaxEnabled, action.getAjaxEnabled()!=null && action.getAjaxEnabled().booleanValue());
		this.compareCodes(showlets, action.getShowlets());
//		assertEquals(testAllowed, action.getTestAllowed()!=null && action.getTestAllowed().booleanValue());
//		assertEquals(testValid, action.getTestValid()!=null && action.getTestValid().booleanValue());
	}
	
	private String executeEdit(String username) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpmyportal/MyPortal/Config", "edit");
		String result = this.executeAction();
		return result;
	}
	
	private String executeAddShowlet(String username, String[] showlets, String showletCode, boolean testAllowed, boolean testValid) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpmyportal/MyPortal/Config", "addShowlet");
		this.addParameter("showlets", showlets);
		this.addParameter("showletCode", showletCode);
		this.addParameter("testAllowed", testAllowed);
		this.addParameter("testValid", testValid);
		String result = this.executeAction();
		return result;
	}
	
	private String executeRemoveShowlet(String username, String[] showlets, String showletCode, boolean testAllowed, boolean testValid) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpmyportal/MyPortal/Config", "removeShowlet");
		this.addParameter("showlets", showlets);
		this.addParameter("showletCode", showletCode);
		this.addParameter("testAllowed", testAllowed);
		this.addParameter("testValid", testValid);
		String result = this.executeAction();
		return result;
	}
	
	private String executeSave(String username, boolean ajaxEnabled, String[] showlets, boolean testAllowed, boolean testValid) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpmyportal/MyPortal/Config", "save");
		this.addParameter("ajaxEnabled", String.valueOf(ajaxEnabled));
		this.addParameter("showlets", showlets);
		this.addParameter("testAllowed", testAllowed);
		this.addParameter("testValid", testValid);
		String result = this.executeAction();
		return result;
	}
	
	private IPageModelUserConfigManager  _pageModelUserConfigManager;
	private JpmyportalTestHelper _helper;
	
}