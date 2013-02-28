----------------------------------------------------------------------------------
------------------------------ Plugin jpmyportal ----------------------------
----------------------------------------------------------------------------------


----------------------------------------------
----------------- DB Port --------------------
----------------------------------------------

CREATE TABLE jpmyportal_userpagemodelconfig
(
  username character varying(40) NOT NULL,
  pagemodelcode character varying(40) NOT NULL,
  framepos integer NOT NULL,
  showletcode character varying(40) NOT NULL,
  config character varying,
  CONSTRAINT jpmyportal_userpagemodelconfig_pkey PRIMARY KEY (username, framepos, pagemodelcode),
  CONSTRAINT jpmyportal_userpagemodelconfig_pagemodelcode_fkey FOREIGN KEY (pagemodelcode)
      REFERENCES pagemodels (code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS=TRUE);

INSERT INTO sysconfig ( version, item, descr, config ) VALUES ( 'production', 'jpmyportal_config', 'Definizione degli oggetti configurabili di MyPortal', '<?xml version="1.0" encoding="UTF-8"?>
<myportalConfig>
	<showlets>
		<showlet code="jpmyportal_void" />
	</showlets>
</myportalConfig>' );


INSERT INTO showletcatalog ( code, titles, plugincode, locked ) VALUES ( 'jpmyportal_void', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Void</property>
<property key="it">Vuoto</property>
</properties>', 'jpmyportal', 1 );


INSERT INTO showletcatalog ( code, titles, plugincode, parenttypecode, defaultconfig, locked ) VALUES ( 'jpmyportal_frame_config', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="en">Configura Frame</property>
	<property key="it">Configure Frame</property>
</properties>', 'jpmyportal', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="actionPath">/ExtStr2/do/Front/jpmyportal/MyPortal/configureFrame.action</property>
</properties>', 1 );


INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_ADD_SHOWLET', 'it', 'Scegli un widget da posizionare nel box' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_ADD_SHOWLET', 'en', 'Select the widget that will be placed in this box' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_BACK', 'it', 'Annulla' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_BACK', 'en', 'Cancel' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_BOX', 'it', 'Box' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_BOX', 'en', 'Box' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_BOX_CONFIGURE_SHOWLET_TITLE', 'it', 'Configura il box' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_BOX_CONFIGURE_SHOWLET_TITLE', 'en', 'Configure this box' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_BOX_NUMBER', 'it', 'Box numero' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_BOX_NUMBER', 'en', 'Box number' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_CHOOSE', 'it', 'Seleziona' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_CHOOSE', 'en', 'Select' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_DELETE_SHOWLET', 'it', 'Elimina il widget posizionato nel box' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_DELETE_SHOWLET', 'en', 'Remove the Widget placed in this box' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_EMPTY', 'it', 'Svuota' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_EMPTY', 'en', 'Empty' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_EXCHANGE', 'it', 'Scambia' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_EXCHANGE', 'en', 'Swap' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_EXCHANGE_TO', 'it', 'Scambia con' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_EXCHANGE_TO', 'en', 'Swap with' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_NEW', 'it', 'Aggiungi showlet' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_NEW', 'en', 'Add Showlet' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_OPTIONS', 'it', 'Opzioni' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_OPTIONS', 'en', 'Options' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_PAGEMODEL', 'it', 'Modello di Pagina' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_PAGEMODEL', 'en', 'Page Model' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_RESET_TO_DEFAULT', 'it', 'Ripristina widget di default' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_RESET_TO_DEFAULT', 'en', 'Restore Default Widgets' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_SELECTED_SHOWLET', 'it', 'Stai configurando' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_SELECTED_SHOWLET', 'en', 'You are configuring' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpmyportal_SHOWLET', 'it', 'Showlet' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES( 'jpmyportal_SHOWLET', 'en', 'Showlet' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpmyportal_LOGIN_TO_USE_THIS_SERVICE', 'it','Effettuare l''accesso per accedere a questo servizio.');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpmyportal_LOGIN_TO_USE_THIS_SERVICE', 'en','Please Login in order to use this service.');

