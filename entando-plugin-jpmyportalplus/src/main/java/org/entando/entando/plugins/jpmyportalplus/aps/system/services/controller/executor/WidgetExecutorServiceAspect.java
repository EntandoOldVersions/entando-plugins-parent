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
package org.entando.entando.plugins.jpmyportalplus.aps.system.services.controller.executor;

import com.agiletec.aps.system.RequestContext;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import org.entando.entando.aps.system.services.controller.executor.WidgetExecutorService;

/**
 * @author E.Santoboni
 */
@Aspect
public class WidgetExecutorServiceAspect extends WidgetExecutorService {
	
	@AfterReturning(pointcut = "execution(* org.entando.entando.aps.system.services.controller.executor.WidgetExecutorService.service(..))", returning = "reqCtx")
    public void injectMyPortalBean(RequestContext reqCtx) {
        System.out.println("**************************************");
        System.out.println(this);
        System.out.println(reqCtx);
        System.out.println("**************************************");
    }
	
}
