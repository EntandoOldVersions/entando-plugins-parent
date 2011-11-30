package com.agiletec.plugins.jpcasclient.aps.system.services.config.parse;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpcasclient.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpcasclient.aps.system.services.config.CasClientConfig;

public class TestConfigDOM extends ApsPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		_configDOM = new ConfigDOM();
	}

	public void test () throws ApsSystemException {
		CasClientConfig config = _configDOM.extractConfig(xmlConfig);
		assertNotNull(config);
		
		assertEquals("http://japs.intranet:8080/cas/login", config.getCasLoginURL());
		assertEquals("http://japs.intranet:8080/cas/logout", config.getCasLogoutURL());
		assertEquals("http://japs.intranet:8080/cas/validate", config.getCasValidateURL());
		assertEquals("notauth", config.getNotAuthPage());
		assertEquals("demo.entando.com", config.getRealm());
		assertEquals("http://japs.intranet:8080", config.getServerBaseURL());
		
		String configStr = _configDOM.createConfigXml(config);
		assertNotNull(configStr);
		
		assertTrue(configStr.contains(xmlConfig));
		
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		_configDOM = null;
	}
	
	private String xmlConfig = "<casclientConfig>" +
				"<active>false</active>" +
				"<casLoginURL>http://japs.intranet:8080/cas/login</casLoginURL>" +
				"<casLogoutURL>http://japs.intranet:8080/cas/logout</casLogoutURL>" +
				"<casValidateURL>http://japs.intranet:8080/cas/validate</casValidateURL>" +
				"<serverBaseURL>http://japs.intranet:8080</serverBaseURL>" +
				"<notAuthPage>notauth</notAuthPage>" +
				"<realm>demo.entando.com</realm>" +
			"</casclientConfig>";
	private ConfigDOM _configDOM;

}
