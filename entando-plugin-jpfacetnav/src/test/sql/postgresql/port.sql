INSERT INTO showletcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpfacetnav_results', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Results</property>
<property key="it">Risultati Ricerca</property>
</properties>', '<config>
	<parameter name="contentTypesFilter">
		Tipi di contenuto dei risultati da erogare (OPZIONALE)
	</parameter>
	<action name="facetNavResultConfig"/>
</config>', 'jpfacetnav', NULL, NULL, 1);

INSERT INTO showletcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpfacetnav_tree', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Facets Tree</property>
<property key="it">Albero delle faccette</property>
</properties>', '<config>
	<parameter name="facetRootNodes">
		Faccette radice da visualizzare nell''albero
	</parameter>
	<parameter name="contentTypesFilter">
		Tipi di contenuto dei risultati da erogare (OPZIONALE)
	</parameter>
	<action name="facetNavTreeConfig"/>
</config>', 'jpfacetnav', NULL, NULL, 1);