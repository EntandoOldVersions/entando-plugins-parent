/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
* Entando is a free software; 
* You can redistribute it and/or modify it
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
package com.agiletec.plugins.jpcalendar.aps.system.services.calendar;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.io.StringReader;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Support Class for XML parsing of jpcalendar plugin
 * configuration.
 * 
 * <calendarConfig>
 * 	  <contentType code="EVN" />
 * 	  <dateAttributes>
 * 		<start name="Data" />
 * 		<end name="Data" />
 * 	  </dateAttributes>
 * </calendarConfig>
 * 
 * @author E.Santoboni
 */
public class CalendarConfigDOM {
	
	protected CalendarConfigDOM() {}
	
	protected CalendarConfigDOM(String xmlText) throws ApsSystemException {
		this.decodeDOM(xmlText);
	}
	
	public String createConfigXml(CalendarConfig config) {
		Element root = this.createConfigElement(config);
		Document doc = new Document(root);
		XMLOutputter out = new XMLOutputter();
		Format format = Format.getPrettyFormat();
		format.setIndent("\t");
		out.setFormat(format);
		return out.outputString(doc);
	}
	
	private Element createConfigElement(CalendarConfig config) {
		Element configElem = new Element("calendarConfig");
		if (null != config.getContentTypeCode()) {
			Element contentTypeElement = new Element("contentType");
			contentTypeElement.setAttribute("code", config.getContentTypeCode());
			configElem.addContent(contentTypeElement);
		}
		Element dateAttributesElement = new Element("dateAttributes");
		configElem.addContent(dateAttributesElement);
		this.addDateAttributeElement(dateAttributesElement, "start", config.getStartAttributeName());
		this.addDateAttributeElement(dateAttributesElement, "end", config.getEndAttributeName());
		return configElem;
	}
	
	private void addDateAttributeElement(Element dateAttributesElement, String elementName, String value) {
		if (null != value) {
			Element startElement = new Element(elementName);
			startElement.setAttribute("name",  value);
			dateAttributesElement.addContent(startElement);
		}
	}
	
	public CalendarConfig extractConfig() throws ApsSystemException {
		CalendarConfig config = new CalendarConfig();
		try {
			Element rootElement = this._doc.getRootElement();
			Element contentTypeElem = rootElement.getChild("contentType");
			if (null != contentTypeElem) {
				config.setContentTypeCode(contentTypeElem.getAttributeValue("code"));
			}
			Element dateAttributeElem = rootElement.getChild("dateAttributes");
			if (null != dateAttributeElem) {
				Element startDateAttributeElem = dateAttributeElem.getChild("start");
				if (null != startDateAttributeElem) {
					config.setStartAttributeName(startDateAttributeElem.getAttributeValue("name"));
				}
				Element endDateAttributeElem = dateAttributeElem.getChild("end");
				if (null != endDateAttributeElem) {
					config.setEndAttributeName(endDateAttributeElem.getAttributeValue("name"));
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "extractConfig", "Error extracting config");
			throw new ApsSystemException("Error parsing extracting", t);
		}
		return config;
	}
	
	private void decodeDOM(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		try {
			this._doc = builder.build(reader);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "decodeDOM", "Error parsing config: " + t.getMessage());
			throw new ApsSystemException("Error parsing config", t);
		}
	}
	
	private Document _doc;
	
}