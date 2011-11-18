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
package com.agiletec.plugins.jpwebmail.aps.system;

public interface JpwebmailSystemConstants {
	
	/**
	 * Nome del servizio delegato alla gestione della Web Mail.
	 */
	public static final String WEBMAIL_MANAGER = "jpwebmailWebMailManager";
	
	/**
	 * Password usata dalla utility SSL per registrare i certificati autogenerati
	 */
	public static final String WEBMAIL_SSL_CERTIFICATE_STORE_PWD = "changeit";
	
	public static final String WEBMAIL_CONFIG_ITEM = "jpwebmail_config";
	
	
	public static final String CURRENT_MESSAGE_ON_EDIT = "currentMessageOnEdit";
	
	public static final String INBOX_FOLDER = "INBOX";
	
	public static final String TO_RECIPIENT = "1";
	
	public static final String CC_RECIPIENT = "2";
	
	public static final String BCC_RECIPIENT = "3";
	
}