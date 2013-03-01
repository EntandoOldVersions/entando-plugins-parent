INSERT INTO showletcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpfrontshortcut_content_viewer', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Contents - Publish a Content</property>
<property key="it">Contenuti - Pubblica un Contenuto</property>
</properties>', '<config>
	<parameter name="contentId">Content ID</parameter>
	<parameter name="modelId">Content Model ID</parameter>
	<action name="viewerConfig"/>
</config>', 'jpfrontshortcut', NULL, NULL, 1, NULL);
INSERT INTO showletcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpfrontshortcut_navigation_menu', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Navigation - Vertical Menu</property>
<property key="it">Navigazione - Menù Verticale</property>
</properties>', '<config>
<parameter name="navSpec">Rules for the Page List auto-generation</parameter>
<action name="navigatorConfig" />
</config>', 'jpfrontshortcut', NULL, NULL, 1, NULL);
INSERT INTO showletcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpfrontshortcut_info', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Info</property>
<property key="it">Info</property>
</properties>', NULL, 'jpfrontshortcut', NULL, NULL, 1, NULL);


INSERT INTO pagemodels (code, descr, frames, plugincode) VALUES ('jpfrontshortcut_test', 'jpfrontshortcut plugin - Test', '<frames>
	<frame locked="true" pos="0">
		<descr>Frame I</descr>
	</frame>
	<frame locked="true" pos="1">
		<descr>Frame II</descr>
	</frame>
	<frame locked="true" pos="2">
		<descr>Frame III</descr>
	</frame>
	<frame locked="true" pos="3">
		<descr>Frame IV</descr>
	</frame>
	<frame locked="true" pos="4">
		<descr>Frame V</descr>
	</frame>
	<frame locked="true" pos="5">
		<descr>Frame VI</descr>
	</frame>
	<frame locked="true" pos="6">
		<descr>Frame VII</descr>
	</frame>
	<frame locked="true" pos="7">
		<descr>Frame VIII</descr>
	</frame>
	<frame locked="true" pos="8">
		<descr>Frame IX</descr>
	</frame>
	<frame locked="true" pos="9">
		<descr>Frame X</descr>
	</frame>
</frames>', 'jpfrontshortcut');


INSERT INTO pages (code, parentcode, pos, modelcode, titles, groupcode, showinmenu, extraconfig) VALUES ('jpfrontshortcut_test', 'homepage', 7, 'jpfrontshortcut_test', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Front shortcut Test Page</property>
<property key="it">Front shortcut Test Page</property>
</properties>', 'free', 1, '<?xml version="1.0" encoding="UTF-8"?>
<config>
  <useextratitles>false</useextratitles>
</config>');

INSERT INTO showletconfig (pagecode, framepos, showletcode, config, publishedcontent) VALUES ('jpfrontshortcut_test', 1, 'jpfrontshortcut_navigation_menu', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="navSpec">code(homepage).subtree(2)</property>
</properties>', NULL);
INSERT INTO showletconfig (pagecode, framepos, showletcode, config, publishedcontent) VALUES ('jpfrontshortcut_test', 5, 'jpfrontshortcut_info', NULL, NULL);