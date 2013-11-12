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
package com.agiletec.plugins.jpwebmail.apsadmin.webmail;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * @version 1.0
 * @author E.Santoboni
 */
public abstract class AbstractWebmailBaseAction extends BaseAction implements IWebMailBaseAction {
	
	public Folder[] getMainFolders() throws Throwable {
		Folder folder = this.getStore().getDefaultFolder();
		Folder[] child = folder.list();
		return child;
	}
	
	public Folder[] getCurrentChildrenFolders() {
		try {
			Folder folder = this.getCurrentFolder();
			Folder[] child = folder.list();
			if (child == null || child.length==0) {
				child = folder.getParent().list();
			}
			return child;
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getCurrentChildrenFolders", 
					"Errore in estrazione figli di cartella " + this.getCurrentFolderName());
		}
		return new Folder[0];
	}
	
	public Folder getCurrentFolder() {
		String currentFolder = this.getCurrentFolderName();
		try {
			return this.getStore().getFolder(currentFolder);
		} catch (MessagingException e) {
			throw new RuntimeException("Errore in estrazione cartella " + currentFolder, e);
		}
	}
	
	@Override
	public void closeFolders() {
		Folder folder = this.getOpenedFolder();
		if (null != folder && folder.isOpen()) {
			try {
				folder.close(false);
			} catch (MessagingException e) {
				throw new RuntimeException("Errore in chiusura folder " + folder.getName());
			}
		}
	}
	
	protected Store getStore() {
		return this._store;
	}
	@Override
	public void setStore(Store store) {
		this._store = store;
	}
	
	public String getCurrentFolderName() {
		if (null == this._currentFolderName) this.setCurrentFolderName("INBOX");
		return _currentFolderName;
	}
	public void setCurrentFolderName(String currentFolderName) {
		this._currentFolderName = currentFolderName;
	}
	
	protected Folder getOpenedFolder() {
		return _openedFolder;
	}
	protected void setOpenedFolder(Folder openedFolder) {
		this._openedFolder = openedFolder;
	}
	
	private Store _store;
	private String _currentFolderName;
	private Folder _openedFolder;
	
}