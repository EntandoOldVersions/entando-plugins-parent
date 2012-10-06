/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.plugins.jpuserprofile.aps.system.orm.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = UserProfile.TABLE_NAME)
public class UserProfile {
	
	public UserProfile() {}
	
	@DatabaseField(columnName = "username", 
			dataType = DataType.STRING, 
			width = 40, 
			canBeNull = false, id = true)
	private String _username;
	
	@DatabaseField(columnName = "profiletype", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false, index = true)
	private String _profileType;
	
	@DatabaseField(columnName = "profilexml", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _profileCml;
	
	@DatabaseField(columnName = "publicprofile", 
			dataType = DataType.SHORT, 
			canBeNull = false)
	private short _publicProfile;
	
	public static final String TABLE_NAME = "jpuserprofile_authuserprofiles";
	
}
/*
CREATE TABLE jpuserprofile_authuserprofiles
(
  username character varying(40) NOT NULL,
  profiletype character varying(30) NOT NULL,
  profilexml character varying NOT NULL,
  publicprofile smallint NOT NULL,
  CONSTRAINT jpuserprofile_autuserprofiles_pkey PRIMARY KEY (username)
);
 */