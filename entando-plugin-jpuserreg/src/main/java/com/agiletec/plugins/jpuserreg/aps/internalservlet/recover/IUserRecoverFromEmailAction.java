/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
* Entando is a free software; 
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpuserreg.aps.internalservlet.recover;

/**
 * Interface for struts Action for managing requests for password recover
 * @author G.Cocco
 * */
public interface IUserRecoverFromEmailAction {

	/**
	 * Password recover from email.
	 * @return The action result
	 * */
	public String recoverFromEmail();

	/**
	 * Initialize functionality and redirect
	 * if user is already logged
	 * */
	public String initRecover();
	
}
