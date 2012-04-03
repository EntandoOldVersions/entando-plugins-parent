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
package com.agiletec.plugins.jpavatar.aps.system.services.avatar;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.agiletec.plugins.jpavatar.aps.ApsPluginBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.plugins.jpavatar.aps.system.JpAvatarSystemConstants;

public class TestAvatarManager extends ApsPluginBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testSaveAvatar() throws Throwable {
		File file = new File("target/test/jAPS_logo.jpg");
		this._avatarManager.saveAvatar("admin", file, "jAPS_logo.jpg");
		String filePath = this._avatarManager.getAvatarDiskFolder() + "avatar" + File.separator + "admin.jpg";
		File avatarFile = new File(filePath);
		assertTrue(avatarFile.exists());
		FileUtils.forceDelete(avatarFile);
		assertFalse(avatarFile.exists());
	}
	
	public void testGetAvatar() throws Throwable {
		File file = new File("target/test/jAPS_logo.jpg");
		this._avatarManager.saveAvatar("admin", file, "jAPS_logo.jpg");
		String filename = this._avatarManager.getAvatar("admin");
		assertEquals("admin.jpg", filename);
		assertEquals("admin.jpg", filename);
		assertEquals("/Entando/resources/plugins/jpavatar/", _avatarManager.getAvatarURL());
		assertTrue(this._avatarManager.getAvatarDiskFolder().endsWith("/resources/plugins/jpavatar/"));
	}
	
	public void testDeleteAvatar() throws Throwable {
		File file = new File("target/test/jAPS_logo.jpg");
		this._avatarManager.saveAvatar("admin", file, "jAPS_logo.jpg");
		String filename = this._avatarManager.getAvatar("admin");
		assertEquals("admin.jpg", filename);
		this._avatarManager.removeAvatar("admin");
		filename = this._avatarManager.getAvatar("admin");
		assertNull(filename);	
	}

	public void testDeleteAOPAvatar() throws Throwable {
		User user = new User();
		String username = "jpavatarTestUser";
		user.setUsername(username);
		user.setPassword(username);
		_userManager.addUser(user);
		assertNotNull(_userManager.getUser(username));
		File file = new File("target/test/jAPS_logo.jpg");
		this._avatarManager.saveAvatar(username, file, "jAPS_logo.jpg");
		String filename = this._avatarManager.getAvatar("jpavatarTestUser");
		assertEquals("jpavatarTestUser.jpg".toLowerCase(), filename);
		this._userManager.removeUser(username);
		filename = this._avatarManager.getAvatar(username);
		assertNull(filename);	
	}

	private void init() {
		_avatarManager = (IAvatarManager) this.getService(JpAvatarSystemConstants.AVATAR_MANAGER);
		_userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		FileUtils.cleanDirectory(new File(this._avatarManager.getAvatarDiskFolder() + "avatar"));
		this._userManager.removeUser("jpavatarTestUser");
	}

	private IAvatarManager _avatarManager;
	private IUserManager _userManager;

}
