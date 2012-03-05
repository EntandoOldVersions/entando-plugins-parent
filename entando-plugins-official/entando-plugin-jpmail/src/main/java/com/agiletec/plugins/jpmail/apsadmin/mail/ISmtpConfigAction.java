/*
 *
 * Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
 * Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 */
package com.agiletec.plugins.jpmail.apsadmin.mail;

public interface ISmtpConfigAction {
	
	/**
	 * Execute the action of editing of the IMailManager service configuration.
	 * @return The action result code.
	 */
	public String edit();
	
	/**
	 * Execute the action of saving of the IMailManager service configuration.
	 * @return The action result code.
	 */
	public String save();
	
}