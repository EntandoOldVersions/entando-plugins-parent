INSERT INTO showletcatalog ( code, titles, parameters, plugincode, locked ) VALUES ( 'jpshowletreplicator', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Showlet Replicator</property>
<property key="it">Replicatore di Showlet</property>
</properties>', '<config>
	<parameter name="pageCodeParam">Page</parameter>
	<parameter name="frameIdParam">Frame</parameter>
	<action name="replicatorConfig"/>
</config>', 'jpshowletreplicator', 1 );