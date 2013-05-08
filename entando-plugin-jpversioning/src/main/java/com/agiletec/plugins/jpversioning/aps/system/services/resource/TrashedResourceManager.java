/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpversioning.aps.system.services.resource;

import java.io.File;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.xml.sax.InputSource;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceDAO;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AbstractMonoInstanceResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AbstractMultiInstanceResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInstance;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceRecordVO;
import com.agiletec.plugins.jacms.aps.system.services.resource.parse.ResourceHandler;
import com.agiletec.plugins.jpversioning.aps.system.JpversioningSystemConstants;
import java.io.InputStream;
import java.util.ArrayList;
import org.entando.entando.aps.system.services.storage.IStorageManager;

/**
 * Manager of trashed resources.
 * @author G.Cocco, E.Mezzano
 */
@Aspect
public class TrashedResourceManager extends AbstractService implements ITrashedResourceManager {
	
	@Override
	public void init() throws Exception {
		this.checkTrashedResourceDiskFolder(this.getResourceTrashRootDiskSubFolder());
		ApsSystemUtils.getLogger().config(this.getClass().getName() + ": initialized ");
		ApsSystemUtils.getLogger().config("Folder trashed resources: " + this.getResourceTrashRootDiskSubFolder());
	}
	
	@Before("execution(* com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager.deleteResource(..)) && args(resource)")
	public void onDeleteResource(ResourceInterface resource) throws ApsSystemException {
		this.addTrashedResource(resource);
	}
	
