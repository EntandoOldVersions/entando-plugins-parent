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