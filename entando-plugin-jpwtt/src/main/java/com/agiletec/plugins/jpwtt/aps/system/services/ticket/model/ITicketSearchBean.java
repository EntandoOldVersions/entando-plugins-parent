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
package com.agiletec.plugins.jpwtt.aps.system.services.ticket.model;

public interface ITicketSearchBean {
	
	public String getMessage();
	
	public String getAuthor();
	
	public String getOperator();
	
	public String[] getWttRoles();
	
	public int[] getStates();
	
	public Integer getPriority();
	
	public Integer getUserInterventionType();
	
	public Integer getAssignedInterventionType();
	
	public Boolean getResolved();
	
}