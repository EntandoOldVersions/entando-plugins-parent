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
package com.agiletec.plugins.jpnewsletter.aps.system.services.linkresolver;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.plugins.jacms.aps.system.services.linkresolver.LinkResolverManager;

/**
 * Servizio gestore della risoluzione dei link simbolici AD HOC per il servizio di newsletter.
 * Scopo di questa classe è l'individuazione in un testo di stringhe rappresentanti
 * link simbolici, e la loro traduzione e sostituzione nel testo con i 
 * corrispondenti URL.
 * Rispetto alla versione base gestisce correttamente l'assenza del RequestContext.
 * Estende, limitatemente base.
 * @author E.Mezzano
 */
public class JpnewsletterLinkResolverManager extends LinkResolverManager {
	
	@Override
	protected boolean isCurrentUserAllowed(RequestContext reqCtx, String pageCode) {
		if (reqCtx != null) {
			return super.isCurrentUserAllowed(reqCtx, pageCode);
		}
		return true;
	}
	
	@Override
	protected boolean isPageAllowed(RequestContext reqCtx, String pageCode) {
		if (reqCtx != null) {
			return super.isPageAllowed(reqCtx, pageCode);
		}
		return true;
	}
	
}