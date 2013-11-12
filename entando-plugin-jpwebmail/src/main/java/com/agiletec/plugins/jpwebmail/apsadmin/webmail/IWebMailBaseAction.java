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
package com.agiletec.plugins.jpwebmail.apsadmin.webmail;

import javax.mail.Store;

/**
 * Interfaccia base per tutte le classi action del modulo WebMail.
 * @version 1.0
 * @author E.Santoboni
 */
public interface IWebMailBaseAction {
	
	/**
	 * Setta lo store all'interno dell'azione.
	 * Il metodo viene chiamato dall'interceptor WebMailStoreFactoryInterceptor in fase di 
	 * inizializzazione; in tal modo la classe Action apposita è predisposta in partenza ad 
	 * effettuare una qualunque operazione all'interno della MailBox.
	 * Lo store viene poi chiuso direttamente dallo stesso interceptor una volta evasa la 
	 * richiesta nella sua totalità (Azione eseguita e view/jsp completamente renderizzata).
	 * @param store Lo store.
	 */
	public void setStore(Store store);
	
	/**
	 * Effettua la chiusura di cartelle eventualmente aperte.
	 * Il metodo viene chiamato dall'interceptor WebMailStoreFactoryInterceptor in fase di 
	 * chiusura della richiesta; in tal modo vengono rilasciate le cartelle 
	 * eventualmente aperte all'interno della MailBox.
	 * Per consentire questa operazione è necessario che ogni classe action tenga traccia di tutte 
	 * le cartelle aperte nel corso della elaborazione per permetterne una chiusura controllate 
	 * una volta finita l'elaborazione.
	 */
	public void closeFolders();
	
}