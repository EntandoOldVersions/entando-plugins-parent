CREATE TABLE jpmyportal_userpagemodelconfig
(
  username character varying(40) NOT NULL,
  pagemodelcode character varying(40) NOT NULL,
  framepos integer NOT NULL,
  widgetcode character varying(40) NOT NULL,
  config longtext,
  CONSTRAINT jpmyportal_userpagemodelconfig_pkey PRIMARY KEY (username, framepos, pagemodelcode),
  CONSTRAINT jpmyportal_userpagemodelconfig_pagemodelcode_fkey FOREIGN KEY (pagemodelcode)
      REFERENCES pagemodels (code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'jpmyportal_config', 'MyPortal widget configuration', '<?xml version="1.0" encoding="UTF-8"?>
<myportalConfig ajaxEnabled="true" >
	<showlets testValid="true" testAllowed="true" >
		<showlet code="jpmyportal_void" />
		<showlet code="content_viewer_list" />
		<showlet code="content_viewer" />
	</showlets>
</myportalConfig>');


INSERT INTO widgetcatalog ( code, titles, plugincode, locked ) VALUES ( 'jpmyportal_void', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Void</property>
<property key="it">Vuoto</property>
</properties>', 'jpmyportal', 1 );

