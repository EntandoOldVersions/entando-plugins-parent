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
package com.agiletec.plugins.jpwscontent.aps.system.services.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.MimetypesFileTypeMap;

import org.w3c.dom.NodeList;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.ListAttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.AbstractResourceAttribute;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AbstractResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AttachResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ImageResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jpwscontent.aps.system.services.content.parse.WsContentDOM;
import com.agiletec.plugins.jpwscontent.aps.system.services.resource.IWsResourceManager;
import com.agiletec.plugins.jpwscontent.aps.system.services.resource.WsResource;
import com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.IWsContentManager;
import com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.WsContentEnvelope;
import com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.XmlContentAnalizer;

/**
 * Service that manages the calls from the entrypoint
 */
public class WsContentManager extends ContentManager implements IWsContentManager {

	/**
	 * Create a Content object from an xml string
	 * @param xml the content xml from the envelope object
	 * @return a content objecr
	 * @throws ApsSystemException if an error occurs
	 */
	public Content createContentFromXml(String xml) throws ApsSystemException {
		String typeCode = this.extractContentType(xml);
		return (Content) super.createEntityFromXml(typeCode, xml);
	}

	/**
	 * Retrieve the content type from the xml of a content object
	 * @param xml the string of the content
	 * @return the content type
	 * @throws ApsSystemException if an error occurs
	 */
	private String extractContentType(String xml) throws ApsSystemException {
		WsContentDOM dom = new WsContentDOM(xml);
		return dom.getTypeCode();
	}

	@Override
	public int addContent(WsContentEnvelope envelope) throws ApsSystemException {
		int status = IWsContentManager.RECIVING_NONE;
		Map<String, String> resourceMapping = new HashMap<String, String>();
		Content newContent = null;
		try {
			XmlContentAnalizer xmlAnalizer = new XmlContentAnalizer(envelope.getContent());
			NodeList nodeList = xmlAnalizer.getListResourses("resource");
			if (!this.getWsResourceManager().checkResourcesPresent(envelope, nodeList)){
				throw new ApsSystemException("Resource not found.");
			}
			for (int i=0; i < envelope.getResources().length; i++) {
				WsResource extracted = (WsResource) envelope.getResources()[i];
				String oldId = extracted.getId();
				String verifyIdResources = this.getWsResourceManager().checkResourcesUniqueId(envelope, extracted.getId());
				if (!extracted.getId().equals(verifyIdResources)) {
					extracted.setId(verifyIdResources);
					this.changeIdResourceInTheContent(nodeList, oldId, verifyIdResources);	
				}
				this.getWsResourceManager().addResourceFromWSCall(extracted);
				resourceMapping.put(oldId, verifyIdResources);				
			}
			String xml = xmlAnalizer.getXml();
			newContent = this.createContentFromXml(xml);
			this.addRemoteContent(newContent);
			ApsSystemUtils.getLogger().info("Added new remote content: " + newContent.getId());
			status = IWsContentManager.RECIVING_OK;
		} catch (Throwable t) {
			status = IWsContentManager.RECIVING_FAILURE;
			this.removeContentAndResources(newContent, resourceMapping);
			ApsSystemUtils.logThrowable(t, this, "addContent");
			throw new ApsSystemException("error adding a content", t);
		}
		return status;
	}
	
	private void changeIdResourceInTheContent(NodeList nodeList, String id,	String verifyIdResources) {
		for (int j=0; j < nodeList.getLength(); j++) {
			String idNodeValue = nodeList.item(j).getAttributes().getNamedItem("id").getNodeValue();
			if (idNodeValue.equals(id)) {
				nodeList.item(j).getAttributes().getNamedItem("id").setNodeValue(verifyIdResources);;
			}
		}
	}
	
	private void addRemoteContent(Content newContent) throws ApsSystemException {
		newContent.setId(null);
		this.saveContent(newContent);
		this.insertOnLineContent(newContent);
	}
	
