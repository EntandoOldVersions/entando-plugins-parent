package com.agiletec.plugins.jpimagemap.aps.system.services.content.model.attribute.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.JAXBLinkValue;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.JAXBResourceValue;

@XmlRootElement(name = "imagemap")
@XmlType(propOrder = {"image", "areas"})
@XmlSeeAlso({ArrayList.class, JAXBAreaValue.class, JAXBResourceValue.class, JAXBLinkValue.class})
public class JAXBImageMapValue {
	
	@XmlElement(name = "image", required = true)
	public JAXBResourceValue getImage() {
		return _image;
	}
	public void setImage(JAXBResourceValue image) {
		this._image = image;
	}
	
	@XmlElement(name = "area", required = true)
	@XmlElementWrapper(name = "areas")
	public List<JAXBAreaValue> getAreas() {
		return _areas;
	}
	public void setAreas(List<JAXBAreaValue> areas) {
		this._areas = areas;
	}
	public void addArea(JAXBAreaValue area) {
		this.getAreas().add(area);
	}
	
	private JAXBResourceValue _image;
	private List<JAXBAreaValue> _areas = new ArrayList<JAXBAreaValue>();
	
}
