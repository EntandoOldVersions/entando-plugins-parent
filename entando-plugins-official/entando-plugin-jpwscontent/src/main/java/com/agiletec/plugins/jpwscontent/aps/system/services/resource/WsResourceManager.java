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
package com.agiletec.plugins.jpwscontent.aps.system.services.resource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.w3c.dom.NodeList;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AbstractMonoInstanceResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AbstractMultiInstanceResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.WsContentEnvelope;
/**
 * Service that handles the resources inside the webservice calls
 *
 */
public class WsResourceManager extends com.agiletec.plugins.jacms.aps.system.services.resource.ResourceManager implements IWsResourceManager {

	@Override
	public void init() throws Exception {
		ApsSystemUtils.getLogger().config(this.getClass().getName() + ": ready");
	}
	
	public ResourceInterface addResource(ResourceDataBean bean) throws ApsSystemException {
    	ResourceInterface resource = this.createResourceType(bean.getResourceType());
    	try {
    		resource.setDescr(bean.getDescr());
    		resource.setMainGroup(bean.getMainGroup());
    		resource.setCategories(bean.getCategories());
    		resource.saveResourceInstances(bean);
    		if (bean instanceof WsResourceBean) {
    			resource.setId(((WsResourceBean) bean).getId());
    		}
    		this.addResource(resource);
    	} catch (Throwable t) {
    		resource.deleteResourceInstances();
			throw new ApsSystemException("Error saving the resource", t);
    	}
    	return resource;
    }
	
