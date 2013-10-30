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
package org.entando.entando.plugins.jpfrontshortcut.aps.tags;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.tags.InternalServletTag;

/**
 * @author E.Santoboni
 */
public class StaticInternalServletTag extends InternalServletTag {
	
	@Override
	protected void includeShowlet(RequestContext reqCtx, ResponseWrapper responseWrapper, Widget widget) throws ServletException, IOException {
		HttpServletRequest request = reqCtx.getRequest();
		try {
			String actionPath = super.getActionPath();
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(actionPath);
			requestDispatcher.include(request, responseWrapper);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "includeShowlet", "Error including showlet");
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/aps/jsp/system/internalServlet_error.jsp");
			requestDispatcher.include(request, responseWrapper);
		}
	}
	
}
