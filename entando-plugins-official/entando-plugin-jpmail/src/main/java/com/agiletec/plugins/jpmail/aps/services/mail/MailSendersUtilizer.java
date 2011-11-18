/*
*
* Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
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
* Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
*/
package com.agiletec.plugins.jpmail.aps.services.mail;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Basic interface for the services using jpmail plugin and IMailManager.
 * 
 * @version 1.0
 * @author E.Mezzano
 *
 */
public interface MailSendersUtilizer {
	
	/**
	 * Returns the identifier of the IMailManager utilizer service.
	 * @return The identifier of the IMailManager utilizer service.
	 */
	public String getName();
	
	/**
	 * Returns the array of the used sender codes.
	 * Restituisce l'array dei codici degli indirizzi mittente utilizzati.
	 * @return The array of the used sender codes.
	 * @throws ApsSystemException In case of exceptions.
	 */
	public String[] getSenderCodes();
	
}