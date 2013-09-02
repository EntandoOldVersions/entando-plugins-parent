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

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig,locked) VALUES (
'jpmyportalplus_void',
'<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">My Portal - Void</property>
<property key="it">My Portal - Vuoto</property>
</properties>',null,'jpmyportalplus',null,null,1);

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig,locked) VALUES (
'jpmyportalplus_sample_showlet',
'<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">My Portal - Sample Widget</property>
<property key="it">My Portal - Widget di Esempio</property>
</properties>',null,'jpmyportalplus',null,null,1);

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig,locked) VALUES (
'jpmyportalplus_test_showlet_1',
'<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">My Portal - Test Widget 1</property>
<property key="it">My Portal - Widget di Test 1</property>
</properties>',null,'jpmyportalplus',null,null,1);

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig,locked) VALUES (
'jpmyportalplus_test_showlet_2',
'<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">My Portal - Test Widget 2</property>
<property key="it">My Portal - Widget di Test 2</property>
</properties>',null,'jpmyportalplus',null,null,1);

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig,locked) VALUES (
'jpmyportalplus_test_showlet_3',
'<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">My Portal - Test Widget 3</property>
<property key="it">My Portal - Widget di Test 3</property>
</properties>',null,'jpmyportalplus',null,null,1);

INSERT INTO sysconfig (version, item, descr, config) VALUES ( 'test', 'jpmyportalplus_config', 'Definizione degli oggetti configurabili di My Portal', '<?xml version="1.0" encoding="UTF-8"?>
<myportalConfig>
	<showlets>
		<showlet code="jpmyportalplus_sample_showlet" />
		<showlet code="jpmyportalplus_test_showlet_1" />
		<showlet code="jpmyportalplus_test_showlet_3" />
	</showlets>
</myportalConfig>' );

INSERT INTO pages (code, parentcode, pos, modelcode, titles, groupcode, showinmenu, extraconfig) VALUES (
'jpmyportalplus_testpage', 'homepage', 5, 'jpmyportalplus_pagemodel', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Test Page</property>
<property key="it">Test Page</property>
</properties>
', 'free', 0, NULL);

INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config, publishedcontent) VALUES (
'jpmyportalplus_testpage', 0, 'login_form', NULL, NULL);
INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config, publishedcontent) VALUES (
'jpmyportalplus_testpage', 1, 'jpmyportalplus_sample_showlet', NULL, NULL);
INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config, publishedcontent) VALUES (
'jpmyportalplus_testpage', 2, 'jpmyportalplus_void', NULL, NULL);
INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config, publishedcontent) VALUES (
'jpmyportalplus_testpage', 4, 'jpmyportalplus_test_showlet_3', NULL, NULL);


INSERT INTO jpmyportalplus_userpageconfig(username, pagecode, framepos, widgetcode, config, closed)
    VALUES ('editorCustomers', 'jpmyportalplus_testpage',
    1, 'jpmyportalplus_void', null, 0);

INSERT INTO jpmyportalplus_userpageconfig(username, pagecode, framepos, widgetcode, config, closed)
    VALUES ('editorCustomers', 'jpmyportalplus_testpage',
    2, 'jpmyportalplus_test_showlet_1', null, 1);

INSERT INTO jpmyportalplus_userpageconfig(username, pagecode, framepos, widgetcode, config, closed)
    VALUES ('editorCustomers', 'jpmyportalplus_testpage',
    3, 'jpmyportalplus_sample_showlet', null, 0);

INSERT INTO jpmyportalplus_userpageconfig(username, pagecode, framepos, widgetcode, config, closed)
    VALUES ('editorCustomers', 'jpmyportalplus_testpage',
    6, 'jpmyportalplus_test_showlet_3', null, 1);
