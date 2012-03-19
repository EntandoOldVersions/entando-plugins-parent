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
package com.agiletec.plugins.jpcontentfeedback.apsadmin.portal.specialshowlet;

/**
 * Interfaccia base per le action gestori della configurazione della showlet erogatore contenuto singolo,
 * e del blocco relativo agli elementi del contentFeedback: commenti e rating
 * @author D.Cherchi
 *
 */
public interface IContentFeedbackShowletAction {

	/**
	 * Esegue l'operazione di associazione di un contenuto alla showlet.
	 * L'operazione ha l'effetto di inserire il riferimento del contenuto desiderato
	 * (ricavato dai parametri della richiesta) nei parametri di configurazione della showlet.
	 * @return Il codice del risultato dell'azione.
	 */
	public String joinContent();

}