	/**
	 * Delete a content and the related resources.
	 * This method is called only when an error occurs when a new content is being added via webservice
	 * @param remoteContent The content to delete
	 * @param wsResourceManager  The resouce manager
	 * @param associationIdWhithNewId Map containing the resources associations of the content(with the new id)
	 */
	private void removeContentAndResources(Content remoteContent, Map<String, String> associationIdWhithNewId ){
		try {
			if (remoteContent!=null) {
				this.removeOnLineContent(remoteContent);
				this.deleteContent(remoteContent);
			}
			Iterator<String> iter = associationIdWhithNewId.values().iterator();
			while (iter.hasNext()) {
				String resourceId = iter.next();
				ResourceInterface resource = this.getWsResourceManager().loadResource(resourceId);
				if (null != resource) {
					this.getWsResourceManager().deleteResource(resource);
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeContentAndResources");
		}
	}

	@Override
	public WsContentEnvelope getContent(String contentId) throws ApsSystemException {
		WsContentEnvelope envelope = null;
		Logger log = ApsSystemUtils.getLogger();
		if (log.isLoggable(Level.FINE)) {
			log.fine("getContent invoked. contentId=" + contentId);
		}
		try {
			Content content = (Content) this.loadContent(contentId, true);
			if (null != content) {
				envelope = new WsContentEnvelope();
				envelope.setContent(content.getXML());
				this.addContentResources(content, envelope);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContent");
			throw new ApsSystemException("error getting content with id " + contentId);
		}
		return envelope;
	}

	/**
	 * Scan the content looking for resources. 
	 * Found resources are converted in ApsWsResource, collected into a list and this list attached to the ApsWsContentEnvelope
	 * @param content The content to scan
	 * @param envelope The ApsWsContentEnvelope
	 * @throws Throwable if an error occurs
	 */
	private void addContentResources(Content content, WsContentEnvelope envelope) throws Throwable {
		List<AttributeInterface> attributes = content.getAttributeList();
		Iterator<AttributeInterface> it = attributes.iterator();
		List<WsResource> resources = new ArrayList<WsResource>();
		while (it.hasNext()) {
			AttributeInterface currentAttr = it.next();
			if (currentAttr != null && currentAttr instanceof AbstractResourceAttribute) {
				this.addApsWsResourceToList(currentAttr, resources);
			} else if (null != currentAttr && currentAttr instanceof ListAttributeInterface) {
				AbstractListAttribute attr = (AbstractListAttribute)currentAttr; 
				String nestedTypeCode = attr.getNestedAttributeTypeCode();
				if (nestedTypeCode.equals("Attach") || nestedTypeCode.equals("Image"))  {
					List<AttributeInterface> attachAttributes = attr.getAttributes();
					if (null != attachAttributes && !attachAttributes.isEmpty()) {
						Iterator<AttributeInterface> attachIt = attachAttributes.iterator();
						while (attachIt.hasNext()) {
							this.addApsWsResourceToList(attachIt.next(), resources);
						}
					}
				}
			}
		}
		if (!resources.isEmpty()) {
			WsResource[] contentResources =  resources.toArray(new WsResource[resources.size()]);
			envelope.setResources(contentResources);
		}
	}

	protected void addApsWsResourceToList(AttributeInterface currentAttr, List<WsResource> resources) {
		try {
			AbstractResourceAttribute attribute = (AbstractResourceAttribute) currentAttr;
			AbstractResource resource = (AbstractResource) attribute.getResource();
			if (null != resource) {
				WsResource res = new WsResource();
				res.setDescription(resource.getDescr());
				String path = null;
				String filename = null;
				if (resource instanceof AttachResource) {
					filename = ((AttachResource) resource).getInstance().getFileName();
					path = ((AttachResource) resource).getDiskFolder().concat(filename);
				} else if (resource instanceof ImageResource) {
					filename = ((ImageResource) resource).getInstance(0, attribute.getDefaultLangCode()).getFileName();
					path = ((ImageResource) resource).getDiskFolder().concat(filename);
				}
				res.setFileBinary(this.getFileContentsAsBytes(path));
				res.setId(resource.getId());
				res.setNameFile(filename);
				res.setMimeType(this.getMimeType(path));
				res.setTypeCode(currentAttr.getType());
				resources.add(res);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addApsWsResourceToList");
		}
	}
	
	private String getMimeType(String path) {
		File f = new File(path);
		return new MimetypesFileTypeMap().getContentType(f);
	}

	private byte[] getFileContentsAsBytes(String path) throws Throwable {
		byte[] data = null;
		try {
			File f = new File(path);
			data = new byte[(int)f.length()];
			if (f.exists()) {
				InputStream inStream = new FileInputStream(f);
				inStream.read(data);
				inStream.close();
			} else {
				ApsSystemUtils.getLogger().warning("unable to find the file: " + path);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getFileContentsAsBytes");
		}
		return data;
	}

	public void setWsResourceManager(IWsResourceManager wsResourceManager) {
		this._wsResourceManager = wsResourceManager;
	}
	public IWsResourceManager getWsResourceManager() {
		return _wsResourceManager;
	}

	private IWsResourceManager _wsResourceManager;
}
