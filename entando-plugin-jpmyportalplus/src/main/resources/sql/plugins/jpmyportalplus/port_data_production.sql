INSERT INTO pagemodels(code, descr, frames, plugincode) VALUES ('jpmyportalplus_pagemodel', 'My Portal', '<frames>
<frame pos="0" locked="true"><descr>Header</descr></frame>
<frame pos="1" column="1" locked="false"><descr>Left Column I</descr><defaultShowlet code="jpmyportalplus_void" /></frame>
<frame pos="2" column="1" locked="false"><descr>Left Column II</descr><defaultShowlet code="jpmyportalplus_void" /></frame>
<frame pos="3" column="2" locked="false"><descr>Middle Column I</descr><defaultShowlet code="jpmyportalplus_void" /></frame>
<frame pos="4" column="2" locked="false"><descr>Middle Column II</descr><defaultShowlet code="jpmyportalplus_void" /></frame>
<frame pos="5" column="3" locked="false"><descr>Right Column I</descr><defaultShowlet code="jpmyportalplus_void" /></frame>
<frame pos="6" column="3" locked="false"><descr>Right Column II</descr><defaultShowlet code="jpmyportalplus_void" /></frame>
<frame pos="7" locked="true"><descr>Footer</descr></frame>
</frames>', 'jpmyportalplus');

INSERT INTO showletcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig,locked) VALUES (
'jpmyportalplus_void',
'<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">My Portal - Void</property>
<property key="it">My Portal - Vuoto</property>
</properties>',null,'jpmyportalplus',null,null,1);

INSERT INTO showletcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig,locked) VALUES (
'jpmyportalplus_sample_showlet',
'<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">My Portal - Sample Showlet</property>
<property key="it">My Portal - Showlet di Esempio</property>
</properties>',null,'jpmyportalplus',null,null,1);

INSERT INTO sysconfig (version, item, descr, config) VALUES ( 'production', 'jpmyportalplus_config', 'Definizione degli oggetti configurabili di My Portal', '<?xml version="1.0" encoding="UTF-8"?>
<myportalConfig>
	<showlets>
		<showlet code="jpmyportalplus_sample_showlet" />
	</showlets>
</myportalConfig>' );

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_APPLY','en','Apply');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_APPLY','it','Applica');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_CONFIG_INTRO','en','Choose which content you want to add in this page');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_CONFIG_INTRO','it','Scegli quali contenuti mostrare nella pagina');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_CONFIGMYHOME','en','Page Content Configuration');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_CONFIGMYHOME','it','Configura la Pagina');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_INSERTINTOCOLUMN','en','Inserting it into column');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_INSERTINTOCOLUMN','it','Inserendolo nella colonna');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_MOVE','en','Move');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_MOVE','it','Sposta');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_RESET','en','Reset the Page');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_RESET','it','Reimposta la pagina');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_MOVETHISSHOWLET','en','Move this box');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_MOVETHISSHOWLET','it','Sposta questo box');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_RESET_INTRO','en','If you want to discard the current configuration you can reset the page.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_RESET_INTRO','it','Se desideri riportare la pagina alla configurazione predefinita, puoi resettare le impostazioni.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_SWAPITWITH','en','Swap it with');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_SWAPITWITH','it','Scambiandolo con');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_LOADING_INFO', 'it', 'Caricamento informazioni in corso...');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_LOADING_INFO', 'en', 'Loading...');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_ERROR_INFO', 'it', 'Si è verificato un errore, riprovare.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_ERROR_INFO', 'en', 'An error has occurred, retry.');
