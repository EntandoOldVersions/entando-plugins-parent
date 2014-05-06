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

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

/**
 * Wrapper of MimeMessage
 * @author E.Santoboni
 */
public class JpMimeMessage extends MimeMessage {
	
	protected JpMimeMessage(Session session) {
		super(session);
		//this._session = session;
	}
	
	@Override
	public Session getSession() {
		return super.getSession();
	}
	
	/*
	protected Session getSession() {
		return _session;
	}
	
	private Session _session;
	*/
}
