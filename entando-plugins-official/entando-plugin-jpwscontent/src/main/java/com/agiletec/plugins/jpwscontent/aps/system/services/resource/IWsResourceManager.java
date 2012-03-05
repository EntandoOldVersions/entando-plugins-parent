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

import org.w3c.dom.NodeList;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.WsContentEnvelope;

/**
 * Interface for the service that handles the remote resources
 */
public interface IWsResourceManager extends IResourceManager {
	/**
	 * Check if the resources specified in the xml are present in the resources section of the envelope
	 * @param envelope the envelope. Containd the xml and the resources
	 * @param nodeList the list of the resources specified in the xml
	 * @return true if all the resources are present
	 * @throws ApsSystemException if an error occurs
	 */
	public boolean checkResourcesPresent(WsContentEnvelope envelope, NodeList nodeList) throws ApsSystemException;

	/**
	 * Extract the {@link ApsWsResource} according to the id provided as parameter
	 * @param envelope the envelope containing the resource to extract
	 * @param id the id of the resource to extract
	 * @return the {@link ApsWsResource} 
	 * @throws ApsSystemException if an error occurs
	 */
	public WsResource getResourceFromEnvelope(WsContentEnvelope envelope, String id) throws ApsSystemException;

	/**
	 * Check if the resource id id is unique. 
	 * If not, returns a new id
	 * @param envelope the envelope that contains the resource
	 * @param id the resource id
	 * @return the resource id, modidued if necessary
	 * @throws ApsSystemException if an error occurs
	 */
	public String checkResourcesUniqueId(WsContentEnvelope envelope, String id) throws ApsSystemException;

	/**
	 * Add a new resource to the system
	 * @param resource the resource to add
	 * @return the added resource
	 * @throws Throwable if an error occurs
	 */
	public ResourceInterface addResourceFromWSCall(WsResource wsResource) throws ApsSystemException;
	
}
