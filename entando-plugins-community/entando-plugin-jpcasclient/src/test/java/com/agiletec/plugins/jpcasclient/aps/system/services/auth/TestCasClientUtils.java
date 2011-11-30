package com.agiletec.plugins.jpcasclient.aps.system.services.auth;

import org.springframework.mock.web.MockHttpServletRequest;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.system.services.url.PageURL;

public class TestCasClientUtils extends BaseTestCase {

	@Override
	protected void setUp() throws Exception {
		// TODO?
		super.setUp();
		_urlManager = (IURLManager) this.getApplicationContext().getBean(SystemConstants.URL_MANAGER);
		_langManager = (ILangManager) this.getApplicationContext().getBean(SystemConstants.LANGUAGE_MANAGER);

		_casClientUtils = new CasClientUtils();
	}

	
	
	public void test () {
		RequestContext reqCtx = this.getRequestContext();
		PageURL pageUrl = new PageURL(_urlManager, reqCtx);
//		Lang it = _langManager.getLang("it");
//		reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG, it);
//	
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
	private ILangManager _langManager;
}
