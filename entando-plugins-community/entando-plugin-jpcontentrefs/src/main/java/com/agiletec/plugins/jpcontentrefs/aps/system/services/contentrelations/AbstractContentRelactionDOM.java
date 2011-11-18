/*
*
* Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
* This file is part of JAPS software.
* JAPS and its  source-code is  licensed under the  terms of the
* GNU General Public License  as published by  the Free Software
* Foundation (http://www.fsf.org/licensing/licenses/gpl.txt).
* 
* You may copy, adapt, and redistribute this file for commercial
* or non-commercial use.
* When copying,  adapting,  or redistributing  this document you
* are required to provide proper attribution  to AgileTec, using
* the following attribution line:
* Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
*/
package com.agiletec.plugins.jpcontentrefs.aps.system.services.contentrelations;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Classe JDOM per la lettura e scrittura dell'xml di relazione 
 * dei tipi contenuto con elementi associabili.
 * Esempio per categorie:
<contenttypes>
	<contenttype typecode="MEN">
		<category code="cat1"/>
		<category code="cat2"/>
	</contenttype>
	<contenttype typecode="EVN">
		<category code="cat1"/>
	</contenttype>
</contenttypes>
 * @version 1.0
 * @author E.Santoboni
 */
public abstract class AbstractContentRelactionDOM {
	
	public AbstractContentRelactionDOM() throws ApsSystemException {}
	
	public AbstractContentRelactionDOM(String xmlText) throws ApsSystemException {
		this.decodeDOM(xmlText);
	}
	
	private void decodeDOM(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		try {
			_doc = builder.build(reader);
		} catch (Throwable t) {
			ApsSystemUtils.getLogger().severe("Errore nel parsing: " + t.getMessage());
			throw new ApsSystemException("Errore nel parsing: " + t.getMessage(), t);
		}
	}
	
	public Map<String, List<String>> getContentTypes() {
		Map<String, List<String>> contentTypesMap = new HashMap<String, List<String>>();
		List contentTypeElements = this.getDocument().getRootElement().getChildren("contenttype");
		if (null != contentTypeElements && contentTypeElements.size() > 0) {
			Iterator contentTypeElementsIter = contentTypeElements.iterator();
			while (contentTypeElementsIter.hasNext()) {
				Element contentTypeElement = (Element) contentTypeElementsIter.next();
				this.addReferenciesForType(contentTypesMap, contentTypeElement);
			}
		}
		return contentTypesMap;
	}
	
	private void addReferenciesForType(Map<String, List<String>> contentTypesMap, Element contentTypeElement) {
		String contentTypeCode = contentTypeElement.getAttributeValue("typecode");
		List<String> referencesForType = new ArrayList<String>();
		List referenceElements = contentTypeElement.getChildren(this.getReferenceElementName());
		if (null != referenceElements && referenceElements.size() > 0) {
			Iterator referenceElementsIter = referenceElements.iterator();
			while (referenceElementsIter.hasNext()) {
				Element categoryElement = (Element) referenceElementsIter.next();
				String code = categoryElement.getAttributeValue("code");
				referencesForType.add(code);
			}
		}
		contentTypesMap.put(contentTypeCode, referencesForType);
	}
	
	public void setContent(Map<String, List<String>> contentTypeCategories) {
		this.initDocument();
		Element elementRoot = new Element("contenttypes");
		this.getDocument().setRootElement(elementRoot);
		Iterator<String> contentTypeCodesIter = contentTypeCategories.keySet().iterator();
		while (contentTypeCodesIter.hasNext()) {
			String contentTypeCode = (String) contentTypeCodesIter.next();
			if (null != contentTypeCategories.get(contentTypeCode) && 
					!((List<String>)contentTypeCategories.get(contentTypeCode)).isEmpty()) {
				Element contentElement = new Element("contenttype");
				contentElement.setAttribute("typecode", contentTypeCode);
				List<String> codes = (List<String>)contentTypeCategories.get(contentTypeCode);
				for (int i=0; i<codes.size(); i++) {
					String code = codes.get(i);
					Element refElement = new Element(this.getReferenceElementName());
					refElement.setAttribute("code", code);
					contentElement.addContent(refElement);
				}
				elementRoot.addContent(contentElement);
			}
		}
	}
	
	protected abstract String getReferenceElementName();

	protected Document getDocument() {
		return this._doc;
	}
	
	public String getXMLDocument(){
		XMLOutputter out = new XMLOutputter();
		Format format = Format.getPrettyFormat();
		out.setFormat(format);
		String xml = out.outputString(_doc);
		return xml;
	}
	
	public void initDocument() {
		this._doc = new Document();
	}
	
	private Document _doc;
	
}
