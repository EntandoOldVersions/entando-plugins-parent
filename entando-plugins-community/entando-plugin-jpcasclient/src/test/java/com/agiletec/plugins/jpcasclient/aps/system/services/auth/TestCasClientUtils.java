package com.agiletec.plugins.jpcasclient.aps.system.services.auth;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.system.services.url.PageURL;
import com.agiletec.plugins.jpcasclient.apsadmin.ApsAdminPluginBaseTestCase;

public class TestCasClientUtils extends ApsAdminPluginBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		// TODO?
		super.setUp();
		_urlManager = (IURLManager) this.getApplicationContext().getBean(SystemConstants.URL_MANAGER);
	}

	
	
	public void test () {
		RequestContext reqCtx = new RequestContext();
		PageURL pageUrl = new PageURL(_urlManager, reqCtx);
	
		String urlWithoutParam = _casClientUtils.getURLStringWithoutTicketParam(pageUrl, reqCtx);
		assertNotNull(urlWithoutParam);
	}
	
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	
	private CasClientUtils _casClientUtils;
	private IURLManager _urlManager;
}
