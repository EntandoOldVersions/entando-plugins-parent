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
package com.agiletec.plugins.jpmyportal.aps.internalservlet.myportal;

public interface IMyPortalAction {
	
	/**
	 * Scambia le showlet contenute nei due frame indicati
	 * @return il risultato dell'operazione.
	 */
	public String swapFrames();
	
	/**
	 * Mette la showlet vuota ('void') nel frame corrente
	 * @return il risultato dell'operazione.
	 */
	public String emptyCustomizableShowlet();
	
	/**
	 * Pubblica la showlet richiesta nel frame corrente
	 * @return il risultato dell'operazione.
	 */
	public String assignShowletToFrame();
	
	/**
	 * Resetta il frame personalizzato dall'utente ripristinando quello della configurazione della pagina.
	 * @return il risultato dell'operazione.
	 */
	public String resetFrame();
	
}