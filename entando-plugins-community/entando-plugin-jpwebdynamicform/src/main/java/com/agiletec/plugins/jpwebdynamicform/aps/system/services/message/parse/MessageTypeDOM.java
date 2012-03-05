/*
*
* Copyright 2010 AgileTec S.r.l. (http://www.agiletec.it) All rights reserved.
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
* Copyright 2010 AgileTec S.r.l. (http://www.agiletec.it) All rights reserved.
*
*/
package com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.parse;

import com.agiletec.aps.system.common.entity.parse.EntityTypeDOM;

/**
 * DOM class delegated to interpret the xml defining the Message Types configuration.
 * This class is used as utility for MessageManager.
 * @see EntityTypeDOM for more information.
 * @author M.Minnai
 */
public class MessageTypeDOM extends EntityTypeDOM {
	
	@Override
	protected String getEntityTypeRootElementName() {
		return "messagetype";
	}
	
	@Override
	protected String getEntityTypesRootElementName() {
		return "messagetypes";
	}
	
}