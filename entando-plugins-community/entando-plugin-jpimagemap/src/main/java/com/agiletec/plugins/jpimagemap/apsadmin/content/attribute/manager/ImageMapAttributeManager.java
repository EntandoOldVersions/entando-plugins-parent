/*
*
* Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software; 
* you can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpimagemap.apsadmin.content.attribute.manager;

import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.apsadmin.system.entity.attribute.manager.AbstractAttributeManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.ImageAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.LinkAttribute;
import com.agiletec.plugins.jpimagemap.aps.system.services.content.model.attribute.ImageMapAttribute;
import com.agiletec.plugins.jpimagemap.aps.system.services.content.model.attribute.util.LinkedArea;

/**
 * Classe manager degli attributi tipo ImageMap.
 * @author E.Santoboni - G.Cocco
 */
public class ImageMapAttributeManager extends AbstractAttributeManager {
	
	/**
	 * @deprecated As of version 2.4.1 of Entando, moved validation within single attribute.
	 */
	@Override
	protected int getState(AttributeInterface attribute, com.agiletec.apsadmin.system.entity.attribute.AttributeTracer tracer) {
		ImageMapAttribute imageMap = (ImageMapAttribute) attribute;
		if (imageMap.getImage() != null && imageMap.getAreas().size() > 0) {
			return this.VALUED_ATTRIBUTE_STATE;
		} else if ((imageMap.getImage().getResource() != null && imageMap.getAreas().size()==0 ) 
				|| (imageMap.getImage().getResource()==null && imageMap.getAreas().size() != 0) ) {
			return this.INCOMPLETE_ATTRIBUTE_STATE;
		}
		return this.EMPTY_ATTRIBUTE_STATE;
	}
	
	/**
	 * @deprecated As of version 2.4.1 of Entando, moved validation within single attribute.
	 */
	@Override
	protected void updateAttribute(AttributeInterface attribute, com.agiletec.apsadmin.system.entity.attribute.AttributeTracer tracer, HttpServletRequest request) {
		this.updateAttribute(attribute, (AttributeTracer) tracer, request);
	}
	
	@Override
	protected void updateAttribute(AttributeInterface attribute, AttributeTracer tracer, HttpServletRequest request) {
		this.manageImageMapAttribute(attribute, tracer, request, null);
	}
	
	private void manageImageMapAttribute(AttributeInterface attribute, 
			AttributeTracer tracer, HttpServletRequest request, IApsEntity entity) {
		ImageMapAttribute imageMapAttribute = (ImageMapAttribute) attribute;
		ImageAttribute imageAttr = imageMapAttribute.getImage();		
		AttributeTracer imageTracer = (AttributeTracer) tracer.clone();
		ResourceAttributeManager imageManager = (ResourceAttributeManager) this.getManager(imageAttr.getType());
		imageManager.updateAttribute(imageAttr, imageTracer, request);
		List<LinkedArea> areas = imageMapAttribute.getAreas();
		for (int i = 0; i < areas.size(); i++) {
			LinkedArea area = (LinkedArea) areas.get(i);
			AttributeTracer areaTracer = (AttributeTracer) tracer.clone();
			areaTracer.setMonoListElement(true);
			areaTracer.setListIndex(i);
			LinkAttribute link = area.getLink();
			LinkAttributeManager linkManager = (LinkAttributeManager) this.getManager(link.getType());
			String coords = request.getParameter(imageMapAttribute.getName() + "_coords_" + tracer.getListIndex());
			linkManager.updateAttribute(link, areaTracer, request);
			area.setCoords(coords);
		}
	}
	
	protected String getCustomAttributeErrorMessage(AttributeFieldError attributeFieldError, ActionSupport action) {
		String errorCode = attributeFieldError.getErrorCode();
        String messageKey = null;
        if (errorCode.equals(ImageMapAttribute.INVALID_LINKED_AREA_ERROR)) {
            messageKey = "Content.linkedAreaElement.invalidArea.maskmsg";
        } else if (errorCode.equals(ImageMapAttribute.INTERSECTED_AREA_ERROR)) {
            messageKey = "Content.linkedAreaElement.intersectedArea";
        }
        if (null != messageKey) {
            return action.getText(messageKey);
        } else {
            return super.getCustomAttributeErrorMessage(attributeFieldError, action);
        }
    }
	
}