

INSERT INTO sysconfig( version, item, descr, config )
    VALUES ('test', 'jpcontentworkflow_config', 'Workflow dei Contenuti', '<?xml version="1.0" encoding="UTF-8"?>
<contenttypes><contenttype typecode="RAH" role="contentType_RAH" /><contenttype typecode="ART" role="contentType_ART" /><contenttype typecode="EVN" role="contentType_EVN" /></contenttypes>
');


INSERT INTO sysconfig( version, item, descr, config )
    VALUES ('test', 'jpcontentworkflow_notifierConfig', 'Workflow - Servizio di notifica cambio stato Contenuti', '<?xml version="1.0" encoding="UTF-8"?>
<notifierConfig>
	<scheduler>
		<active value="false" />
		<delay value="24" />
		<start value="04/12/2008 16:08" />
	</scheduler>
	<mail senderCode="CODE1" mailAttributeName="email" html="false">
		<subject><![CDATA[[Portale]: Notifica stato contenuti]]></subject>
		<header><![CDATA[Elenco contenuti: Ciao {user},<br />di seguito l\'elenco dei contenuti per cui è richiesto il tuo intervento<br /><br />]]></header>
		<template><![CDATA[<br />Contenuto {type} - {descr} - Stato {status}<br />]]></template>
		<footer><![CDATA[<br />Fine Mail (footer)]]></footer>
	</mail>
</notifierConfig>');

