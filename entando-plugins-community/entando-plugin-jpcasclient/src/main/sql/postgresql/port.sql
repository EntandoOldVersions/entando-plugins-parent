-- Port Production Database
INSERT INTO 
showletcatalog(code, titles, parameters, plugincode, 
		parenttypecode, defaultconfig, locked)
    VALUES ('jpcasclient_login', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Login - with CAS</property>
<property key="it">Login - con CAS</property>
</properties>', NULL, 'jpcasclient', NULL, NULL, 1);