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
package com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model;

public class MessageModel {
	
	public String getBodyModel() {
		return _bodyModel;
	}
	public void setBodyModel(String bodyModel) {
		this._bodyModel = bodyModel;
	}
	
	public String getSubjectModel() {
		return _subjectModel;
	}
	public void setSubjectModel(String subjectModel) {
		this._subjectModel = subjectModel;
	}
	
	private String _bodyModel;
	private String _subjectModel;
	
}