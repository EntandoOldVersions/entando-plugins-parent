/*
*
* Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* jAPS is a free software; 
* you can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpaddressbook.aps.system.services.vcard;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpaddressbook.aps.system.services.vcard.model.VCardContactField;

/**
 * @author A.Cocco
 */
public interface IVCardManager {
	
	public void updateVCardMapping(List<VCardContactField> fields) throws ApsSystemException;
	
	public String getVCard(String contactId) throws ApsSystemException;
	
	public List<VCardContactField> getVCardFields() throws ApsSystemException;
	
}
