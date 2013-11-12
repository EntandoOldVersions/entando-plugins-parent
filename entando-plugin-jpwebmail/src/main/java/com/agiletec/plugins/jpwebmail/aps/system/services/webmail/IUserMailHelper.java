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
package com.agiletec.plugins.jpwebmail.aps.system.services.webmail;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * Interfaccia base per la classe helper delegata alla restituzione 
 * dell'indirizzo email dell'utente specificato.
 * @version 1.0
 * @author E.Santoboni
 */
public interface IUserMailHelper {
	
	/** 
	 * Restituisce la mail associata all'utente dato.
	 * L'indirizzo email può essere restituito nella forma semplice (solo indirizzo email) o 
	 * nella froma completa (nome con indirizzo email rachiuso tra parentesi "<>")
	 * @param user L'utente cui restituire l'indirizzo email corrispondente.
	 * @return L'indirizzo email dell'utente specificato.
	 * @throws ApsSystemException In caso di errore.
	 */
	public String getEmailAddress(UserDetails user) throws ApsSystemException;
	
}