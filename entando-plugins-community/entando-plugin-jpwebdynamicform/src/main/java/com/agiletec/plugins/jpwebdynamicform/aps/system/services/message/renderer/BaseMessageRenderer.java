/*
*
* Copyright 2010 AgileTec S.r.l. (http://www.agiletec.it) All rights reserved.
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
* Copyright 2010 AgileTec S.r.l. (http://www.agiletec.it) All rights reserved.
*
*/
package com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.renderer;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.renderer.BaseEntityRenderer;
import com.agiletec.aps.system.common.renderer.EntityWrapper;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Message;

/**
 * Implementation for Entity Renderer Service
 * Provide the rendering functionalities for generic entities.
 * @author E.Mezzano - M.Diana - W.Ambu - E.Santoboni
 */
public class BaseMessageRenderer extends BaseEntityRenderer {
	
	@Override
	protected EntityWrapper getEntityWrapper(IApsEntity entity) {
		return new EntityWrapper((Message) entity);
	}
	
	@Override
	protected String getEntityWrapperContextName() {
		return "message";
	}
	
}