-- Port Test Script 
INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'jpcalendar_Config', 'Configurazione Calendario', '<calendarConfig>
	<contentType code="EVN" />
	<dateAttributes>
		<start name="DataInizio" />
		<end name="DataFine" />
	</dateAttributes>
</calendarConfig>');

/*
 * Vedere note per come deve essere configurato il tipo di contenuto
 * 
 * See notes for how to set the content type
 */