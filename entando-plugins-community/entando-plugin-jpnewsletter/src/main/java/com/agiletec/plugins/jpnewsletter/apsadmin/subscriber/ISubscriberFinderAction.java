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
package com.agiletec.plugins.jpnewsletter.apsadmin.subscriber;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.Subscriber;

/**
 * Interfaccia base per la classe SubscriberFinderAction per la ricerca dei
 * sottoscritti al servizio generico di newsletter in Back-End.
 * 
 * @author A.Turrini
 */
public interface ISubscriberFinderAction {
	
	/**
	 * Carica la lista degli utenti sottoscritti al servizio generico di
	 * newsletter
	 * 
	 * @return Stringa risultato dell'operazione di caricamento
	 * @throws ApsSystemException 
	 */
	public List<Subscriber> getSubscribers() throws ApsSystemException;
	
}
