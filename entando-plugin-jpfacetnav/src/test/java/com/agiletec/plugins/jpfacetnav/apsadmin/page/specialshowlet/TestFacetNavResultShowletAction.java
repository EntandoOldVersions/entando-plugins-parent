package com.agiletec.plugins.jpfacetnav.apsadmin.page.specialshowlet;

import java.util.List;


import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jpfacetnav.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jpfacetnav.apsadmin.portal.specialshowlet.FacetNavResultShowletAction;
import com.opensymphony.xwork2.Action;

import org.entando.entando.aps.system.services.page.Widget;

public class TestFacetNavResultShowletAction extends ApsAdminPluginBaseTestCase {

	public void testInitConfig_1() throws Throwable {
		String result = this.executeConfigFacetNavResult("admin", "homepage", "1", "jpfacetnav_results");
		assertEquals(Action.SUCCESS, result);
		FacetNavResultShowletAction action = (FacetNavResultShowletAction) this.getAction();
		org.entando.entando.aps.system.services.page.Widget showlet = action.getShowlet();
		assertNotNull(showlet);
		assertEquals(0, showlet.getConfig().size());
		List<SmallContentType> contentTypes = action.getContentTypes();
		assertNotNull(contentTypes);
		assertEquals(4, contentTypes.size());
	}

	private String executeConfigFacetNavResult(String username, String pageCode, String frame, String showletTypeCode) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/Page/SpecialShowlet", "facetNavResultConfig");
		this.addParameter("pageCode", pageCode);
		this.addParameter("frame", frame);
		if (null != showletTypeCode && showletTypeCode.trim().length()>0) {
			this.addParameter("showletTypeCode", showletTypeCode);
		}
		return this.executeAction();
	}

}