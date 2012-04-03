package com.agiletec.plugins.jpimagemap.apsadmin.content.attribute.manager;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Classe manager degli attributi tipo risorsa Image(per ImageMap attribute).
 * @author E.Santoboni - G.Cocco
 */
public class ResourceAttributeManager extends com.agiletec.plugins.jacms.apsadmin.content.attribute.manager.ResourceAttributeManager {
	
	@Override
	protected void checkAttribute(ActionSupport action,
			AttributeInterface attribute, com.agiletec.apsadmin.system.entity.attribute.AttributeTracer tracer,
			IApsEntity entity) {
		super.checkAttribute(action, attribute, tracer, entity);
	}
	
	/**
	 * @deprecated As of version 2.4.1 of Entando, moved validation within single attribute.
	 */
	@Override
	protected void updateAttribute(AttributeInterface attribute, com.agiletec.apsadmin.system.entity.attribute.AttributeTracer tracer, HttpServletRequest request) {
		super.updateAttribute(attribute, tracer, request);		
	}
	
	protected void updateAttribute(AttributeInterface attribute, AttributeTracer tracer, HttpServletRequest request) {
		super.updateAttribute(attribute, tracer, request);		
	}
	
}