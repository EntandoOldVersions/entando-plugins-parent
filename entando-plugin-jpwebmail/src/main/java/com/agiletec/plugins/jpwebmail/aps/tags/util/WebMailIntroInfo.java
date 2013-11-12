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
package com.agiletec.plugins.jpwebmail.aps.tags.util;

public class WebMailIntroInfo {
	
	public int getMessageCount() {
		return _messageCount;
	}
	public void setMessageCount(int messageCount) {
		this._messageCount = messageCount;
	}
	
	public int getNewMessageCount() {
		return _newMessageCount;
	}
	public void setNewMessageCount(int newMessageCount) {
		this._newMessageCount = newMessageCount;
	}
	
	public int getUnreadMessageCount() {
		return _unreadMessageCount;
	}
	public void setUnreadMessageCount(int unreadMessageCount) {
		this._unreadMessageCount = unreadMessageCount;
	}
	
	public boolean isExistMailbox() {
		return _existMailbox;
	}
	public void setExistMailbox(boolean existMailbox) {
		this._existMailbox = existMailbox;
	}
	
	private int _messageCount;
	private int _newMessageCount;
	private int _unreadMessageCount;
	private boolean _existMailbox;
	
}