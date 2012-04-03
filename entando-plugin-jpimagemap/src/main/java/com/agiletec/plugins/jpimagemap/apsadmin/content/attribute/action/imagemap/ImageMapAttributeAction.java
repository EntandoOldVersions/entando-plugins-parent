package com.agiletec.plugins.jpimagemap.apsadmin.content.attribute.action.imagemap;

import java.util.logging.Logger;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.plugins.jacms.apsadmin.content.AbstractContentAction;
import com.agiletec.plugins.jpimagemap.aps.system.services.content.model.attribute.ImageMapAttribute;
import com.agiletec.plugins.jpimagemap.aps.system.services.content.model.attribute.util.LinkedArea;

public class ImageMapAttributeAction extends AbstractContentAction implements IImageMapAttributeAction {
	
	@Override
	public String removeImage() {
		this.getContentActionHelper().updateEntity(this.getContent(), this.getRequest());
		Logger log = ApsSystemUtils.getLogger();
		try {
			ImageMapAttribute currentAttribute = this.getAttribute();
			currentAttribute.getImage().getResources().clear();
			currentAttribute.getImage().getTextMap().clear();
			currentAttribute.getAreas().clear();
			log.fine("Removed Image element from ImageMap " + currentAttribute.getName());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeImage");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String addArea() {
		this.getContentActionHelper().updateEntity(this.getContent(), this.getRequest());
		Logger log = ApsSystemUtils.getLogger();
		try {
			ImageMapAttribute currentAttribute = this.getAttribute();
			currentAttribute.addArea();
			log.fine("Added LinkedArea element to ImageMap " + currentAttribute.getName());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addArea");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String removeArea() {
		this.getContentActionHelper().updateEntity(this.getContent(), this.getRequest());
		Logger log = ApsSystemUtils.getLogger();
		try {
			int elementIndex = this.getElementIndex();
			ImageMapAttribute currentAttribute = this.getAttribute();
			if (elementIndex>=0 && elementIndex<currentAttribute.getAreas().size()) {
				currentAttribute.removeArea(elementIndex);
				log.fine("Removed LinkedArea element from ImageMap " + currentAttribute.getName());
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeArea");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String defineArea() {
		this.getContentActionHelper().updateEntity(this.getContent(), this.getRequest());
		return SUCCESS;
	}
	
	@Override
	public String saveArea() {
		Logger log = ApsSystemUtils.getLogger();
		try {
			ImageMapAttribute currentAttribute = this.getAttribute();
			LinkedArea area = currentAttribute.getArea(this.getElementIndex());
			area.setCoords(this.concatCoords());
			log.fine("SAVED Area " + this.getElementIndex() + " for ImageMap " + currentAttribute.getName());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "saveArea");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected String concatCoords() {
		// FIXME Campi String (invece di int) per problema in jsp
		int left = (int) Float.parseFloat(this.getLeft());
		int top = (int) Float.parseFloat(this.getTop());
		int right = left + (int) Float.parseFloat(this.getWidth());
		int down = top + (int) Float.parseFloat(this.getHeight());
		String coords = left + "," + top + "," + right + "," + down;
		return coords;
	}
	
	public ImageMapAttribute getAttribute() {
		if (this._attribute==null) {
			this._attribute = (ImageMapAttribute) this.getContent().getAttribute(this.getAttributeName());
		}
		return _attribute;
	}
	
	public String getAttributeName() {
		return _attributeName;
	}
	public void setAttributeName(String attributeName) {
		this._attributeName = attributeName;
	}
	
	public int getElementIndex() {
		return _elementIndex;
	}
	public void setElementIndex(int elementIndex) {
		this._elementIndex = elementIndex;
	}
	
	public String getLangCode() {
		return _langCode;
	}
	public void setLangCode(String resourceLangCode) {
		this._langCode = resourceLangCode;
	}
	
	public String getLeft() {
		return _left;
	}
	public void setLeft(String left) {
		this._left = left;
	}
	
	public String getTop() {
		return _top;
	}
	public void setTop(String top) {
		this._top = top;
	}
	
	public String getWidth() {
		return _width;
	}
	public void setWidth(String width) {
		this._width = width;
	}
	
	public String getHeight() {
		return _height;
	}
	public void setHeight(String height) {
		this._height = height;
	}
	
	private ImageMapAttribute _attribute;
	
	private String _attributeName;
	private int _elementIndex;
	private String _langCode;
	
	private String _left;
	private String _top;
	private String _width;
	private String _height;
	
}