/*
*
* Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpmyportal.aps.system.services.controller.control;

import org.springframework.mock.web.MockHttpServletRequest;

import com.agiletec.plugins.jpmyportal.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpmyportal.util.JpmyportalTestHelper;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.controller.control.Authenticator;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpmyportal.aps.system.JpmyportalSystemConstants;
import com.agiletec.plugins.jpmyportal.aps.system.services.controller.control.MyPortalControllerService;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.model.PageModelUserConfigBean;

public class TestMyPortalControllerService extends ApsPluginBaseTestCase {
	
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
	
	private void init() throws Exception {
    	this._myPortalControllerService = (MyPortalControllerService) this.getApplicationContext().getBean("jpmyportalControllerService");
    	this._authenticator = (Authenticator) this.getApplicationContext().getBean("AuthenticatorControlService");
    	this._helper = new JpmyportalTestHelper(this.getApplicationContext());
    }
	
	public void testService() throws Throwable {
		PageModelUserConfigBean pageModelUserConfig = null;
		int status;
		UserDetails currentUser = null;
		RequestContext reqCtx = this.getRequestContext();
		MockHttpServletRequest request = (MockHttpServletRequest) reqCtx.getRequest();
		// admin NON ha una configurazione persializzata
		request.setParameter("username", "admin");
		request.setParameter("password", "admin");
		status = _authenticator.service(reqCtx, ControllerManager.CONTINUE);
		assertEquals(ControllerManager.CONTINUE, status);
		currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		assertEquals("admin", currentUser.getUsername());
		// carichiamo la configurazione personalizzata dell'utente admin 
		status = this._myPortalControllerService.service(reqCtx, ControllerManager.CONTINUE);		
		assertEquals(ControllerManager.CONTINUE, status);
		pageModelUserConfig = (PageModelUserConfigBean) request.getSession().getAttribute(JpmyportalSystemConstants.SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG);
		assertNull(pageModelUserConfig);	// l'utente admin non ha configurazione personalizzata
		
		// L'utente 'mainEditor' ha una configurazione personalizzata
		request.setParameter("username", "mainEditor");
		request.setParameter("password", "mainEditor");
		status = _authenticator.service(reqCtx, ControllerManager.CONTINUE);
		assertEquals(status, ControllerManager.CONTINUE);
		currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		assertEquals("mainEditor", currentUser.getUsername());
		status = this._myPortalControllerService.service(reqCtx, ControllerManager.CONTINUE);
		assertEquals(ControllerManager.CONTINUE, status);
		pageModelUserConfig = (PageModelUserConfigBean) request.getSession().getAttribute(JpmyportalSystemConstants.SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG);
		assertNotNull(pageModelUserConfig);
		assertEquals("mainEditor", pageModelUserConfig.getUsername());	// verifichiamo che sia stata caricata la configurazione
		
		//ricarichiamo nuovamente l'utente admin
		request.setParameter("username", "admin");
		request.setParameter("password", "admin");
		status = _authenticator.service(reqCtx, ControllerManager.CONTINUE);
		assertEquals(ControllerManager.CONTINUE, status);
		currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		assertEquals("admin", currentUser.getUsername());
		// carichiamo la configurazione personalizzata dell'utente admin 
		status = this._myPortalControllerService.service(reqCtx, ControllerManager.CONTINUE);		
		assertEquals(ControllerManager.CONTINUE, status);  // la sessione non deve contenere nessuna configurazione personalizzata
		pageModelUserConfig = (PageModelUserConfigBean) request.getSession().getAttribute(JpmyportalSystemConstants.SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG);
		assertNull(pageModelUserConfig);
	}
	
	private MyPortalControllerService _myPortalControllerService;
	private Authenticator _authenticator;
	private JpmyportalTestHelper _helper;
	
}