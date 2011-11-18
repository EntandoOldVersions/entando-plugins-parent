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
package com.agiletec.plugins.jpuserprofile.aps.system.services;

/**
 * User Profile Plugin system constants.
 * @author E.Santoboni
 */
public interface ProfileSystemConstants {
	
	/**
	 * Code of default type of UserProfile Object
	 */
	public static final String DEFAULT_PROFILE_TYPE_CODE = "PFL";
	
	/**
	 * Bean Name of UserProfile Manager
	 */
	public static final String USER_PROFILE_MANAGER = "jpuserprofileUserProfileManager";
	
	/**
	 * The name of the attribute that contains the first name
	 */
	public static final String ATTRIBUTE_ROLE_FIRST_NAME = "jpuserprofile:firstname";
	
	/**
	 * The name of the attribute that contains the surname
	 */
	public static final String ATTRIBUTE_ROLE_SURNAME = "jpuserprofile:surname";
	
	/**
	 * The name of the attribute that contains the mail address
	 */
	public static final String ATTRIBUTE_ROLE_MAIL = "jpuserprofile:mail";
	
	public static final String ATTRIBUTE_DISABLING_CODE_ON_EDIT = "jpuserprofile:onEdit";
	
}
