/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.plugins.jpwebdynamicform.aps.system.orm.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import org.entando.entando.aps.system.orm.IDbInstallerManager;
import org.entando.entando.aps.system.orm.model.ExtendedColumnDefinition;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Answer.TABLE_NAME)
public class Answer implements ExtendedColumnDefinition {
	
	public Answer() {}
	
	@DatabaseField(columnName = "answerid", 
			width = 16, 
			canBeNull = false, id = true)
	private String _answerId;
	
	@DatabaseField(foreign = true, columnName = "messageid", 
			width = 16, 
			canBeNull = false)
	private Message _messageId;
	
	@DatabaseField(columnName = "operator", 
			dataType = DataType.STRING, 
			width = 20)
	private String _operator;
	
	@DatabaseField(columnName = "senddate", 
			dataType = DataType.DATE,
			canBeNull = false)
	private Date _sendDate;
	
	@DatabaseField(columnName = "text", 
			dataType = DataType.LONG_STRING,
			canBeNull = false)
	private String _text;
	
	@Override
	public String[] extensions(IDbInstallerManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String messageTableName = Message.TABLE_NAME;
		if (IDbInstallerManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			messageTableName = "`" + messageTableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_fkey FOREIGN KEY (messageid) "
				+ "REFERENCES " + messageTableName + " (messageid)"};
	}
	
	public static final String TABLE_NAME = "jpwebdynamicform_messageanswers";
	
}
/*
CREATE TABLE jpwebdynamicform_messageanswers
(
  answerid character varying(16) NOT NULL,
  messageid character varying(16) NOT NULL,
  "operator" character varying(20),
  senddate timestamp without time zone NOT NULL,
  "text" character varying NOT NULL,
  CONSTRAINT jpwebdynamicform_messageanswers_pkey PRIMARY KEY (answerid),
  CONSTRAINT jpwebdynamicform_messageanswers_messageid_fkey FOREIGN KEY (messageid)
      REFERENCES jpwebdynamicform_messages (messageid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
 */