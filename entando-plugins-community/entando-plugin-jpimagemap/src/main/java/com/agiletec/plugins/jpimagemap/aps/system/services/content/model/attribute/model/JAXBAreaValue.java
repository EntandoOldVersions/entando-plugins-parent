package com.agiletec.plugins.jpimagemap.aps.system.services.content.model.attribute.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.JAXBLinkValue;

@XmlRootElement(name = "area")
@XmlType(propOrder = {"coords", "link"})
@XmlSeeAlso({JAXBLinkValue.class})
public class JAXBAreaValue {
	
	@XmlElement(name = "coords", required = true)
	public String getCoords() {
		return coords;
	}
	public void setCoords(String coords) {
		this.coords = coords;
	}
	
	@XmlElement(name = "link", required = true)
	public JAXBLinkValue getLink() {
		return _link;
	}
	public void setLink(JAXBLinkValue link) {
		this._link = link;
	}
	
	private String coords;
	private JAXBLinkValue _link;
	
}
