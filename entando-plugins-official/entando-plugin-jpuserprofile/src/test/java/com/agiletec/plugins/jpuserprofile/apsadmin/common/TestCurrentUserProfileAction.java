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
package com.agiletec.plugins.jpuserprofile.apsadmin.common;

import java.util.List;

import com.agiletec.plugins.jpuserprofile.apsadmin.ApsAdminPluginBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IApsAuthority;
import com.agiletec.aps.system.services.authorization.authorizator.IApsAuthorityManager;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.plugins.jpuserprofile.aps.system.services.ProfileSystemConstants;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.IUserProfileManager;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.model.IUserProfile;
import com.agiletec.plugins.jpuserprofile.apsadmin.common.ICurrentUserProfileAction;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author E.Santoboni
 */
public class TestCurrentUserProfileAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testEditProfile() throws Throwable {
		this.initAction("/do/jpuserprofile/Profile", "edit");
		this.setUserOnSession(USERNAME_FOR_TEST);
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		IUserProfile currentUserProfile = (IUserProfile) this.getRequest().getSession().getAttribute(ICurrentUserProfileAction.SESSION_PARAM_NAME_CURRENT_PROFILE);
		assertNotNull(currentUserProfile);
		assertEquals(USERNAME_FOR_TEST, currentUserProfile.getUsername());
	}
	
	public void testValidateProfile() throws Throwable {
		this.initAction("/do/jpuserprofile/Profile", "edit");
		this.setUserOnSession(USERNAME_FOR_TEST);
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		
		this.initAction("/do/jpuserprofile/Profile", "save");
		result = this.executeAction();
		assertEquals(Action.INPUT, result);
		
		ActionSupport action = this.getAction();
		assertEquals(5, action.getFieldErrors().size());
		
		this.initAction("/do/jpuserprofile/Profile", "save");
		this.addParameter("Name", "Eugenio");
		this.addParameter("Surname", "Santoboni");
		this.addParameter("email", "");
		this.addParameter("birthdate", "25/09/1972");
		this.addParameter("language", "it");
		result = this.executeAction();
		assertEquals(Action.INPUT, result);
		
		action = this.getAction();
		assertEquals(1, action.getFieldErrors().size());
		assertEquals(1, ((List<String>)action.getFieldErrors().get("email")).size());
		
		IUserProfile currentUserProfile = (IUserProfile) this.getRequest().getSession().getAttribute(ICurrentUserProfileAction.SESSION_PARAM_NAME_CURRENT_PROFILE);
		assertNotNull(currentUserProfile);
		assertEquals(USERNAME_FOR_TEST, currentUserProfile.getUsername());
		assertEquals("Eugenio", currentUserProfile.getValue("Name"));
		assertEquals("Santoboni", currentUserProfile.getValue("Surname"));
		
	}
	
	public void testSaveProfile() throws Throwable {
		this.initAction("/do/jpuserprofile/Profile", "edit");
		this.setUserOnSession(USERNAME_FOR_TEST);
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		try {
			this.initAction("/do/jpuserprofile/Profile", "save");
			this.addParameter("Name", "Eugenio");
			this.addParameter("Surname", "Santoboni");
			this.addParameter("email", "eugeniosant@tiscali.it");
			this.addParameter("birthdate", "25/09/1972");
			this.addParameter("language", "it");
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			
			IUserProfile userProfile = this._profileManager.getProfile("mainEditor");
			assertNotNull(userProfile);
			assertEquals("Amanda", userProfile.getValue("Name"));
		} catch (Throwable t) {
			throw t;
		} finally {
			this._profileManager.deleteProfile(USERNAME_FOR_TEST);
			IUserProfile userProfile = this._profileManager.getProfile(USERNAME_FOR_TEST);
			assertNull(userProfile);
		}
	}
	
	private void init() throws Exception {
    	try {
    		this._profileManager = (IUserProfileManager) this.getService(ProfileSystemConstants.USER_PROFILE_MANAGER);
    		this._userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
    		User user = this.createUserForTest(USERNAME_FOR_TEST);
    		this._userManager.addUser(user);
    		this._roleManager = (IApsAuthorityManager) this.getService(SystemConstants.ROLE_MANAGER);
    		this._role = (Role) this._roleManager.getAuthority("editor");
    		this._roleManager.setUserAuthorization(USERNAME_FOR_TEST, this._role);
		} catch (Throwable e) {
			throw new Exception(e);
		}
    }
	
    @Override
	protected void tearDown() throws Exception {
    	this._roleManager.removeUserAuthorization(USERNAME_FOR_TEST, this._role);
		this._userManager.removeUser(USERNAME_FOR_TEST);
		super.tearDown();
	}
    
	protected User createUserForTest(String username) {
    	User user = new User();
		user.setUsername(username);
        user.setPassword(username);
        return user;
	}
	
    private IUserProfileManager _profileManager;
	private IUserManager _userManager = null;
	
	private static final String USERNAME_FOR_TEST = "jpuserprofile_testUser";
	
	private IApsAuthorityManager _roleManager;
	private IApsAuthority _role = null;
	
}
