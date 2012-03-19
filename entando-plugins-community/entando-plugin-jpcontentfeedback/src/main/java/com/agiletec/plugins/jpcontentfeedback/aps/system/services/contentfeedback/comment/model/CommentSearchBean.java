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
package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model;

import java.util.Date;

/**
 *
 * @author D.Cherchi
 *
 */
public class CommentSearchBean implements ICommentSearchBean {

	@Override
	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}
	@Override
	public Date getCreationFROMDate() {
		return _creationFROMDate;
	}
	public void setCreationFROMDate(Date creationFROMDate) {
		this._creationFROMDate = creationFROMDate;
	}

	@Override
	public String getComment() {
		return _comment;
	}
	public void setComment(String comment) {
		this._comment = comment;
	}

	@Override
	public int getStatus() {
		return _status;
	}
	public void setStatus(int status) {
		this._status = status;
	}

	@Override
	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}

	public void setCreationTODate(Date creationTODate) {
		this._creationTODate = creationTODate;
	}

	@Override
	public Date getCreationTODate() {
		return _creationTODate;
	}

	public String _contentId;
	public Date _creationFROMDate;
	private Date _creationTODate;
	public String _comment;
	public int _status = 0;
	private String _username;

}