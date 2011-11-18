INSERT INTO showletcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpphotogallery', '<?xml version="1.0" encoding="UTF-8"?>
	<properties>
		<property key="en">Photo Gallery</property>
		<property key="it">Galleria Fotografica</property>
	</properties>', '<config>
	<parameter name="contentType">Content Type (mandatory)</parameter>
	<parameter name="modelIdMaster">Main Model</parameter>
	<parameter name="modelIdPreview">Preview Model</parameter>
	<parameter name="category">Category</parameter>
	<parameter name="filters" />
	<action name="jpphotogalleryConfig"/>
</config>', 'jpphotogallery', NULL, NULL, 1);