	@Override
	public List<String> searchTrashedResourceIds(String resourceTypeCode, String text, List<String> allowedGroups) throws ApsSystemException {
		List<String> resources = null;
    	try {
    		resources = this.getTrashedResourceDAO().searchTrashedResourceIds(resourceTypeCode, text, allowedGroups);
    	} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "searchTrashedResourceIds");
			throw new ApsSystemException("Error while extracting trashed resources", t);
    	}
    	return resources;
	}
	
	@Override
	public ResourceInterface loadTrashedResource(String id) throws ApsSystemException{
		ResourceInterface resource = null;
		try {
			ResourceRecordVO resourceVo = this.getTrashedResourceDAO().getTrashedResource(id);
			if (null != resourceVo) {
				resource = this.createResource(resourceVo);
				ApsSystemUtils.getLogger().info("loaded trashed resource " + id);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "loadTrashedResource");
			throw new ApsSystemException("Error while loading trashed resource", t);
		}
		return resource;
	}
	
	@Override
	public void restoreResource(String resourceId) throws ApsSystemException {
		ResourceInterface resource = this.loadTrashedResource(resourceId);
		if (null != resource) {
			try {
				boolean isProtected = !Group.FREE_GROUP_NAME.equals(resource.getMainGroup());
				String folder = this.getSubfolder(resource);
				String folderDest = resource.getFolder();
				if (resource.isMultiInstance()) {
					AbstractMultiInstanceResource multiResource = (AbstractMultiInstanceResource) resource;
					Map<String, ResourceInstance> instancesMap = multiResource.getInstances();
					Iterator<ResourceInstance> iter = instancesMap.values().iterator();
					while (iter.hasNext()) {
						ResourceInstance resourceInstance = iter.next();
						String path = folder + resourceInstance.getFileName();
						System.out.println("SORGENTE " + path);
						InputStream is = this.getStorageManager().getStream(path, true);
						String pathDest = folderDest + resourceInstance.getFileName();
						System.out.println("DESTINAZIONE " + pathDest);
						this.getStorageManager().saveFile(pathDest, isProtected, is);
					}
				} else {
					AbstractMonoInstanceResource monoResource = (AbstractMonoInstanceResource) resource;
					ResourceInstance resourceInstance = monoResource.getInstance();
					String path = folder + resourceInstance.getFileName();
					System.out.println("SORGENTE " + path);
					InputStream is = this.getStorageManager().getStream(path, true);
					String pathDest = folderDest + resourceInstance.getFileName();
					System.out.println("DESTINAZIONE " + pathDest);
					this.getStorageManager().saveFile(pathDest, isProtected, is);
				}
				//Map<String, String> filesPathDestination = this.resourceInstancesArchiveFilePaths(resource);
				//Map<String, String> filesPathSource = this.resourceInstancesTrashFilePaths(resource);
				//String filePath = filesPathDestination.values().iterator().next();
				//String destionationDirPath = filePath.substring(0, filePath.lastIndexOf(File.separator));
				//this.checkTrashedResourceDiskFolder(destionationDirPath);		
				//this.moveResourcesIstances(filesPathSource, filesPathDestination);
	    		this.getResourceDAO().addResource(resource);
				this.removeFromTrash(resource);
				//this.deleteInstancesFromTrash(filesPathSource.values());
				//this.getTrashedResourceDAO().delTrashedResource(resourceId);
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "restoreResource", "Error on restoring trashed resource");
				throw new ApsSystemException("Error on restoring trashed resource", t);
			}
		}
	}
	
	@Override
	public void removeFromTrash(String resourceId) throws ApsSystemException {
		try {
			ResourceRecordVO resourceVo = this.getTrashedResourceDAO().getTrashedResource(resourceId);
			if (null != resourceVo) {
				ResourceInterface resource = this.createResource(resourceVo);
				this.removeFromTrash(resource);
			}
		} catch (Throwable t) {
    		ApsSystemUtils.logThrowable(t, this, "removeFromTrash");
    		throw new ApsSystemException("Error removing Trashed Resource", t);
		}
	}
	
	protected void removeFromTrash(ResourceInterface resource) throws ApsSystemException {
		try {
			//ResourceRecordVO resourceVo = this.getTrashedResourceDAO().getTrashedResource(resourceId);
			//if (null != resourceVo) {
				//ResourceInterface resource = this.createResource(resourceVo);
			String folder = this.getSubfolder(resource);
			if (resource.isMultiInstance()) {
				AbstractMultiInstanceResource multiResource = (AbstractMultiInstanceResource) resource;
				Map<String, ResourceInstance> instancesMap = multiResource.getInstances();
				Iterator<ResourceInstance> iter = instancesMap.values().iterator();
				while (iter.hasNext()) {
					ResourceInstance resourceInstance = iter.next();
					String path = folder + resourceInstance.getFileName();
					this.getStorageManager().deleteFile(path, true);
				}
			} else {
				AbstractMonoInstanceResource monoResource = (AbstractMonoInstanceResource) resource;
				ResourceInstance resourceInstance = monoResource.getInstance();
				String path = folder + resourceInstance.getFileName();
				this.getStorageManager().deleteFile(path, true);
			}
			//}
			this.getTrashedResourceDAO().delTrashedResource(resource.getId());
		} catch (Throwable t) {
    		ApsSystemUtils.logThrowable(t, this, "removeFromTrash");
    		throw new ApsSystemException("Error removing Trashed Resource", t);
		}
	}
	
	@Override
	public void addTrashedResource(ResourceInterface resource) throws ApsSystemException {
		String folder = this.getSubfolder(resource);
		List<String> paths = new ArrayList<String>();
		try {
			if (resource.isMultiInstance()) {
				AbstractMultiInstanceResource multiResource = (AbstractMultiInstanceResource) resource;
				Map<String, ResourceInstance> instancesMap = multiResource.getInstances();
				Iterator<ResourceInstance> iter = instancesMap.values().iterator();
				while (iter.hasNext()) {
					ResourceInstance resourceInstance = iter.next();
					InputStream is = resource.getResourceStream(resourceInstance);
					String path = folder + resourceInstance.getFileName();
					paths.add(path);
					this.getStorageManager().saveFile(path, true, is);
				}
			} else {
				AbstractMonoInstanceResource monoResource = (AbstractMonoInstanceResource) resource;
				ResourceInstance resourceInstance = monoResource.getInstance();
				InputStream is = resource.getResourceStream(resourceInstance);
				String path = folder + resourceInstance.getFileName();
				paths.add(path);
				this.getStorageManager().saveFile(path, true, is);
			}
			this.getTrashedResourceDAO().addTrashedResource(resource);
		} catch (Throwable t) {
			t.printStackTrace();
			for (int i = 0; i < paths.size(); i++) {
				String path = paths.get(i);
				this.getStorageManager().deleteFile(path, true);
			}
    		ApsSystemUtils.logThrowable(t, this, "addTrashedResource");
    		throw new ApsSystemException("Error adding Trashed Resource", t);
    	}
	}
	
	/**
	 * Verifica l'esistenza della directory di destinazione dei file
	 */
	private void checkTrashedResourceDiskFolder(String dirPath) {
		try {
			boolean exist = this.getStorageManager().exists(dirPath, true);
			if (!exist) {
				this.getStorageManager().createDirectory(dirPath, true);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "checkTrashedResourceDiskFolder");
    		throw new RuntimeException("Error on check Trashed disk folder", t);
		}
	}
	/*
	protected Map<String,String> resourceInstancesTrashFilePaths(ResourceInterface resource) throws ApsSystemException {
		Map<String,String> filesPath = new HashMap<String, String>();
		StringBuilder subfolder = new StringBuilder(this.getResourceTrashRootDiskSubFolder());
		subfolder.append(File.separator).append(resource.getType())
				.append(File.separator).append(resource.getMainGroup()).append(File.separator);
		if (resource.isMultiInstance()) {
			AbstractMultiInstanceResource multiInstanceResource = (AbstractMultiInstanceResource) resource;
			Map<String, ResourceInstance> instances = multiInstanceResource.getInstances();
			Set<String> keys = instances.keySet();
			Iterator<String> iterator = keys.iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				ResourceInstance instance = instances.get(key);
				InputStream is = resource.getResourceStream(instance);
				String filename = instance.getFileName();
				this.getStorageManager().saveFile(subfolder.toString() + filename, true, is);
			}
		} else {
			AbstractMonoInstanceResource monoInstanceResource = (AbstractMonoInstanceResource) resource;
			ResourceInstance instance = monoInstanceResource.getInstance();
			InputStream is = resource.getResourceStream(instance);
			String filename = instance.getFileName();
			this.getStorageManager().saveFile(subfolder.toString() + filename, true, is);
		}
		return filesPath;
	}
	*/
	@Override
	public InputStream getTrashFileStream(ResourceInterface resource, ResourceInstance instance) throws ApsSystemException {
		try {
			String path = this.getSubfolder(resource) + instance.getFileName();
			return this.getStorageManager().getStream(path, true);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getTrashFileStream");
    		throw new ApsSystemException("Error on extracting stream", t);
		}
	}
	
	protected String getSubfolder(ResourceInterface resource) {
		StringBuilder subfolder = new StringBuilder(this.getResourceTrashRootDiskSubFolder());
		subfolder.append(File.separator).append(resource.getType())
				.append(File.separator).append(resource.getId()).append(File.separator);
		return subfolder.toString();
	}
	
	/*
     * Metodo di servizio. Restituisce una risorsa 
     * in base ai dati del corrispondente record del db.
     * @param resourceVo Il vo relativo al record del db.
     * @return La risorsa valorizzata.
     * @throws ApsSystemException
     */
    private ResourceInterface createResource(ResourceRecordVO resourceVo) throws ApsSystemException {
		String resourceType = resourceVo.getResourceType();
		String resourceXML = resourceVo.getXml();
		ResourceInterface resource = this.getResourceManager().createResourceType(resourceType);
		this.fillEmptyResourceFromXml(resource, resourceXML);
		resource.setMainGroup(resourceVo.getMainGroup());
		return resource;
	}
    
    /**
     * Valorizza una risorsa prototipo con gli elementi 
     * dell'xml che rappresenta una risorsa specifica. 
     * @param resource Il prototipo di risorsa da specializzare con gli attributi dell'xml.
     * @param xml L'xml della risorsa specifica. 
     * @throws ApsSystemException
     */
    protected void fillEmptyResourceFromXml(ResourceInterface resource, String xml) throws ApsSystemException {
    	try {
			SAXParserFactory parseFactory = SAXParserFactory.newInstance();			
    		SAXParser parser = parseFactory.newSAXParser();
    		InputSource is = new InputSource(new StringReader(xml));
    		ResourceHandler handler = new ResourceHandler(resource, this.getCategoryManager());
    		parser.parse(is, handler);
    	} catch (Throwable t) {
    		ApsSystemUtils.logThrowable(t, this, "fillEmptyResourceFromXml");
    		throw new ApsSystemException("Error on loading resource", t);
    	}
    }
	
	protected String getResourceTrashRootDiskSubFolder() {
		return JpversioningSystemConstants.DEFAULT_RESOURCE_TRASH_FOLDER_NAME;//folderName;
	}
	
	protected IResourceManager getResourceManager() {
		return _resourceManager;
	}
	public void setResourceManager(IResourceManager resourceManager) {
		this._resourceManager = resourceManager;
	}
    
	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}
	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}
    
	protected IStorageManager getStorageManager() {
		return _storageManager;
	}
	public void setStorageManager(IStorageManager storageManager) {
		this._storageManager = storageManager;
	}
	
    protected ITrashedResourceDAO getTrashedResourceDAO() {
		return _trashedResourceDAO;
	}
	public void setTrashedResourceDAO(ITrashedResourceDAO trashedResourceDAO) {
		this._trashedResourceDAO = trashedResourceDAO;
	}
	
	protected IResourceDAO getResourceDAO() {
		return _resourceDAO;
	}
	public void setResourceDAO(IResourceDAO resourceDAO) {
		this._resourceDAO = resourceDAO;
	}
	
    private IResourceManager _resourceManager;
    private ICategoryManager _categoryManager;
    
	private IStorageManager _storageManager;
	
    private ITrashedResourceDAO _trashedResourceDAO;
    private IResourceDAO _resourceDAO;
	
}