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
package com.agiletec.plugins.jpuserprofile.aps.internalservlet.common;

import java.util.List;

import com.agiletec.plugins.jpuserprofile.apsadmin.ApsAdminPluginBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
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
public class TestFrontCurrentUserProfileAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testEditProfile_1() throws Throwable {
		this.initAction("/do/jpuserprofile/Front/CurrentUser/Profile", "edit");
		this.setUserOnSession(USERNAME_FOR_TEST);
		String result = this.executeAction();
		assertEquals("currentUserWithoutProfile", result);
        IUserProfile currentUserProfile = (IUserProfile) this.getRequest().getSession().getAttribute(ICurrentUserProfileAction.SESSION_PARAM_NAME_CURRENT_PROFILE);
        assertNull(currentUserProfile);
	}
	
    public void testEditProfile_2() throws Throwable {
    	this.setUserOnSession("editorCustomers");
        this.initAction("/do/jpuserprofile/Front/CurrentUser/Profile", "edit");
        String result = this.executeAction();
        assertEquals(Action.SUCCESS, result);
        IUserProfile currentUserProfile = (IUserProfile) this.getRequest().getSession().getAttribute(ICurrentUserProfileAction.SESSION_PARAM_NAME_CURRENT_PROFILE);
        assertNotNull(currentUserProfile);
        assertEquals("editorCustomers", currentUserProfile.getUsername());
    }
	
	public void testValidateProfile() throws Throwable {
		this.initAction("/do/jpuserprofile/Front/CurrentUser/Profile", "edit");
		this.setUserOnSession("editorCustomers");
		String result = this.executeAction();
        assertEquals(Action.SUCCESS, result);
		
        this.initAction("/do/jpuserprofile/Front/CurrentUser/Profile", "save");
		this.addParameter("Name", "");
        this.addParameter("Surname", "");
        result = this.executeAction();
        assertEquals(Action.INPUT, result);
		
        ActionSupport action = this.getAction();
        assertEquals(2, action.getFieldErrors().size());

        this.initAction("/do/jpuserprofile/Front/CurrentUser/Profile", "save");
        this.addParameter("Name", "Ronald");
        this.addParameter("Surname", "Rossi");
        this.addParameter("email", "");
        this.addParameter("birthdate", "25/09/1972");
        this.addParameter("language", "it");
        result = this.executeAction();
        assertEquals(Action.INPUT, result);

        action = this.getAction();
        assertEquals(1, action.getFieldErrors().size());
        assertEquals(1, ((List<String>) action.getFieldErrors().get("email")).size());

        IUserProfile currentUserProfile = (IUserProfile) this.getRequest().getSession().getAttribute(ICurrentUserProfileAction.SESSION_PARAM_NAME_CURRENT_PROFILE);
        assertNotNull(currentUserProfile);
        assertEquals("editorCustomers", currentUserProfile.getUsername());
        assertEquals("Ronald", currentUserProfile.getValue("Name"));
        assertEquals("Rossi", currentUserProfile.getValue("Surname"));
	}
	/*
	public void testSaveProfile() throws Throwable {
		this.initAction("/do/jpuserprofile/Front/CurrentUser/Profile", "edit");
		this.setUserOnSession(USERNAME_FOR_TEST);
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		try {
			this.initAction("/do/jpuserprofile/Front/CurrentUser/Profile", "save");
			this.addParameter("Name", "Eugenio");
			this.addParameter("Surname", "Santoboni");
			this.addParameter("email", "ccccccc@vvvvv.it");
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
	*/
	private void init() throws Exception {
    	try {
    		this._profileManager = (IUserProfileManager) this.getService(ProfileSystemConstants.USER_PROFILE_MANAGER);
    		this._userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
    		User user = this.createUserForTest(USERNAME_FOR_TEST);
    		this._userManager.addUser(user);
		} catch (Throwable e) {
			throw new Exception(e);
		}
    }
	
    @Override
	protected void tearDown() throws Exception {
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
	
}
