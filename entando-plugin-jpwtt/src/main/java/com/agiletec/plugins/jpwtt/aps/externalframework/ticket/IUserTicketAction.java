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
package com.agiletec.plugins.jpwtt.aps.externalframework.ticket;

/**
 * Interface for a class providing, for the current user, functions of creation ad visualization of a Ticket.
 * @version 1.0
 * @author E.Mezzano
 *
 */
public interface IUserTicketAction {
	
	/**
	 * Execute the action of visualization of a Ticket created by the current user.
	 * @return The action result code.
	 */
	public String view();
	
	/**
	 * Execute the action of creation of a Ticket for the current user.
	 * @return The action result code.
	 */
	public String save();
	
}