INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'jpmail_config', 'Configurazione del servizio di invio eMail', '<?xml version="1.0" encoding="UTF-8"?>
<mailConfig>
	<active>true</active>
	<senders>
		<sender code="CODE1">EMAIL1@EMAIL.COM</sender>
		<sender code="CODE2">EMAIL2@EMAIL.COM</sender>
	</senders>
	<smtp debug="true">
		<host>localhost</host>
		<port>25000</port>
		<user></user>
		<password></password>
		<security>std</security>
	</smtp>
</mailConfig>');