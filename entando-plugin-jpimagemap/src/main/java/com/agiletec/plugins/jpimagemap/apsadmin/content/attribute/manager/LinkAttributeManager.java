package com.agiletec.plugins.jpimagemap.apsadmin.content.attribute.manager;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;

public class LinkAttributeManager extends com.agiletec.plugins.jacms.apsadmin.content.attribute.manager.LinkAttributeManager {
	
	@Override
	protected void updateAttribute(AttributeInterface attribute, AttributeTracer tracer, HttpServletRequest request) {
		super.updateAttribute(attribute, tracer, request);		
	}
	
}
