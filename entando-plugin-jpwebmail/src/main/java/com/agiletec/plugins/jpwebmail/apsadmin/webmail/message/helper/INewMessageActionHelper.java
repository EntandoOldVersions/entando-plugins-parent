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
package com.agiletec.plugins.jpwebmail.apsadmin.webmail.message.helper;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * @version 1.0
 * @author E.Santoboni
 */
public interface INewMessageActionHelper {
	
	public MimeMessage updateMessageOnSession(HttpServletRequest request) throws ApsSystemException;
	
	public String getUserWebMailDiskRootFolder(UserDetails currentUser);
	
	public void deleteUserWebMailTempDirectory(UserDetails currentUser);
	
}
