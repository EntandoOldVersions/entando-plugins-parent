INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'jpmail_config', 'Configurazione del servizio di invio eMail', '<?xml version="1.0" encoding="UTF-8"?>
<mailConfig>
	<senders>
		<sender code="CODE1">EMAIL1@EMAIL.COM</sender>
		<sender code="CODE2">EMAIL2@EMAIL.COM</sender>
	</senders>
	<smtp debug="true">
		<host>out.virgilio.it</host>
		<port>25</port>
		<user></user>
		<password></password>
		<security>std</security>
	</smtp>
</mailConfig>');