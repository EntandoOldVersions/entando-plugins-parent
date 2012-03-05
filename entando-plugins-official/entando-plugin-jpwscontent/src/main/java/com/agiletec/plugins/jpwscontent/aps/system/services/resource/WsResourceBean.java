/*
*
* Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software; 
* you can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwscontent.aps.system.services.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.WsContentEnvelope;

/**
 * Resource object used inside the {@link WsContentEnvelope}
 */
public class WsResourceBean implements IWsResourceDataBean {
	
	/**
	 * Return the resource type
	 * @return the resource type
	 */
	public String getResourceType() {
		return _resourceType;
	}
	public void setResourceType(String type) {
		this._resourceType = type;
	}
	
	/**
	 * Return the description of the resource
	 * @return The description of the resource
	 */
	public String getDescr() {
		return _descr;
	}
	public void setDescr(String descr) {
		this._descr = descr;
	}
	
	/**
	 * Return the main group of the resource
	 * @return the main group of the resource
	 */
	public String getMainGroup() {
		return _mainGroup;
	}
	public void setMainGroup(String mainGroup) {
		this._mainGroup = mainGroup;
	}

	/**
	 * Return the list of the categories associated to the resource
	 * @return the list of the categoriies associated to the resource
	 */
	public List getCategories() {
		return _categories;
	}
	public void setCategories(List categories) {
		this._categories = categories;
	}
	
	/**
	 * Returns the inputstream associated to the file of this resource
	 * @return the file inputstrean of the resource
	 * @throws Throwable if an error occurs
	 */
	public InputStream getInputStream() throws Throwable {
		return _inputStream;
	}
	public void setInputStream(InputStream inputStream) throws Throwable {
		_inputStream = inputStream ;
	}
		
	/**
	 * Returns the filename of the resource to add
	 * @return the filename of the resource 
	 */
	public String getFileName() {
		return _fileName;
	}
	public void setFileName(String fileName) {
		_fileName = fileName;
	}
	
	/**
	 * The size of the resource
	 * @return the size of the resource
	 */
	public int getFileSize() {
		return _size;
	}
	public void setFileSize(int size) {
		this._size = size;
	}
	
	/**
	 * The id of the resource
	 * @return the id of the resource
	 */
	public String getId() {
		return _id;
	}
	public void setId(String id) {
		this._id = id;
	}
	
	@Override
	public String getResourceId() {
		return getId();
	}
	public void setResourceId(String resourceId) {
		this.setId(resourceId);
	}
	
	/**
	 * the resource mime-type
	 * @return the myme-type of the resource 
	 */
	public String getMimeType() {
		return _mimeType;
	}
	public void setMimeType(String mimeType) {
		this._mimeType = mimeType;
	}
	
	@Override
	public File getFile() {
		File f = new File(this.getFileName());
		try {
			InputStream stream = this.getInputStream();
			OutputStream out = new FileOutputStream(f);
			byte buf[]=new byte[1024];
			int len;
			while((len = stream.read(buf))>0) {
				out.write(buf,0,len);
			}
			out.close();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getFile");
			throw new RuntimeException("Error", t);
		}
		return f;
	}
	
	private String _resourceType;
	private String _descr;
	private String _mainGroup;
	private String _fileName;
	private String _id;
	private int _size;
	private InputStream _inputStream;
	private List _categories;
	private String _mimeType;

}
