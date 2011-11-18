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
package com.agiletec.plugins.jpcalendar.aps.system.services.calendar;

import java.io.StringReader;
import java.util.logging.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;

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
	
	protected CalendarConfigDOM(String xmlText) throws ApsSystemException {
		_log = ApsSystemUtils.getLogger();
		this.decodeDOM(xmlText);
	}
	
	protected String getManageContentType() {
		Element contenTypeElem = this._doc.getRootElement().getChild("contentType");
		String contenType = contenTypeElem.getAttributeValue("code");
		return contenType;
	}
	
	protected String getManageStartAttribute() {
		return this.getManageAttribute("start");
	}
	
	protected String getManageEndAttribute() {
		return this.getManageAttribute("end");
	}
	
	private String getManageAttribute(String dateElementName) {
		Element dateAttributeElem = this._doc.getRootElement().getChild("dateAttributes").getChild(dateElementName);
		String dateAttributeName = dateAttributeElem.getAttributeValue("name");
		return dateAttributeName;
	}
	
	private void decodeDOM(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		try {
			_doc = builder.build(reader);
		} catch (Throwable t) {
			_log.severe("Errore nel parsing: " + t.getMessage());
			throw new ApsSystemException("Errore nell'interpretazione dell'xml", t);
		}
	}
	
	private Document _doc;
	private Logger _log;

}
