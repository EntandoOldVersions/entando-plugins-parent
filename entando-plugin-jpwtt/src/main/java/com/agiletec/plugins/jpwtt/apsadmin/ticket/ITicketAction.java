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
package com.agiletec.plugins.jpwtt.apsadmin.ticket;

public interface ITicketAction {
	
	public String view();
	
	public String takeInCharge();
	
	public String update();
	
	public String setAssignable();
	
	public String answer();
	
	public String release();
	
	public String close();
	
}