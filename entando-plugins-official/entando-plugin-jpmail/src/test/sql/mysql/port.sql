INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'jpmail_config', 'Configurazione del servizio di invio eMail', '<mailConfig>
	<senders>
		<sender code="CODE1">EMAIL1@EMAIL.COM</sender>
		<sender code="CODE2">EMAIL2@EMAIL.COM</sender>
	</senders>
	<smtp debug="true" >
		<host>out.virgilio.it</host>
		<user></user>
		<password></password>
	</smtp>
</mailConfig>');