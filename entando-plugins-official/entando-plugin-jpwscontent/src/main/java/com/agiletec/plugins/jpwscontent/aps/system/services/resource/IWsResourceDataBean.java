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

import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceDataBean;
/**
 * Base interface for the resources that comes from a webservice call or must
 * be inserted inside a envelope
 *
 */
public interface IWsResourceDataBean extends ResourceDataBean {

	/**
	 * Returns the resource id
	 * @return the resource id
	 */
	public String getId() ;
	
	/**
	 * Set the id for the resource
	 * @param id string of the resource id
	 */
	public void setId(String id);

}
