/*
*
* Copyright 2008 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
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
* Copyright 2008 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
*/
package com.agiletec.plugins.jpversioning.aps.system.services.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
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
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceDAO;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AbstractMonoInstanceResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AbstractMultiInstanceResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInstance;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceRecordVO;
import com.agiletec.plugins.jacms.aps.system.services.resource.parse.ResourceHandler;

/**
 * Manager of trashed resources.
 * @author G.Cocco, E.Mezzano
 */
@Aspect
public class TrashedResourceManager extends AbstractService implements ITrashedResourceManager {
	
	public void init() throws Exception {
		ApsSystemUtils.getLogger().config(this.getClass().getName() + ": initialized ");
		ApsSystemUtils.getLogger().config("Folder trashed resources: " + this.getResourceTrashRootDiskFolder());
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
				Map<String, String> filesPathDestination = this.resourceInstancesArchiveFilePaths(resource);
				Map<String, String> filesPathSource = this.resourceInstancesTrashFilePaths(resource);
				String filePath = filesPathDestination.values().iterator().next();
				String destionationDirPath = filePath.substring(0, filePath.lastIndexOf(File.separator));
				this.checkTrashedResourceDiskFolder(destionationDirPath);		
				this.moveResourcesIstances(filesPathSource, filesPathDestination);
	    		this.getResourceDAO().addResource(resource);
				this.deleteInstancesFromTrash(filesPathSource.values());
				this.getTrashedResourceDAO().delTrashedResource(resourceId);
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "restoreResource", "Error on restoring trashed resource");
				throw new ApsSystemException("Error on restoring trashed resource", t);
			}
		}
	}
	
	@Override
	public void removeFromTrash(String resourceId) throws ApsSystemException {
		// Rimozione file fisici
		ResourceRecordVO resourceVo = this.getTrashedResourceDAO().getTrashedResource(resourceId);
		if (null != resourceVo) {
			ResourceInterface resource = this.createResource(resourceVo);
			Map<String, String> paths = this.resourceInstancesTrashFilePaths(resource);
			this.deleteInstancesFromTrash(paths.values());
		}
		// Rimozione metadati
		this.getTrashedResourceDAO().delTrashedResource(resourceId);
	}
	
	/**
	 * Carica una risorsa dal cestino per il ripristino, occupandosi quindi anche 
	 * della sua rimozione dal cestino.
	 * 
	 * @param resourceId l'identificativo della risorsa
	 * @param basePath il percorso RealPath di esecuzione per il calcolo dei percorsi sul filesystem
	 * */
	private ResourceInterface getResourceToRestore(String resourceId) throws Throwable {
		// Ripristina la risorsa leggendola dalle "tabelle-cestino"
		// e solo dopo cancella la risorsa con il metodo del TrashedResourceDAO 
		ResourceInterface resource = this.loadTrashedResource(resourceId);
		if (null != resource) {
			try {
				// spostamento file fisici e cancellazione del cestino
				Map<String, String> filesPathDestination = this.resourceInstancesArchiveFilePaths(resource);
				Map<String, String> filesPathSource = this.resourceInstancesTrashFilePaths(resource);
				String destionationDirPath = filesPathDestination.values().iterator().next();
				this.checkTrashedResourceDiskFolder(destionationDirPath);		
				this.moveResourcesIstances(filesPathSource, filesPathDestination);
	    		this.getResourceDAO().addResource(resource);
				this.deleteInstancesFromTrash(filesPathSource.values());
				this.getTrashedResourceDAO().delTrashedResource(resourceId);
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "getResourceToRestore", "Error while doading trashed resource to testore : resource id " + resourceId);
				throw t;
			}
		}
		return resource;
	}
	
	@Override
	public void addTrashedResource(ResourceInterface resource) throws ApsSystemException {
		// Invoca cancellazione della risorsa dal Manager esteso
		// Solo dopo aver creato la copia della risorsa con il TrashedResourceDAO
		
		// salvo risorsa sul cestino file
		Map<String, String> filesPathSource = this.resourceInstancesArchiveFilePaths(resource);
		Map<String, String> filesPathDestination = this.resourceInstancesTrashFilePaths(resource);
		String filePath = filesPathDestination.values().iterator().next();
		String destionationDirPath = filePath.substring(0, filePath.lastIndexOf(File.separator));
		this.checkTrashedResourceDiskFolder(destionationDirPath);		
		this.moveResourcesIstances(filesPathSource, filesPathDestination);
		// salvo risorsa sul cestino - db
		this.getTrashedResourceDAO().addTrashedResource(resource);
	}
	
	/*
	 * Cancella le istanze di una risorsa presenti nel cestino 
	 * */
	private void deleteInstancesFromTrash(Collection<String> paths) {		
		for ( String path : paths) {
			File file = new File(path);
			if (null != file) {
				file.delete();
			} else {
				ApsSystemUtils.getLogger().info(" rimozione di " + path + " file non trovato.");
			}
		}
	}
	
	/*
	 * Spostamento delle risorse di tutte le istanze di una risorsa
	 * */
	private void moveResourcesIstances(Map<String, String> filesPath, Map<String, String> filesPathDestination) throws ApsSystemException {
		Set<String> keys = filesPath.keySet();
		for (String key : keys ) {
			this.save(filesPath.get(key), filesPathDestination.get(key));
		}
	}
	
	/*
	 * Verifica l'esistenza della directory di destinazione dei file
	 * */
	private void checkTrashedResourceDiskFolder(String dirPath) {
		File dir = new File(dirPath);
		if (!dir.exists() || !dir.isDirectory()) {
			dir.mkdirs();
		}
	}
	
	/*
	 * Salvataggio di un file su filesystem
	 * */
	private void save(String filePathSource, String filePathDestination) throws ApsSystemException {
    	try {
    		FileInputStream inStream = new FileInputStream(filePathSource);
    		FileOutputStream outStream = new FileOutputStream(filePathDestination);
    		while (inStream.available() > 0) {
    			outStream.write(inStream.read());
    		}
    		outStream.close();
    		inStream.close();
    	} catch (FileNotFoundException e) {
    		ApsSystemUtils.logThrowable(e, this, "save", "File not found : " + filePathSource);
    	} catch (IOException e) {
    		ApsSystemUtils.logThrowable(e, this, "save");
    	} catch (Throwable t) {
    		ApsSystemUtils.logThrowable(t, this, "save");
    		throw new ApsSystemException("Generic error on saving file : " + filePathSource, t);
    	}
    }
	
	/*
	 * Metodo di servizio per conoscere la lista dei percorsi delle istanze della risorsa nel cestino 
	 */
	@Override
	public Map<String,String> resourceInstancesTrashFilePaths(ResourceInterface resource) {
		Map<String,String> filesPath = null;
		String type = resource.getType();
		StringBuffer bufferStr = new StringBuffer(this.getResourceTrashRootDiskFolder());
		bufferStr.append(File.separator);
		bufferStr.append(type);
		bufferStr.append(File.separator);
		bufferStr.append(resource.getMainGroup());
		bufferStr.append(File.separator);
		String filename = null;
		filesPath = new HashMap<String, String>();
		if (resource.isMultiInstance()) {
			AbstractMultiInstanceResource multiInstanceResource = (AbstractMultiInstanceResource) resource;
			Map<String, ResourceInstance> instances = multiInstanceResource.getInstances();
			Set<String> keys = instances.keySet();
			Iterator<String> it = keys.iterator();
			StringBuffer tempBuff = null;
			while (it.hasNext()) {
				tempBuff = new StringBuffer(bufferStr);
				String obj = it.next();
				filename = ((ResourceInstance)instances.get(obj)).getFileName();
				filesPath.put(obj, tempBuff.append(filename).toString());
			}
		} else {
			AbstractMonoInstanceResource monoInstanceResource = (AbstractMonoInstanceResource) resource;
			ResourceInstance instance = monoInstanceResource.getInstance();
			filename = ((ResourceInstance)instance).getFileName();
			filesPath.put("0", bufferStr.append(filename).toString());
		}
		return filesPath;
	}
	
	/*
	 * Metodo di servizio per conoscere la lista dei percorsi delle istanze di una risorsa dell'archivio
	 * */
	private Map<String,String> resourceInstancesArchiveFilePaths(ResourceInterface resource) {
		StringBuffer resourceDiskFolder = new StringBuffer(resource.getDiskFolder());
		int i = 0;
		Map<String,String> filesPath = new HashMap<String, String>();
		String filename = null;
		if (resource.isMultiInstance()) {
			AbstractMultiInstanceResource multiInstanceResource = (AbstractMultiInstanceResource) resource;
			Map instances = multiInstanceResource.getInstances();
			Set keys = instances.keySet();
			Iterator it = keys.iterator();
			while (it.hasNext()) {
				resourceDiskFolder = new StringBuffer(resource.getDiskFolder());
				Object obj = it.next();
				filename = ((ResourceInstance)instances.get(obj)).getFileName();
				filesPath.put(obj.toString(), resourceDiskFolder.append(filename).toString());
			}
		} else {
			AbstractMonoInstanceResource monoInstanceResource = (AbstractMonoInstanceResource) resource;
			ResourceInstance instance = monoInstanceResource.getInstance();
			filename = ((ResourceInstance)instance).getFileName();
			filesPath.put("0", resourceDiskFolder.append(filename).toString());
		}
		return filesPath;
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
    		throw new ApsSystemException("Error on loading resource risorsa", t);
    	}
    }
	
	private boolean isValidNumericString(String integerNumber) {
		return (integerNumber.trim().length() > 0 && integerNumber.matches("\\d+"));
	}
    
	protected String getResourceTrashRootDiskFolder() {
		return _resourceTrashRootDiskFolder;
	}
	public void setResourceTrashRootDiskFolder(String resourceTrashRootDiskFolder) {
		this._resourceTrashRootDiskFolder = resourceTrashRootDiskFolder;
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
	
	private String _resourceTrashRootDiskFolder;
	
    private IResourceManager _resourceManager;
    private ICategoryManager _categoryManager;
    
    private ITrashedResourceDAO _trashedResourceDAO;
    private IResourceDAO _resourceDAO;
	
}