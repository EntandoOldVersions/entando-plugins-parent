/*
*
* Copyright 2009 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
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
* Copyright 2009 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
*/
package com.agiletec.plugins.jpavatar.apsadmin.avatar;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.agiletec.plugins.jpavatar.apsadmin.ApsAdminPluginBaseTestCase;

import com.agiletec.plugins.jpavatar.aps.system.JpAvatarSystemConstants;
import com.agiletec.plugins.jpavatar.aps.system.services.avatar.IAvatarManager;
import com.agiletec.plugins.jpavatar.apsadmin.avatar.AvatarAction;
import com.opensymphony.xwork2.Action;

public class TestAvatarAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testEdit() throws Throwable {
		String result = this.executeEdit();
		assertEquals(Action.SUCCESS, result);
		AvatarAction action = (AvatarAction) this.getAction();
		assertEquals(100, action.getImageMaxHeight());
		assertEquals(100, action.getImageMaxWidth());
		assertEquals(100, action.getImageMaxSize());
		assertEquals("png,jpg", action.getImageTypes());
	}

	public void testEditForGuest() throws Throwable {
		this.initAction(NS, "edit");
		String result = this.executeAction();
		assertEquals("apslogin", result);
	}

	public void testBin() throws Throwable {
		String result = this.executeBin();
		assertEquals(Action.SUCCESS, result);
		AvatarAction action = (AvatarAction) this.getAction();
		List<String> actionMessages = (List<String>) action.getActionMessages();
		assertEquals(1, actionMessages.size());
		assertEquals(action.getText("jpavatar.message.confirmDelete"), actionMessages.get(0));
	}
	
	public void testDelete() throws Throwable {
		File file = new File("target/test/jAPS_logo.jpg");
		this._avatarManager.saveAvatar("admin", file, "jAPS_logo.jpg");
		String filename = this._avatarManager.getAvatar("admin");
		assertEquals("admin.jpg", filename);
		String result = this.executeDelete();
		assertEquals(Action.SUCCESS, result);
		filename = this._avatarManager.getAvatar("admin");
		assertNull(filename);	
	}
	
	public void testSave_1() throws Throwable {
		String result = this.executeSave();
		assertEquals(Action.INPUT, result);
	}

	private String executeEdit() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction(NS, "edit");
		return this.executeAction();
	}
	
	private String executeBin() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction(NS, "bin");
		return this.executeAction();
	}

	private String executeDelete() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction(NS, "delete");
		return this.executeAction();
	}

	private String executeSave() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction(NS, "save");
		return this.executeAction();
	}
	
	private void init() {
		_avatarManager = (IAvatarManager) this.getService(JpAvatarSystemConstants.AVATAR_MANAGER);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		FileUtils.cleanDirectory(new File(this._avatarManager.getAvatarDiskFolder() + "avatar"));
	}
	
	private IAvatarManager _avatarManager;
	private static final String NS = "/do/jpavatar/Avatar";
	
}
