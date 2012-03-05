/*
*
* Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwscontent.aps.system.services.wscontent;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * JDOM Helper class uset to parse the content xml
 */
public class XmlContentAnalizer {
	
	/**
	 * Main constructor
	 * @param xml the content in xml format
	 * @throws ParserConfigurationException 
	 * @throws SAXException
	 * @throws IOException
	 */
	public XmlContentAnalizer(String xml) throws ParserConfigurationException, SAXException, IOException{
		this.init(xml);
	}
	
	/**
	 * Builds a {@link Document} from the xml string
	 * @param xml the content in xml format
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private void init(String xml) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbf.newDocumentBuilder();
		InputStream in = new java.io.ByteArrayInputStream(xml.getBytes("UTF-8"));
		_document = builder.parse(in);
	}
	
	/**
	 * Returns a {@link NodeList} object, with it's childrens
	 * Restituisce il nodo avente il nome specificato, con tutti i suoi figli.
	 * @param tagName The name of the node to search
	 * @return a {@link NodeList} object, with it's childrens
	 */
	public NodeList getListResourses(String tagName) {
		NodeList nlChilds = _document.getElementsByTagName(tagName);
		return nlChilds;
	}
	
	/**
	 * Update the value of the attribute
	 * @param node The node containing the attribute
	 * @param attribute the name of the attribute to refresh
	 * @param value the new value for the attribute
	 * @return true if the update process success
	 */
	public boolean updateID(Node node, String attribute, String value) {
		try {
			node.getAttributes().getNamedItem(attribute).setNodeValue(value);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Return the xml string of the document.
	 * @return the xml string of the document 
	 * @throws TransformerException if an error occurs
	 */
	public String getXml() throws TransformerException {
		TransformerFactory tFactory =TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();
        transformer.setOutputProperty("indent", "yes");
        DOMSource source = new DOMSource(_document);
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        transformer.transform(source, result); 
        return sw.toString();
	}
	
	private Document _document;

}