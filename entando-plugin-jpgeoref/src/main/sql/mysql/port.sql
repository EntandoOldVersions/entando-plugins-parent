INSERT INTO showletcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpgeoref_GoogleListViewer', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Publish contents on Map</property>
<property key="it">Pubblicazione contenuti su Mappa</property>
</properties>', '<config>
	<parameter name="contentType">Tipo di contenuto (obbligatorio)</parameter>
	<parameter name="modelId">Modello di contenuto</parameter>
	<parameter name="category">Categoria del contenuto</parameter>
	<parameter name="filters" />
	<action name="listViewerConfig"/>
</config>', 'jpgeoref', NULL, NULL, 1);

INSERT INTO showletcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpgeoref_GoogleRoute', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Route</property>
<property key="it">Tragitto su Mappa</property>
</properties>', '<config>
	<parameter name="contentsId">
		Identificativi di Contenuto separato da virgola
	</parameter>
	<parameter name="listModelId">
		Id Modello per contenuti in lista semplice
	</parameter>
	<parameter name="markerModelId">
		Id Modello per i marcatori dei contenuti su mappa
	</parameter>
	<action name="configSimpleParameter"/>
</config>', 'jpgeoref', NULL, NULL, 1);