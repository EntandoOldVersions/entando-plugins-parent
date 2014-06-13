/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpmyportalplus.aps.system.services.pagemodel;

import java.io.StringReader;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author E.Santoboni
 */
public class MyPortalPageModelDOM {
	
	private static final Logger _logger = LoggerFactory.getLogger(MyPortalPageModelDOM.class);
	
	public MyPortalPageModelDOM(String xmlText) throws ApsSystemException {
		this.decodeDOM(xmlText);
		this.buildFrames();
	}

	private void decodeDOM(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		try {
			_doc = builder.build(reader);
		} catch (Throwable t) {
			_logger.error("Error detected parsing the XML: {}", xmlText, t);
			throw new ApsSystemException("Error detected parsing the XML", t);
		}
	}
	
	private void buildFrames() throws ApsSystemException {
		List<Element> frameElements = _doc.getRootElement().getChildren(TAB_FRAME);
		if (null != frameElements && frameElements.size() > 0) {
			for (int i = 0; i < frameElements.size(); i++) {
				Element element = frameElements.get(i);
				this.buildFrame(element);
			}
		}
	}

	private void buildFrame(Element frameElement) throws ApsSystemException {
		int pos = Integer.parseInt(frameElement.getAttributeValue(ATTRIBUTE_POS));
		MyPortalFrameConfig frameConfig = new MyPortalFrameConfig();
		String fixed = frameElement.getAttributeValue("locked");
		if (null == fixed || fixed.equals("true")) {
			//frame.setLocked(true);
			frameConfig.setLocked(true);
		}
		String column = frameElement.getAttributeValue("column");
		if (null != column) {
			try {
				frameConfig.setColumn(Integer.parseInt(column));
			} catch (Throwable t) {
				//nothing to do
			}
		}
		this.getModelConfig().put(pos, frameConfig);
	}
	
	private Document _doc;
	private final String TAB_FRAME = "frame";
	private final String ATTRIBUTE_POS = "pos";
	
	public Map<Integer, MyPortalFrameConfig> getModelConfig() {
		return _modelConfig;
	}
	
	private Map<Integer, MyPortalFrameConfig> _modelConfig = new HashMap<Integer, MyPortalFrameConfig>();

}