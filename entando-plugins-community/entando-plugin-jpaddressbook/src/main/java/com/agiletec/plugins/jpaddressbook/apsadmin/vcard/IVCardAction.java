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
package com.agiletec.plugins.jpaddressbook.apsadmin.vcard;

import java.io.InputStream;

/**
 * @author A.Cocco
 */
public interface IVCardAction {
	
	/**
	 * Entry point for the VCard configuration.
	 * @return the result of the action.
	 */
	public String edit();
	
	/**
	 * Filter the fields of the VCard.
	 * @return the result of the action.
	 */
	public String filterFields();
	
	/**
	 * Save the VCard configurationVCardFields
	 * @return true if the result of configuration's save it's ok.
	 */
	public String save();
	
	/**
	 * Returns vcard inputstream
	 * @return vcard inputstream
	 */
	public InputStream getInputStream();
	
	/**
	 * Download VCard
	 * @return VCard
	 * @throws Exception
	 */
	public String downloadVCard();	
	
}