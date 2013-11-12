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
package com.agiletec.plugins.jpwebmail.apsadmin.config;

public interface IWebMailConfigAction {
	
	/**
	 * Esegue l'azione di edit della configurazione del servizio di Web Mail.
	 * @return Il codice del risultato dell'azione.
	 */
	public String edit();
	
	/**
	 * Esegue l'azione di salvataggio della configurazione del servizio di  Web Mail.
	 * @return Il codice del risultato dell'azione.
	 */
	public String save();
	
}