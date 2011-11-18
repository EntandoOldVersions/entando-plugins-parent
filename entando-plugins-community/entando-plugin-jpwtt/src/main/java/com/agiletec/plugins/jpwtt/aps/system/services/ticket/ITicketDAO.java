/*
* Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
* This file is part of JAPS software.
* JAPS and its  source-code is  licensed under the  terms of the
* GNU General Public License  as published by  the Free Software
* Foundation (http://www.fsf.org/licensing/licenses/gpl.txt).
* 
* You may copy, adapt, and redistribute this file for commercial
* or non-commercial use.
* When copying,  adapting,  or redistributing  this document you
* are required to provide proper attribution  to AgileTec, using
* the following attribution line:
* Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
*/
package com.agiletec.plugins.jpwtt.aps.system.services.ticket;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.ITicketSearchBean;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.Ticket;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.TicketOperation;

/**
 * Interface for Data Access Object for Ticket Objects.
 * @version 1.0
 * @author S.Didaci - G.Cocco
 */
public interface ITicketDAO {
   
	public List<Ticket> loadTickets() throws ApsSystemException;
	
	public List<String> searchTicketIds(ITicketSearchBean searchBean) throws ApsSystemException;
	
	public Ticket loadTicket(String code) throws ApsSystemException;
	
	public List<TicketOperation> loadTicketOperations(String code) throws ApsSystemException;
	
	public void addTicket(Ticket ticket) throws ApsSystemException;
	
	public void updateTicketWithOperation(Ticket ticket, TicketOperation operation) throws ApsSystemException;
	
}