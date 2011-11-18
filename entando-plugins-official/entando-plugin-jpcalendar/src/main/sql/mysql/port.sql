-- Port Script
INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'jpcalendar_Config', 'Configurazione Calendario', '<calendarConfig>
	<contentType code="<CONTENT_TYPE_ID>" />
	<dateAttributes>
		<start name="<NOME_ATTRIBUTO_DATA_INIZIO>" />
		<end name="<NOME_ATTRIBUTO_DATA_FINE>" />
	</dateAttributes>
</calendarConfig>');

/*
 * Vedere note per come deve essere configurato il tipo di contenuto
 * 
 * See notes for how to set the content type
 */


INSERT INTO showletcatalog(
            code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked)
    VALUES ('jpcalendar_calendar', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Calendar</property>
<property key="it">Calendario</property>
</properties>', NULL, 'jpcalendar', NULL, NULL, 1);

INSERT INTO showletcatalog(
            code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked)
    VALUES ('jpcalendar_dailyEvents', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Events of the Day</property>
<property key="it">Eventi del giorno</property>
</properties>', NULL, 'jpcalendar', NULL, NULL, 1);


INSERT INTO localstrings(keycode, langcode, stringvalue) 
	VALUES 
('jpcalendar_SEARCH_GO' , 'en', 'Search'),
('jpcalendar_SEARCH_GO' , 'it', 'Ricerca'),
('jpcalendar_MONTH_PREVIOUS' , 'en', 'Previuos Month'),
('jpcalendar_MONTH_PREVIOUS' , 'it', 'Mese Precedente'),
('jpcalendar_MONTH_NEXT' , 'en', 'Next Month'),
('jpcalendar_MONTH_NEXT' , 'it', 'Prossimo Mese'),
('jpcalendar_MONTH_CHOOSE' , 'en', 'Select Month'),
('jpcalendar_MONTH_CHOOSE' , 'it', 'Seleziona Mese'),
('jpcalendar_MONTH_JANUARY' , 'en', 'January'),
('jpcalendar_MONTH_JANUARY' , 'it', 'Gennaio'),
('jpcalendar_MONTH_FEBRUARY' , 'en', 'February'),
('jpcalendar_MONTH_FEBRUARY' , 'it', 'Febbraio'),
('jpcalendar_MONTH_MARCH' , 'en', 'March'),
('jpcalendar_MONTH_MARCH' , 'it', 'Marzo'),
('jpcalendar_MONTH_APRIL' , 'en', 'April'),
('jpcalendar_MONTH_APRIL' , 'it', 'Aprile'),
('jpcalendar_MONTH_MAY' , 'en', 'May'),
('jpcalendar_MONTH_MAY' , 'it', 'Maggio'),
('jpcalendar_MONTH_JUNE' , 'en', 'June'),
('jpcalendar_MONTH_JUNE' , 'it', 'Giugno'),
('jpcalendar_MONTH_JULY' , 'en', 'July'),
('jpcalendar_MONTH_JULY' , 'it', 'Luglio'),
('jpcalendar_MONTH_AUGUST' , 'en', 'August'),
('jpcalendar_MONTH_AUGUST' , 'it', 'Agosto'),
('jpcalendar_MONTH_SEPTEMBER' , 'en', 'September'),
('jpcalendar_MONTH_SEPTEMBER' , 'it', 'Settembre'),
('jpcalendar_MONTH_OCTOBER' , 'en', 'October'),
('jpcalendar_MONTH_OCTOBER' , 'it', 'Ottobre'),
('jpcalendar_MONTH_NOVEMBER' , 'en', 'November'),
('jpcalendar_MONTH_NOVEMBER' , 'it', 'Novembre'),
('jpcalendar_MONTH_DECEMBER' , 'en', 'December'),
('jpcalendar_MONTH_DECEMBER' , 'it', 'Dicembre'),
('jpcalendar_YEAR_CHOOSE' , 'en', 'Select Year'),
('jpcalendar_YEAR_CHOOSE' , 'it', 'Seleziona Anno'),
('jpcalendar_SUMMARY' , 'en', 'Event calendar'),
('jpcalendar_SUMMARY' , 'it', 'Calendario eventi'),
('jpcalendar_CAPTION' , 'en', 'Descr event calendar'),
('jpcalendar_CAPTION' , 'it', 'Calendario eventi descr'),
('jpcalendar_WEEK_NUMBER' , 'en', 'week num'),
('jpcalendar_WEEK_NUMBER' , 'it', 'num sett'),
('jpcalendar_WEEK_MONDAY' , 'en', 'mon'),
('jpcalendar_WEEK_MONDAY' , 'it', 'lun'),
('jpcalendar_WEEK_TUESDAY' , 'en', 'tue'),
('jpcalendar_WEEK_TUESDAY' , 'it', 'mar'),
('jpcalendar_WEEK_WEDNESDAY' , 'en', 'wed'),
('jpcalendar_WEEK_WEDNESDAY' , 'it', 'mer'),
('jpcalendar_WEEK_THURSDAY' , 'en', 'thu'),
('jpcalendar_WEEK_THURSDAY' , 'it', 'gio'),
('jpcalendar_WEEK_FRIDAY' , 'en', 'fri'),
('jpcalendar_WEEK_FRIDAY' , 'it', 'ven'),
('jpcalendar_WEEK_SATURDAY' , 'en', 'sat'),
('jpcalendar_WEEK_SATURDAY' , 'it', 'sab'),
('jpcalendar_WEEK_SUNDAY' , 'en', 'sun'),
('jpcalendar_WEEK_SUNDAY' , 'it', 'dom'),
('jpcalendar_EVENTS_TITLE' , 'en', 'Events'),
('jpcalendar_EVENTS_TITLE' , 'it', 'Eventi'),
('jpcalendar_EVENTS_PREVIOUS' , 'en', 'previous events'),
('jpcalendar_EVENTS_PREVIOUS' , 'it', 'precedenti eventi'),
('jpcalendar_EVENTS_NEXT' , 'en', 'next events'),
('jpcalendar_EVENTS_NEXT' , 'it', 'prossimi eventi');