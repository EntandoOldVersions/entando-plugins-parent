package com.agiletec.plugins.jpimagemap.apsadmin.content.attribute.manager;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.apsadmin.system.entity.attribute.AttributeTracer;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Classe manager degli attributi tipo risorsa Image(per ImageMap attribute).
 * @author E.Santoboni - G.Cocco
 */
public class ResourceAttributeManager extends com.agiletec.plugins.jacms.apsadmin.content.attribute.manager.ResourceAttributeManager {
	
	@Override
	protected void checkAttribute(ActionSupport action,
			AttributeInterface attribute, AttributeTracer tracer,
			IApsEntity entity) {
		super.checkAttribute(action, attribute, tracer, entity);
	}
	
	@Override
	protected void updateAttribute(AttributeInterface attribute, AttributeTracer tracer, HttpServletRequest request) {
		super.updateAttribute(attribute, tracer, request);		
	}
	
}