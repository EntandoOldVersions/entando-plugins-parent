INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig,locked) VALUES (
'jpmyportalplus_void',
'<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">My Portal - Void</property>
<property key="it">My Portal - Vuoto</property>
</properties>',null,'jpmyportalplus',null,null,1);

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig,locked) VALUES (
'jpmyportalplus_sample_widget',
'<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">My Portal - Sample Widget</property>
<property key="it">My Portal - Widget di Esempio</property>
</properties>',null,'jpmyportalplus',null,null,1);

INSERT INTO sysconfig (version, item, descr, config) VALUES ( 'production', 'jpmyportalplus_config', 'Definizione degli oggetti configurabili di My Portal', '<?xml version="1.0" encoding="UTF-8"?>
<myportalConfig>
	<widgets>
		<widget code="jpmyportalplus_sample_widget" />
	</widgets>
</myportalConfig>' );

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_APPLY','en','Apply');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_APPLY','it','Applica');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_CONFIG_INTRO','en','Choose which content you want to add in this page');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_CONFIG_INTRO','it','Scegli quali contenuti mostrare nella pagina');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_CONFIGMYHOME','en','Show Settings');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_CONFIGMYHOME','it','Configura la Pagina');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_INSERTINTOCOLUMN','en','Inserting it into column');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_INSERTINTOCOLUMN','it','Inserendolo nella colonna');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_MOVE','en','Move');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_MOVE','it','Sposta');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_RESET','en','Reset the Page');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_RESET','it','Reimposta la pagina');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_MOVETHISWIDGET','en','Move this box');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_MOVETHISWIDGET','it','Sposta questo box');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_RESET_INTRO','en','If you want to discard the current configuration you can reset the page.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_RESET_INTRO','it','Se desideri riportare la pagina alla configurazione predefinita, puoi resettare le impostazioni.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_SWAPITWITH','en','Swap it with');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_SWAPITWITH','it','Scambiandolo con');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_LOADING_INFO', 'it', 'Caricamento informazioni in corso...');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_LOADING_INFO', 'en', 'Loading...');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_ERROR_INFO', 'it', 'Si è verificato un errore, riprovare.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_ERROR_INFO', 'en', 'An error has occurred, retry.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpmyportalplus_RESET_INTRO', 'it', 'Cancella la configurazione Utente');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpmyportalplus_RESET_INTRO', 'en', 'Reset user configuration');
