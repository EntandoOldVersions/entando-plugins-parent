UPDATE sysconfig
   SET config='<?xml version="1.0" encoding="UTF-8"?>
<contenttypes>
	<contenttype typecode="GEO" typedescr="georef content" viewpage="contentview" listmodel="2" defaultmodel="2">
		<attributes>
			<attribute name="title" attributetype="Monotext">
				<validations>
					<required>true</required>
				</validations>
			</attribute>
			<attribute name="choords" attributetype="Coords">
				<validations>
					<required>true</required>
				</validations>
			</attribute>
		</attributes>
	</contenttype>
	<contenttype typecode="ART" typedescr="Articolo rassegna stampa" viewpage="contentview" listmodel="11" defaultmodel="1">
		<attributes>
			<attribute name="Titolo" attributetype="Text" indexingtype="text">
				<validations>
					<required>true</required>
				</validations>
			</attribute>
			<list name="Autori" attributetype="Monolist">
				<nestedtype>
					<attribute name="Autori" attributetype="Monotext" />
				</nestedtype>
			</list>
			<attribute name="VediAnche" attributetype="Link" />
			<attribute name="CorpoTesto" attributetype="Hypertext" indexingtype="text" />
			<attribute name="Foto" attributetype="Image" />
			<attribute name="Data" attributetype="Date" searcheable="true" />
			<attribute name="Numero" attributetype="Number" searcheable="true" />
		</attributes>
	</contenttype>
	<contenttype typecode="EVN" typedescr="Evento" viewpage="contentview" listmodel="51" defaultmodel="5">
		<attributes>
			<attribute name="Titolo" attributetype="Text" searcheable="true" indexingtype="text" />
			<attribute name="CorpoTesto" attributetype="Hypertext" indexingtype="text" />
			<attribute name="DataInizio" attributetype="Date" searcheable="true" />
			<attribute name="DataFine" attributetype="Date" searcheable="true" />
			<attribute name="Foto" attributetype="Image" />
			<list name="LinkCorrelati" attributetype="Monolist">
				<nestedtype>
					<attribute name="LinkCorrelati" attributetype="Link" />
				</nestedtype>
			</list>
		</attributes>
	</contenttype>
	<contenttype typecode="RAH" typedescr="Tipo_Semplice" viewpage="contentview" listmodel="126" defaultmodel="457">
		<attributes>
			<attribute name="Titolo" attributetype="Text" indexingtype="text">
				<validations>
					<minlength>10</minlength>
					<maxlength>100</maxlength>
				</validations>
			</attribute>
			<attribute name="CorpoTesto" attributetype="Hypertext" indexingtype="text" />
			<attribute name="Foto" attributetype="Image" />
			<attribute name="email" attributetype="Monotext">
				<validations>
					<regexp><![CDATA[.+@.+.[a-z]+]]></regexp>
				</validations>
			</attribute>
			<attribute name="Numero" attributetype="Number" />
			<attribute name="Correlati" attributetype="Link" />
			<attribute name="Allegati" attributetype="Attach" />
			<attribute name="Checkbox" attributetype="CheckBox" />
		</attributes>
	</contenttype>
</contenttypes>'
 WHERE version='test' AND item='contentTypes';

INSERT INTO contents(
            contentid, contenttype, descr, status, workxml, created, lastmodified, 
            onlinexml, maingroup, currentversion, lasteditor)
    VALUES ('GEO4','GEO','dfdfd','PUBLIC','<?xml version="1.0" encoding="UTF-8"?>
<content id="GEO4" typecode="GEO" typedescr="georef content"><descr>dfdfd</descr><groups mainGroup="free" /><categories /><attributes><attribute name="title" attributetype="Monotext"><monotext>dfdfd</monotext></attribute><attribute name="choords" attributetype="Coords"><x>1.2</x><y>2.3</y><z>3.4</z></attribute></attributes><status>PUBLIC</status><version>1.0</version><lastEditor>admin</lastEditor><created>20111205115534</created><lastModified>20111205115534</lastModified></content>
','20111205115534','20111205115534','<?xml version="1.0" encoding="UTF-8"?>
<content id="GEO4" typecode="GEO" typedescr="georef content"><descr>dfdfd</descr><groups mainGroup="free" /><categories /><attributes><attribute name="title" attributetype="Monotext"><monotext>dfdfd</monotext></attribute><attribute name="choords" attributetype="Coords"><x>1.2</x><y>2.3</y><z>3.4</z></attribute></attributes><status>PUBLIC</status><version>1.0</version><lastEditor>admin</lastEditor><created>20111205115534</created><lastModified>20111205115534</lastModified></content>
','free','1.0','admin');