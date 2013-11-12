/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando Enterprise Edition software.
* You can redistribute it and/or modify it
* under the terms of the Entando's EULA
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwebmail.apsadmin.webmail.message;

/**
 * Interfaccia base per le classi action che gestiscono le operazioni 
 * per la gestione degli attachment (aggiunta/rimozione) in un nuovo messaggio singolo.
 * @version 1.0
 * @author E.Santoboni
 */
public interface IAttachmentAction {
	
	/**
	 * Effettua la richiesta di aggiunta di un nuovo attachment al messaggio corrente.
	 * @return Il codice del risultato dell'azione.
	 */
	public String addAttachment();
	
	/**
	 * Effettua la richiesta di rimozione di un attachment dal messaggio corrente.
	 * @return Il codice del risultato dell'azione.
	 */
	public String removeAttachment();
	
}