	public void addResource(ResourceInterface resource) throws ApsSystemException {
    	try {
    		String id = resource.getId();
    		if ( id == null || id.trim().length() == 0 || this.loadResource(id) != null) {
    			int newId = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
        		resource.setId(String.valueOf(newId));
    		}
    		this.getResourceDAO().addResource(resource);
    	} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addResource");
			throw new ApsSystemException("Errore in salvataggio risorsa", t);
    	}
    }
	
	public ResourceInterface addResourceFromWSCall(WsResource resource) throws ApsSystemException {
		ResourceInterface resourceInterface = null;
		ByteArrayInputStream inputStream = new ByteArrayInputStream(resource.getFileBinary());
		try {
			WsResourceBean resourceBean = new WsResourceBean();
			resourceBean.setInputStream(inputStream);
			resourceBean.setFileSize(resource.getFileBinary().length);
			resourceBean.setCategories(new ArrayList());
			resourceBean.setMainGroup(Group.FREE_GROUP_NAME);
			resourceBean.setDescr(resource.getDescription());
			resourceBean.setResourceType(resource.getTypeCode());
			resourceBean.setId(resource.getId());
			resourceBean.setMimeType(resource.getMimeType());
			this.verifyNameFile(resource);
			
			String fileName = resource.getNameFile();
			resourceBean.setFileName(fileName);
			resourceInterface = this.addResource(resourceBean);
			ApsSystemUtils.getLogger().info("Adding remote resource " + resourceInterface.getId());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addResourceFromWSCall");
			throw new ApsSystemException("Error loading a resource", t);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				throw new ApsSystemException("Error closing the inputstream", e);
			}
		}
		return resourceInterface;
	}
	
	/**
	 * Checks into the system resources for files with the same filename.
	 * If duplicates are found, the ApsWsResource filename will be renamed
	 * @param wsResource The resource to check
	 */
	private void verifyNameFile(WsResource wsResource) {
		this.verifyNameFile(wsResource, false, 0);
	}

	/**
	 * Checks into the system resources for files with the same filename.
	 * If duplicates are found, the ApsWsResource filename will be renamed
	 * @param wsResource the resource to check
	 * @param isRemane boolean. the resource is alreadt been renamed
	 * @param value the rename value
	 */
	private void verifyNameFile(WsResource wsResource, boolean isRemane, int value) {
		value++;
		ResourceInterface resourcePrototype = this.createResourceType(wsResource.getTypeCode());
		boolean isDuplicated = this.checkDuplicateFile(resourcePrototype, wsResource);
		if (isDuplicated) {
			String fileName = null;
			if (isRemane) {
				fileName = wsResource.getNameFile().substring(0, (wsResource.getNameFile().indexOf(".") - String.valueOf(value).length()));
			} else {
				fileName = wsResource.getNameFile().substring(0, wsResource.getNameFile().indexOf("."));
			}
			fileName = fileName	+ value	+ wsResource.getNameFile().substring(wsResource.getNameFile().indexOf("."));
			wsResource.setNameFile(fileName);
			verifyNameFile(wsResource, true, value);
		}
	}

	/**
	 * Check if the file is already present in the filesystem
	 * @param resourcePrototype the resource prototype
	 * @param wsResource The resource to check
	 * @return True if the file is already present
	 */
	private boolean checkDuplicateFile(ResourceInterface resourcePrototype,	WsResource wsResource) {
		resourcePrototype.setMainGroup(Group.FREE_GROUP_NAME);
		String baseDiskFolder = resourcePrototype.getDiskFolder();
		String fileName = null;
		if (resourcePrototype.isMultiInstance()) {
			fileName = ((AbstractMultiInstanceResource) resourcePrototype).getInstanceFileName(wsResource.getNameFile(), 0, null);
		} else {
			fileName = ((AbstractMonoInstanceResource) resourcePrototype).getInstanceFileName(wsResource.getNameFile());
		}
		if ((new File(baseDiskFolder + fileName)).exists()) {
			return true;
		}
		return false;
	}

	/**
	 * Delete a resource.
	 * @param idResource the id of the resource to delete
	 * @throws ApsSystemException if an error occurs
	 * @throws SQLException if an error occurs
	 */
	private void removeResource(String idResource) throws ApsSystemException {
		ResourceInterface resource = this.loadResource(idResource);
		if (null != resource) {
			resource.deleteResourceInstances();
			this.deleteResource(resource);
			ApsSystemUtils.getLogger().info("Deleted resource " + resource.getId());
		}
	}

	public String checkResourcesUniqueId(WsContentEnvelope envelope, String id) throws ApsSystemException {
		if (this.existId(id)) {
			id = String.valueOf(this.getKeyGeneratorManager().getUniqueKeyCurrentValue());
			if (this.exitIdResourcesInContent(envelope, id)) {
				this.checkResourcesUniqueId(envelope, id);
			}
		}
		return id;
	}

	/**
	 * Check if a resource is present
	 * @param id the id of the resource to search
	 * @return True if the resource exists
	 * @throws ApsSystemException if an error occurs
	 */
	private boolean existId(String id) throws ApsSystemException {
		boolean existId = (null != this.loadResource(id));
		return existId;
	}
	
	public WsResource getResourceFromEnvelope(WsContentEnvelope envelope, String id) throws ApsSystemException {
		WsResource[] resources = envelope.getResources();
		WsResource resource = null;
		for (int i = 0; i < resources.length; i++) {
			resource = resources[i];
			if (resource.getId().equals(id)) {
				return resource;
			}
		}
		return null;
	}

	/**
	 * Check if the id is present in the envelope
	 * @param envelope
	 * @param id id to check
	 * @return true if the resource id is present.
	 */
	private boolean exitIdResourcesInContent(WsContentEnvelope envelope,	String id) {
		WsResource[] resources = envelope.getResources();
		for (int i = 0; i < resources.length; i++) {
			if (resources[i].getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	public boolean checkResourcesPresent(WsContentEnvelope envelope,	NodeList nodeList) throws ApsSystemException {
		String idValue = null;
		for (int j = 0; j < nodeList.getLength(); j++) {
			idValue = nodeList.item(j).getAttributes().getNamedItem("id").getNodeValue();
			if (!exitIdResourcesInContent(envelope, idValue)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Delete the resources stored in the parameter
	 * @param associazioni map containing the ids of the resource to delete
	 * @throws ApsSystemException if an error occurs
	 * @throws SQLException if an error occurs
	 */
	public void deleteResources(Map associazioni) throws ApsSystemException, SQLException {
		Iterator it = associazioni.values().iterator();
		for (int k = 0; k < associazioni.size(); k++) {
			String idRes = (String) it.next();
			removeResource(idRes);
		}
	}

	
	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}
	
	private IKeyGeneratorManager _keyGeneratorManager;
	
}
