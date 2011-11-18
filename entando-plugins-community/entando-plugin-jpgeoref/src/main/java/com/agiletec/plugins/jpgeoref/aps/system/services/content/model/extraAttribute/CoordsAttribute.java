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
package com.agiletec.plugins.jpgeoref.aps.system.services.content.model.extraAttribute;

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
		this.getCoords()[0] = x;
	}
	/**
	 * Sets coordinate y
	 * @param y coordinate y
	 */
	public void setY(double y){
		this.getCoords()[1] = y;
	}
	/**
	 * Sets coordinate z
	 * @param z coordinate z
	 */
	public void setZ(double z){
		this.getCoords()[2] = z;
	}
	/**
	 * Returns coordinate x
	 * @return coordinate x
	 */
	public double getX() {
		return this.getCoords()[0];
	}
	/**
	 * Returns coordinate y
	 * @return coordinate y
	 */
	public double getY() {
		return this.getCoords()[1];
	}
	/**
	 * Returns coordinate z
	 * @return coordinate z
	 */
	public double getZ() {
		return this.getCoords()[2];
	}
	/**
	 * Returns coordinate
	 * @return coordinate
	 */
	public double[] getCoords() {
		return _coords;
	}
	/**
	 * Sets coordinate
	 * @param coords coordinate
	 */
	public void setCoords(double[] coords) {
		this._coords = coords;
	}
	
	@Override
	public Object getValue() {
		return this;
	}
	
	@Override
	protected Object getJAXBValue(String langCode) {
		return this._coords;
	}

	private static final String ATTRIBUTE_ELEMENT = "attribute";
	private static final String ATTRIBUTE_ELEMENT_NAME = "name";
	private static final String ATTRIBUTE_ELEMENT_TYPE = "attributetype";

	private double[] _coords = new double[3];

}
