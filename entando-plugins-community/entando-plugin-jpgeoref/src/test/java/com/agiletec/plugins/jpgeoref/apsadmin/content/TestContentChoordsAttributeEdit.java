package com.agiletec.plugins.jpgeoref.apsadmin.content;

import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jpgeoref.apsadmin.ApsAdminPluginBaseTestCase;

public class TestContentChoordsAttributeEdit extends ApsAdminPluginBaseTestCase {
	
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	
	public void test () throws Throwable {
		
		this.executeEdit(_contentId, "admin");
		
	}
	
	
	protected String executeEdit(String contentId, String currentUserName) throws Throwable {
		this.initAction("/do/jacms/Content", "edit");
		this.setUserOnSession(currentUserName);
		this.addParameter("contentId", contentId);
		String result = this.executeAction();
		return result;
	}

	private void init() throws Exception {
    	try {
    				
    		
    		_contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
    	
	    		IApsEnt
	    		
	    		((IEntityTypesConfigurer) _contentManager).addEntityPrototype();
    	
    	} catch (Throwable t) {
            throw new Exception(t);
        }
    }
	
	protected IContentManager getContentManager() {
		return this._contentManager;
	}
    
    private IContentManager _contentManager = null;
    private String _contentId = "GEO4";

}
