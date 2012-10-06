/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.plugins.jpuserreg.aps.system.orm.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = ActivationToken.TABLE_NAME)
public class ActivationToken {
	
	public ActivationToken() {}
	
	@DatabaseField(columnName = "username", 
			dataType = DataType.STRING, 
			width = 40, 
			canBeNull = false, id = true)
	private String _username;
	
	@DatabaseField(columnName = "token", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _token;
	
	@DatabaseField(columnName = "regtime", 
			dataType = DataType.DATE, 
			canBeNull = false)
	private Date _registrationTime;
	
	@DatabaseField(columnName = "tokentype", 
			dataType = DataType.STRING, 
			width = 25, 
			canBeNull = false)
	private String _tokentype;
	
	public static final String TABLE_NAME = "jpuserreg_activationtokens";
	
}
/*
CREATE TABLE jpuserreg_activationtokens
(
  username character varying(40) NOT NULL,
  token character varying NOT NULL,
  regtime timestamp without time zone NOT NULL,
  tokentype character varying(25) NOT NULL,
  CONSTRAINT jpuserreg_activationtokens_pkey PRIMARY KEY (username)
);
 */