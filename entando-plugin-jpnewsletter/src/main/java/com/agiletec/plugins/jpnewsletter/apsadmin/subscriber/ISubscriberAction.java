/*
*
* Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpnewsletter.apsadmin.subscriber;


/**
 * Interfaccia base per la classe SubscriberAction per la gestione dei
 * sottoscritti al servizio generico di newsletter in Back-End.
 * 
 * @author A.Turrini
 */
public interface ISubscriberAction {

	/**
	 * Cancella definitivamente una sottoscrizione al servizio di newsletter.
	 * 
	 * @return Stringa risultato della cancellazione della sottoscrizione
	 */
	public String deleteSubscriber();

	/**
	 * Predispone la cancellazione di una sottoscrizione dal servizio di
	 * newsletter.
	 * 
	 * @return Stringa risultato dell'operazione
	 */
	public String trashSubscriber();

}
