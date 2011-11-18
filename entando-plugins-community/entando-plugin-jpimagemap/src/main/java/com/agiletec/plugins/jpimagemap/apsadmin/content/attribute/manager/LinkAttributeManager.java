package com.agiletec.plugins.jpimagemap.apsadmin.content.attribute.manager;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.apsadmin.system.entity.attribute.AttributeTracer;
import com.agiletec.plugins.jpimagemap.aps.system.services.content.model.attribute.ImageMapAttribute;
import com.opensymphony.xwork2.ActionSupport;

public class LinkAttributeManager extends com.agiletec.plugins.jacms.apsadmin.content.attribute.manager.LinkAttributeManager {
	
	@Override
	protected int getState(AttributeInterface attribute, AttributeTracer tracer) {
		return super.getState(attribute, tracer);
	}
	
	@Override
	protected void updateAttribute(AttributeInterface attribute,
			AttributeTracer tracer, HttpServletRequest request) {
		super.updateAttribute(attribute, tracer, request);
	}
	
	@Override
	protected void checkAttribute(ActionSupport action,	AttributeInterface attribute, AttributeTracer tracer, IApsEntity entity) {
		if (attribute instanceof ImageMapAttribute) {
			boolean isValidLink = this.isValidLinkedAreaElement(attribute, tracer);
			if (!isValidLink) {
				String formFieldName = tracer.getFormFieldName(attribute);
				String[] args = { attribute.getName(), String.valueOf(tracer.getListIndex()+1) };
				this.addFieldError(action, formFieldName, this.getLinkedAreaElementNotValidMessage(), args);
			}
		}
		super.checkAttribute(action, attribute, tracer, entity);
	}
	
	protected boolean isValidLinkedAreaElement(AttributeInterface attribute, AttributeTracer tracer) {
		return super.isValidMonoListElement(attribute, tracer);
	}
	
	protected String getLinkedAreaElementNotValidMessage() {
		return "Content.linkedAreaElement.invalidLink.maskmsg";
	}
	
}