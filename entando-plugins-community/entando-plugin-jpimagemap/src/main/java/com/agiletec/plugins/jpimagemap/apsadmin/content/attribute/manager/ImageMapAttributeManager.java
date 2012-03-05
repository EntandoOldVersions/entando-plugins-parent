/*
 *
 * Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 * This file is part of Entando software.
 * JAPS and its  source-code is  licensed under the  terms of the
 * GNU General Public License  as published by  the Free Software
 * Foundation (http://www.fsf.org/licensing/licenses/gpl.txt).
 * 
 * You may copy, adapt, and redistribute this file for commercial
 * or non-commercial use.
 * When copying,  adapting,  or redistributing  this document you
 * are required to provide proper attribution  to AgileTec, using
 * the following attribution line:
 * Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 */
package com.agiletec.plugins.jpimagemap.apsadmin.content.attribute.manager;

import java.awt.Rectangle;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.apsadmin.system.entity.attribute.AttributeTracer;
import com.agiletec.apsadmin.system.entity.attribute.manager.AbstractAttributeManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.ImageAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.LinkAttribute;
import com.agiletec.plugins.jpimagemap.aps.system.services.content.model.attribute.ImageMapAttribute;
import com.agiletec.plugins.jpimagemap.aps.system.services.content.model.attribute.util.LinkedArea;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Classe manager degli attributi tipo ImageMap.
 * @author E.Santoboni - G.Cocco
 */
public class ImageMapAttributeManager extends AbstractAttributeManager {
	
	@Override
	protected int getState(AttributeInterface attribute, AttributeTracer tracer) {
		ImageMapAttribute imageMap = (ImageMapAttribute) attribute;
		if (imageMap.getImage() != null && imageMap.getAreas().size() > 0) {
			return this.VALUED_ATTRIBUTE_STATE;
		} else if ((imageMap.getImage().getResource() != null && imageMap.getAreas().size()==0 ) 
				|| (imageMap.getImage().getResource()==null && imageMap.getAreas().size() != 0) ) {
			return this.INCOMPLETE_ATTRIBUTE_STATE;
		}
		return this.EMPTY_ATTRIBUTE_STATE;
	}
	
	@Override
	protected void updateAttribute(AttributeInterface attribute,
			AttributeTracer tracer, HttpServletRequest request) {
		this.manageImageMapAttribute(false, true, null, attribute, tracer, request, null);
	}
	
	@Override
	protected void checkAttribute(ActionSupport action, AttributeInterface attribute, AttributeTracer tracer, IApsEntity entity) {
		super.checkAttribute(action, attribute, tracer, entity);
		this.manageImageMapAttribute(true, false, action, attribute, tracer, null, entity);
	}
	
	protected void manageImageMapAttribute(boolean isCheck, boolean isUpdate, ActionSupport action, 
			AttributeInterface attribute, AttributeTracer tracer, HttpServletRequest request, IApsEntity entity) {
		ImageMapAttribute imageMapAttribute = (ImageMapAttribute) attribute;
		ImageAttribute imageAttr = imageMapAttribute.getImage();		
		AttributeTracer imageTracer = (AttributeTracer) tracer.clone();
		
		ResourceAttributeManager imageManager = (ResourceAttributeManager) this.getManager(imageAttr.getType());
		if (isCheck && !isUpdate) {
			imageManager.checkAttribute(action, imageAttr, imageTracer, entity);
		}
		if (!isCheck && isUpdate) {
			imageManager.updateAttribute(imageAttr, imageTracer, request);
		}
		List<LinkedArea> areas = imageMapAttribute.getAreas();
		for (int i = 0; i < areas.size(); i++) {
			LinkedArea area = (LinkedArea) areas.get(i);
			AttributeTracer areaTracer = (AttributeTracer) tracer.clone();
			areaTracer.setMonoListElement(true);
			areaTracer.setListIndex(i);
			this.manageArea(area, isCheck, isUpdate, action, imageMapAttribute, areaTracer, request, entity);
		}
	}
	
	protected void manageArea(LinkedArea area, boolean isCheck, boolean isUpdate, ActionSupport action, 
			ImageMapAttribute imageMapAttribute, AttributeTracer tracer, HttpServletRequest request, IApsEntity entity) {
		LinkAttribute link = area.getLink();
		LinkAttributeManager linkManager = (LinkAttributeManager) this.getManager(link.getType());
		if (isCheck && !isUpdate) {
			String coords = imageMapAttribute.getArea(tracer.getListIndex()).getCoords();
			boolean isShapeValued = (area.getShape() != null && area.getShape().trim().length() > 0 );
			boolean isCoordsValued = (coords!= null && coords.trim().length() > 0 && this.isValidNumber(coords));
			if (!isShapeValued || !isCoordsValued) {
				String formFieldName = tracer.getFormFieldName(imageMapAttribute);
				String[] args = { imageMapAttribute.getName(), String.valueOf(tracer.getListIndex()+1) };
				this.addFieldError(action, formFieldName, "Content.linkedAreaElement.invalidArea.maskmsg", args);
			}
			this.isIntersected(area, imageMapAttribute, tracer, action);
			linkManager.checkAttribute(action, link, tracer, entity);
		}
		if (!isCheck && isUpdate) {
			String coords = request.getParameter(imageMapAttribute.getName() + "_coords_" + tracer.getListIndex());
			linkManager.updateAttribute(link, tracer, request);
			area.setCoords(coords);
		}
	}
	
	private boolean isValidNumber(String coords) {
		boolean validate = false;
		Pattern pattern = Pattern.compile("^\\d+,\\d+,\\d+,\\d+$");
		Matcher matcher = pattern.matcher(coords.trim());
		validate = matcher.matches();
		return validate;
	
	}
	
	private void isIntersected(LinkedArea area, ImageMapAttribute imageMapAttribute, AttributeTracer tracer, ActionSupport action) {
		int index = tracer.getListIndex();
		Integer[] coordsArray = area.getArrayCoords();
		Rectangle areaRect = 
			new Rectangle(coordsArray[0].intValue(), coordsArray[1].intValue(), coordsArray[2].intValue() - coordsArray[0].intValue() , coordsArray[3].intValue() - coordsArray[1].intValue());
		for (int i=index-1; i>=0 ; i--){
			LinkedArea currentArea = imageMapAttribute.getArea(i);
			Integer[] currentCoordsArray = currentArea.getArrayCoords();
			Rectangle currentAreaRect = new Rectangle(currentCoordsArray[0].intValue(),currentCoordsArray[1].intValue(), 
					currentCoordsArray[2].intValue() - currentCoordsArray[0].intValue() , currentCoordsArray[3].intValue() - currentCoordsArray[1].intValue());
			boolean intersect = areaRect.intersects(currentAreaRect);
			if (intersect) {
				String formFieldName = tracer.getFormFieldName(imageMapAttribute);
				String[] args = { imageMapAttribute.getName(), String.valueOf(index+1), String.valueOf(i+1)};
				this.addFieldError(action, formFieldName, "Content.linkedAreaElement.intersectedArea", args);
			}
		}
	}
	
}