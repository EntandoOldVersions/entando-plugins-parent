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
package com.agiletec.plugins.jpwscontent.aps.system.services.content.parse;

import java.io.StringReader;

import org.jdom.input.SAXBuilder;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.parse.ContentDOM;

/**
 * Extension of {@link ContentDOM} Class.
*
*/
public class WsContentDOM extends ContentDOM {
	
	public WsContentDOM() {}
	
	/**
	 * Default constructor.
	 * @param xmlText The string to parse.
	 * @throws ApsSystemException If an error occurs parsing the xml.
	 */
	public WsContentDOM(String xmlText) throws ApsSystemException {
		this.decodeDOM(xmlText);
	}
	
	private void decodeDOM(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		try {
			_doc = builder.build(reader);
			this._root = this._doc.getRootElement();
		} catch (Throwable t) {
			ApsSystemUtils.getLogger().severe("Error parsing: " + t.getMessage());
			throw new ApsSystemException("Error parsing xml:", t);
		}
	}
	
	/**
	 * Extract the typeCode from the jdom document.
	 * @return the typecode of the content
	 */
	public String getTypeCode() {
		return this._root.getAttributeValue("typecode");
	}
	
}
