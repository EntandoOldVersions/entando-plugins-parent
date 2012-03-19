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
package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model;

import java.util.Date;


/**
 * Bean per la gestione dei parametri ricercabili dei commenti
 * @author D.Cherchi
 *
 */
public interface ICommentSearchBean {

	/**
	 * @return L'identificativo del contenuto da ricercare
	 */
	public String getContentId();

	/**
	 * @return La data iniziale di publicazione  per la ricerca su range di date
	 */
	public Date getCreationFROMDate();

	/**
	 *
	 * @return La data finale di publicazione per la ricerca su range di date
	 */
	public Date getCreationTODate();

	/**
	 *
	 * @return Il testo del commento da ricercare
	 */
	public String getComment();

	/**
	 *
	 * @return Lo stato dei commenti da ricercare
	 */
	public int getStatus();

	/**
	 *
	 * @return L'utente autore dei commenti da ricercare
	 */
	public String getUsername();

}