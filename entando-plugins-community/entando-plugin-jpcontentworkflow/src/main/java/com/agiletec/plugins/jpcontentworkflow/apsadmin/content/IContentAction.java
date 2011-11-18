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
package com.agiletec.plugins.jpcontentworkflow.apsadmin.content;

/**
 * @author E.Santoboni
 */
public interface IContentAction extends com.agiletec.plugins.jacms.apsadmin.content.IContentAction {
	
	/**
	 * Esegue l'azione di salvataggio contenuto con passaggio allo step precedente.
	 * @return Il codice del risultato dell'azione.
	 */
	public String previousStep();
	
	/**
	 * Esegue l'azione di salvataggio contenuto con passaggio allo step successivo.
	 * @return Il codice del risultato dell'azione.
	 */
	public String nextStep();
	
}