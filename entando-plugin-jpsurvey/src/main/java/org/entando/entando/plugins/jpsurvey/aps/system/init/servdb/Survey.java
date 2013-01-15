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
package org.entando.entando.plugins.jpsurvey.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Survey.TABLE_NAME)
public class Survey {
	
	public Survey() {}
	
	@DatabaseField(columnName = "id", 
			dataType = DataType.INTEGER, 
			canBeNull = false, id = true)
	private int _id;
	
	@DatabaseField(columnName = "description", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _description;
	
	@DatabaseField(columnName = "maingroup", 
			dataType = DataType.STRING, 
			width = 20)
	private String _mainGroup;
	
	@DatabaseField(columnName = "startdate", 
			dataType = DataType.DATE, 
			canBeNull = false)
	private Date _startDate;
	
	@DatabaseField(columnName = "enddate", 
			dataType = DataType.DATE)
	private Date _endDate;
	
	@DatabaseField(columnName = "active", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
	private int _active;
	
	@DatabaseField(columnName = "publicpartialresult", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
	private int _publicPartialResult;
	
	@DatabaseField(columnName = "publicresult", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
	private int _publicResult;
	
	@DatabaseField(columnName = "questionnaire", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
	private int _questionnaire;
	
	@DatabaseField(columnName = "gatheruserinfo", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
	private int _gatherUserInfo;
	
	@DatabaseField(columnName = "title", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _title;
	
	@DatabaseField(columnName = "restrictedaccess", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
	private int _restrictedAccess;
	
	@DatabaseField(columnName = "checkcookie", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
	private int _checkCookie;
	
	@DatabaseField(columnName = "checkipaddress", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
	private int _checkIpAddress;
	
	@DatabaseField(columnName = "imageid", 
			dataType = DataType.STRING, 
			width = 16)
	private String _imageid;
	
	@DatabaseField(columnName = "imagedescr", 
			dataType = DataType.LONG_STRING)
	private String _imageDescription;
	
	public static final String TABLE_NAME = "jpsurvey";
	
}
/*
CREATE TABLE jpsurvey
(
  id integer NOT NULL,
  description character varying NOT NULL,
  maingroup character varying(20) NOT NULL,
  startdate date NOT NULL,
  enddate date,
  active smallint NOT NULL,
  publicpartialresult smallint NOT NULL,
  publicresult smallint NOT NULL,
  questionnaire smallint NOT NULL,
  gatheruserinfo smallint NOT NULL,
  title character varying NOT NULL,
  restrictedaccess smallint NOT NULL,
  checkcookie smallint NOT NULL,
  checkipaddress smallint NOT NULL,
  imageid character varying(16),
  imagedescr character varying,
  CONSTRAINT jpsurvey_pkey PRIMARY KEY (id)
) ;
 */