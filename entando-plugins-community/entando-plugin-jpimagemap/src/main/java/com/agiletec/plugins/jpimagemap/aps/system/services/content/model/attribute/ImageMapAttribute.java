/*
*
* Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
* This file is part of jAPS software.
* jAPS is a free software; 
* you can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
*/
package com.agiletec.plugins.jpimagemap.aps.system.services.content.model.attribute;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

import com.agiletec.aps.system.common.entity.model.attribute.AbstractComplexAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.ImageAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.JAXBLinkValue;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.JAXBResourceValue;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.LinkAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.ResourceAttributeInterface;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jpimagemap.aps.system.services.content.model.attribute.model.JAXBAreaValue;
import com.agiletec.plugins.jpimagemap.aps.system.services.content.model.attribute.model.JAXBImageMapValue;
import com.agiletec.plugins.jpimagemap.aps.system.services.content.model.attribute.util.LinkedArea;

/**
 * Rappresenta un informazione tipo ImageMap.
 * @author E.Santoboni - G.Cocco
 */
public class ImageMapAttribute extends AbstractComplexAttribute implements ResourceAttributeInterface {
	
	@Override
	public void setRenderingLang(String langCode) {
		super.setRenderingLang(langCode);
		if (this.getImage() != null) {
			this.getImage().setRenderingLang(langCode);
			if (_areas.size() > 0) {
				Iterator<LinkedArea> iteratorAreas = _areas.iterator();
				while (iteratorAreas.hasNext()) {
					LinkedArea linkedArea = (LinkedArea) iteratorAreas.next();
					linkedArea.getLink().setRenderingLang(langCode);
				}
			}
		}
	}
	
	@Override
	public Object getAttributePrototype() {
		ImageMapAttribute clone = (ImageMapAttribute) super.getAttributePrototype();
		if (this.getImage() != null) {
			clone.setImage((ImageAttribute)this.getImage().getAttributePrototype());
		}
		if (this.getPrototype() != null) {
			clone.setPrototype((LinkedArea)this.getPrototype().clone());
		}
		return clone;
	}
	
	@Override
	public List<AttributeInterface> getAttributes() {
		List<AttributeInterface> attributes = new ArrayList<AttributeInterface>();
		attributes.add(_image);
		for(int i = 0; i < _areas.size(); i++ ){
			attributes.add(((LinkedArea)_areas.get(i)).getLink());
		}
		return attributes;
	}
	
	@Override
	public Object getRenderingAttributes() {
		return this;
	}
	
	@Override
	public void setComplexAttributeConfig(Element attributeElement, Map attrTypes) throws ApsSystemException {
		_image = (ImageAttribute) ((ImageAttribute) attrTypes.get("Image")).getAttributePrototype();
		_image.setAttributeConfig(attributeElement);
		_prototype = new LinkedArea();
		_prototype.setLink((LinkAttribute)((LinkAttribute) attrTypes.get("Link")).getAttributePrototype());
		_prototype.getLink().setAttributeConfig(attributeElement);
	}
	
	@Override
	public Element getJDOMElement() {
		Element attributeElement = new Element("imagemap");
		attributeElement.setAttribute("name", this.getName());
		attributeElement.setAttribute("attributetype", "ImageMap");
		
		this.setTimeStamp();
		Element timestampElement = new Element("timestamp");
		timestampElement.setText(this.getTimestamp());
		attributeElement.addContent(timestampElement);
		
		if (_image != null) {
			attributeElement.addContent(_image.getJDOMElement());
			if (_areas.size() > 0) {
				Element areas = new Element("areas");
				Iterator iteratorAreas = _areas.iterator();
				while (iteratorAreas.hasNext()) {
					Element area = new Element("area");
					LinkedArea linkedArea = (LinkedArea) iteratorAreas.next();
					area.setAttribute("shape", linkedArea.getShape());
					area.setAttribute("coords", linkedArea.getCoords());
					area.addContent(linkedArea.getLink().getJDOMElement());
					areas.addContent(area);
				}
				attributeElement.addContent(areas);
			}
		}
		return attributeElement;
	}
	
	public LinkedArea getArea(int index){
		return _areas.get(index);
	}
	public LinkedArea addArea() {
		LinkedArea newArea = (LinkedArea) this._prototype.clone();
		newArea.setShape("rect");
		//TODO per ora solo rect come forma
		newArea.setCoords("0,0,0,0");
		newArea.getLink().setDefaultLangCode(this.getDefaultLangCode());
		this._areas.add(newArea);
		return newArea;
	}
	public void removeArea(int index){
		this._areas.remove(index);
	}
	
	public ImageAttribute getImage() {
		if (null != _image) {
			this._image.setDefaultLangCode(this.getDefaultLangCode());
			this._image.setParentEntity(this.getParentEntity());
		}
		return _image;
	}
	public void setImage(ImageAttribute image) {
		this._image = image;
	}
	
	private LinkedArea getPrototype() {
		return this._prototype;
	}
	private void setPrototype(LinkedArea prototype) {
		this._prototype = prototype;
	}
	
	@Override
	public ResourceInterface getResource() {
		if (this.getImage() != null) {
			return this.getImage().getResource();
		}
		return null;
	}
	@Override
	public ResourceInterface getResource(String langCode) {
		return this.getResource();
	}
	@Override
	public void setResource(ResourceInterface res, String langCode) {
		if (this.getImage() != null) {
			this.getImage().setResource(res, this.getDefaultLangCode());
		}
	}
	
	public List<LinkedArea> getAreas() {
		return this._areas;
	}
	public void setAreas(List<LinkedArea> areas) {		
		this._areas = areas;
	}
	
	public String getTimestamp() {
		return _timestamp;
	}
	private void setTimeStamp() {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		String timemillis = String.valueOf(gregorianCalendar.getTimeInMillis());
		this.setTimeStamp(timemillis);
	}
	public void setTimeStamp(String timestamp) {
		this._timestamp = timestamp;
	}
	
	@Override
	public Object getValue() {
		return this;
	}
	
	@Override
	protected Object getJAXBValue(String langCode) {
		JAXBImageMapValue imageMapValue = new JAXBImageMapValue();
		JAXBResourceValue jaxbImageValue = (JAXBResourceValue) this.getImage().getJAXBAttribute(langCode).getValue();
		imageMapValue.setImage(jaxbImageValue);
		for (int i = 0; i < this.getAreas().size(); i++) {
			LinkedArea area = this.getAreas().get(i);
			JAXBAreaValue areaValue = new JAXBAreaValue();
			JAXBLinkValue areaLinkValue = (JAXBLinkValue) area.getLink().getJAXBAttribute(langCode).getValue();
			areaValue.setLink(areaLinkValue);
			areaValue.setCoords(area.getCoords());
			imageMapValue.addArea(areaValue);
		}
		return imageMapValue;
	}
	
	private ImageAttribute _image;
	private String _timestamp;
	private List<LinkedArea> _areas = new ArrayList<LinkedArea>();
	private LinkedArea _prototype;
	
}