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
package com.agiletec.plugins.jpgeoref.aps.system.services.content.model.extraAttribute;

import com.agiletec.aps.system.common.entity.model.attribute.DefaultJAXBAttribute;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import com.agiletec.aps.system.common.entity.model.AttributeSearchInfo;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractAttribute;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.plugins.jpgeoref.aps.system.GeoRefSystemConstants;

/**
 * Representation of an information type "geographical coordinates", mono language.
 * @author E.Santoboni
 */
public class CoordsAttribute extends AbstractAttribute {
	
	/**
	 * Returns JDOM element
	 * @return JDOM element
	 */
	@Override
	public Element getJDOMElement() {
		Element attributeElement = new Element(ATTRIBUTE_ELEMENT);
		attributeElement.setAttribute(ATTRIBUTE_ELEMENT_NAME, this.getName());
		attributeElement.setAttribute(ATTRIBUTE_ELEMENT_TYPE, this.getType());
		if (0 != this.getX() && 0 != this.getY()) {
			attributeElement.addContent(this.createCoordElem(GeoRefSystemConstants.COORDS_ATTRIBUTE_X, this.getX()));
			attributeElement.addContent(this.createCoordElem(GeoRefSystemConstants.COORDS_ATTRIBUTE_Y, this.getY()));
			attributeElement.addContent(this.createCoordElem(GeoRefSystemConstants.COORDS_ATTRIBUTE_Z, this.getZ()));
		}
		return attributeElement;
	}

	/**
	 * Create coordinate element
	 * @param name coordinate element
	 * @param coord coordinate value
	 * @return coordinate element
	 */
	private Element createCoordElem(String name, double coord) {
		Element coordElement = new Element(name);
		coordElement.setText(String.valueOf(coord));
		return coordElement;
	}
	
	@Override
	public boolean isSearchableOptionSupported() {
		return true;
	}
	
	/**
	 * Returns search information
	 * @param system languages
	 * @return search information
	 */
	@Override
	public List<AttributeSearchInfo> getSearchInfos(List<Lang> systemLangs) {
		if (0 != this.getX() && 0 != this.getY()) {
			List<AttributeSearchInfo> infos = new ArrayList<AttributeSearchInfo>();
			String stringInfo = "["+(String.valueOf(this.getX()) + "," 
					+ String.valueOf(this.getY()) + "," 
					+ String.valueOf(this.getZ()))+"]";
			AttributeSearchInfo info = new AttributeSearchInfo(stringInfo, null, null, null);
			infos.add(info);
			return infos;
		}
		return null;
	}
	
	/**
	 * Sets coordinate x
	 * @param x coordinate x
	 */
	public void setX(double x){
		_x = x;
	}
	/**
	 * Sets coordinate y
	 * @param y coordinate y
	 */
	public void setY(double y){
		_y = y;
	}
	/**
	 * Sets coordinate z
	 * @param z coordinate z
	 */
	public void setZ(double z){
		_z = z ;
	}
	/**
	 * Returns coordinate x
	 * @return coordinate x
	 */
	public double getX() {
		return _x;
	}
	/**
	 * Returns coordinate y
	 * @return coordinate y
	 */
	public double getY() {
		return _y;
	}
	
	/**
	 * Returns coordinate z
	 * @return coordinate z
	 */
	public double getZ() {
		return _z;
	}
	
	@Override
	public Object getValue() {
		return this;
	}
	
	@Override
	protected Object getJAXBValue(String langCode) {
		StringBuffer coords = new StringBuffer();
		coords.append("(");
		coords.append(this.getX());
		coords.append(",");
		coords.append(this.getY());
		if (this.getZ() != 0) {
			coords.append(",");
			coords.append(this.getZ());
		}
		coords.append(")");
		return coords.toString();
	}
	
	@Override
	public Status getStatus() {
		if (0 == this.getX() && 0 == this.getY() && 0==this.getZ()) {
			return Status.EMPTY;
		} else if (0 != this.getX() && 0 != this.getY()) {
			return Status.VALUED;
		}
		return Status.INCOMPLETE;
	}
	
	@Override
	public void valueFrom(DefaultJAXBAttribute jaxbAttribute) {
		if (null == jaxbAttribute) return;
		String coords = (String) jaxbAttribute.getValue();
		if (null == coords) return;
		String[] coordinates = coords.trim().substring(1, coords.trim().length() - 1).split(",");
		if (coordinates.length < 2) return;
		try {
			this.setX(Long.parseLong(coordinates[0]));
		} catch (Exception e) {}
		try {
			this.setY(Long.parseLong(coordinates[1]));
		} catch (Exception e) {}
		if (coordinates.length > 2) {
			try {
				this.setZ(Long.parseLong(coordinates[2]));
			} catch (Exception e) {}
		}
	}
	
	private static final String ATTRIBUTE_ELEMENT = "attribute";
	private static final String ATTRIBUTE_ELEMENT_NAME = "name";
	private static final String ATTRIBUTE_ELEMENT_TYPE = "attributetype";

	private double _x;
	private double _y;
	private double _z;

}
