-- Port Production Database
INSERT INTO 
showletcatalog(code, titles, parameters, plugincode, 
		parenttypecode, defaultconfig, locked)
    VALUES ('jpcasclient_login', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Login - with CAS</property>
<property key="it">Login - con CAS</property>
</properties>', NULL, 'jpcasclient', NULL, NULL, 1);

INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'jpcasclient_config', 'Configurazione del servizio di integrazione con il CAS server', 
'<?xml version="1.0" encoding="UTF-8"?>
<casclientConfig>
	<active>false</active>
	<casLoginURL>http://japs.intranet:8080/cas/login</casLoginURL>
	<casLogoutURL>http://japs.intranet:8080/cas/logout</casLogoutURL>
	<casValidateURL>http://japs.intranet:8080/cas/validate</casValidateURL>
	<serverBaseURL>http://japs.intranet:8080</serverBaseURL>
	<notAuthPage>notauth</notAuthPage>
	<realm>demo.entando.com</realm>
</casclientConfig>');