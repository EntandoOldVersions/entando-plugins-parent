/*
 *
 * Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
 * Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 */
package com.agiletec.plugins.jpgeoref.aps.tags;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpgeoref.aps.system.GeoRefSystemConstants;
import com.agiletec.plugins.jpgeoref.aps.system.services.content.model.extraAttribute.CoordsAttribute;
import com.agiletec.plugins.jpgeoref.aps.tags.helper.GeorefInfoBean;

/**
 * @author E.Santoboni
 */
public class GeoRenderListTag extends TagSupport {

	/**
	 * Start tag analysis.
	 */
	public int doStartTag() throws JspException {
		try {
			List<GeorefInfoBean> geoPoints = new ArrayList<GeorefInfoBean>();
			Collection object = (Collection) this.pageContext.getAttribute(this.getMaster());
			if (object == null) {
				ApsSystemUtils.getLogger().severe("There is no list in the request.");
			} else {
				IContentManager contentManager = (IContentManager) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_MANAGER, pageContext);
				Iterator<String> iter = object.iterator();
				double sumx = 0;
				double sumy = 0;
				double westX = MAX_EAST;
				double eastX = MIN_WEST;
				double northY = MIN_SOUTH;
				double southY = MAX_NORTH;
				while (iter.hasNext()) {
					String contentId = (String) iter.next();
					Content content = (Content) contentManager.loadContent(contentId, true);
					if (null != content) {
						CoordsAttribute coords = this.extractCoordAttribute(content);
						if (coords != null && coords.getX() != 0 && coords.getY() != 0) {
							GeorefInfoBean geoInfoBean = new GeorefInfoBean();
							geoInfoBean.setContentId(contentId);
							geoInfoBean.setX(coords.getX());
							geoInfoBean.setY(coords.getY());
							geoInfoBean.setZ(coords.getZ());
							geoPoints.add(geoInfoBean);

							double currentX = coords.getX();
							double currentY = coords.getY();
							westX = (westX>currentX) ? currentX : westX;
							eastX = (eastX<currentX) ? currentX : eastX;
							northY = (northY<currentY) ? currentY : northY;
							southY = (southY>currentY) ? currentY : southY;
							sumx += currentX;
							sumy += currentY;

						}
					}
				}
				double centerx = sumx/geoPoints.size();
				double centery = sumy/geoPoints.size();
				double[] center = {centerx, centery};
				double xMargin = this.calculateResizeMargin(westX, eastX);
				eastX = this.addPositiveMargin(eastX, xMargin, MAX_EAST);
				westX = this.addNegativeMargin(westX, xMargin, MIN_WEST);
				double yMargin = this.calculateResizeMargin(southY, northY);
				northY = this.addPositiveMargin(northY, yMargin, MAX_NORTH);
				southY = this.addNegativeMargin(southY, yMargin, MIN_SOUTH);
				double[] southWest = {westX, southY};
				double[] northEast = {eastX, northY};
				this.pageContext.setAttribute(this.getMarkerParamName(), geoPoints);
				this.pageContext.setAttribute(this.getCenterCoordsParamName(), center);
				this.pageContext.setAttribute(this.getSouthWestCoordsParamName(), southWest);
				this.pageContext.setAttribute(this.getNorthEastCoordsParamName(), northEast);
			}
		} catch (Throwable e) {
			ApsSystemUtils.logThrowable(e, this, "doStartTag");
			throw new JspException("Error initialization tag", e);
		}
		return super.doStartTag();
	}

	/**
	 * Returns coordinate attribute
	 * @param content jAPS content
	 * @return coordinate attribute
	 */
	private CoordsAttribute extractCoordAttribute(Content content) {
		return (CoordsAttribute) content.getAttributeByRole(GeoRefSystemConstants.ATTRIBUTE_ROLE_COORD);
	}


	/**
	 * Calculation of re-dimensioning of margins.
	 * @param min minimum value
	 * @param max maximum value
	 * @return the margin re-dimensioned.
	 */
	private double calculateResizeMargin(double min, double max) {
		double margin = (max-min)*0.15;
		return margin;
	}

	/**
	 * Add the positive margin to coordinate.
	 * @param current current coordinate value
	 * @param margin coordinate margin value
	 * @param maxValue coordinate maximum value
	 * @return the coordinate considering the positive value.
	 */
	private double addPositiveMargin(double current, double margin, double maxValue) {
		double resizedValue = current + margin;
		if (resizedValue>maxValue) {
			resizedValue = maxValue;
		}
		return resizedValue;
	}

	/**
	 * Adds the negative margin to coordinate.
	 * @param current current coordinate value
	 * @param margin coordinate margin value
	 * @param minValue coordinate minimum value
	 * @return the coordinate considering the negative margin.
	 */
	private double addNegativeMargin(double current, double margin, double minValue) {
		double resizedValue = current - margin;
		if (resizedValue<minValue) {
			resizedValue = minValue;
		}
		return resizedValue;
	}

	/**
	 * Returns master
	 * @return master
	 */
	public String getMaster() {
		return _master;
	}
	/**
	 * Sets master
	 * @param master master
	 */
	public void setMaster(String master) {
		this._master = master;
	}
	/**
	 * Returns center coordinate parameter name
	 * @return center coordinate parameter name
	 */
	public String getCenterCoordsParamName() {
		return _centerCoordsParamName;
	}
	/**
	 * Sets center coordinate parameter name
	 * @param centerCoordsParamName center coordinate parameter name
	 */
	public void setCenterCoordsParamName(String centerCoordsParamName) {
		this._centerCoordsParamName = centerCoordsParamName;
	}
	/**
	 * Returns south west coordinate parameter name
	 * @return south west coordinate parameter name
	 */
	public String getSouthWestCoordsParamName() {
		return _southWestCoordsParamName;
	}
	/**
	 * Sets south west coordinate parameter name
	 * @param southWestCoordsParamName south west coordinate parameter name
	 */
	public void setSouthWestCoordsParamName(String southWestCoordsParamName) {
		this._southWestCoordsParamName = southWestCoordsParamName;
	}
	/**
	 * Returns north east coordinate parameter name
	 * @return north east coordinate parameter name
	 */
	public String getNorthEastCoordsParamName() {
		return _northEastCoordsParamName;
	}
	/**
	 * Sets north east coordinate parameter name
	 * @param northEastCoordsParamName north east coordinate parameter name
	 */
	public void setNorthEastCoordsParamName(String northEastCoordsParamName) {
		this._northEastCoordsParamName = northEastCoordsParamName;
	}
	/**
	 * Returns marker parameter name
	 * @return marker parameter name
	 */
	public String getMarkerParamName() {
		return _markerParamName;
	}
	/**
	 * Sets marker parameter name
	 * @param markerParamName marker parameter name
	 */
	public void setMarkerParamName(String markerParamName) {
		this._markerParamName = markerParamName;
	}

	private String _master;
	private String _centerCoordsParamName;
	private String _southWestCoordsParamName;
	private String _northEastCoordsParamName;
	private String _markerParamName;

	private static final double MAX_NORTH = 90;
	private static final double MIN_SOUTH = -90;
	private static final double MAX_EAST = 180;
	private static final double MIN_WEST = -180;

}