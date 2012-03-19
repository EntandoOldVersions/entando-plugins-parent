/*
*
* Copyright 2009 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
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
* Copyright 2009 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
*/
package com.agiletec.plugins.jpavatar.aps.system;

public interface JpAvatarSystemConstants {

	/**
	 * Name of the bean that manages the avatars
	 */
	public static final String AVATAR_MANAGER = "jpavatarAvatarManager";

	/**
	 * Filename that must be returned when no avatar is found. This file must exists and placed in the "avatarDiskFolder" directory
	 */
	public static final String DEFAULT_AVATAR_NAME = "default.png";
}
