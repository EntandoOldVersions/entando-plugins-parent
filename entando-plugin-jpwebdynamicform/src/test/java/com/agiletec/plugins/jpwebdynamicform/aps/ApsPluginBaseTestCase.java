/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando Enterprise Edition software.
* You can redistribute it and/or modify it
* under the terms of the Entando's EULA
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwebdynamicform.aps;

import javax.sql.DataSource;

import com.agiletec.ConfigTestUtils;
import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.plugins.jpwebdynamicform.PluginConfigTestUtils;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.JpwebdynamicformSystemConstants;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.IMessageManager;
import com.agiletec.plugins.jpwebdynamicform.util.JpwebdynamicformTestHelper;

public class ApsPluginBaseTestCase extends BaseTestCase {
	
	@Override
	protected ConfigTestUtils getConfigUtils() {
		return new PluginConfigTestUtils();
	}
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }	
	
    @Override
	protected void tearDown() throws Exception {
    	this._helper.cleanMessages();
		super.tearDown();
	}

	private void init() throws Exception {
		try {
			DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
			ILangManager langManager = (ILangManager) this.getService(SystemConstants.LANGUAGE_MANAGER);
			this._messageManager = (IMessageManager) this.getService(JpwebdynamicformSystemConstants.MESSAGE_MANAGER);
			this._helper = new JpwebdynamicformTestHelper(dataSource, langManager, this._messageManager);
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}

	protected JpwebdynamicformTestHelper _helper;
	protected IMessageManager _messageManager;	
}
