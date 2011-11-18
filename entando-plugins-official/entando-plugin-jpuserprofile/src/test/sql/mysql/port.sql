INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'jpuserprofileProfileType', 'Definizione del profilo utente', '<profiletypes>
	<profiletype typecode="PFL" typedescr="Profilo utente/cittadino tipo" >
		<attributes>
			<attribute name="Name" attributetype="Monotext" searcheable="true" >
				<validations>
					<required>true</required>
				</validations>
				<roles>
					<role>jpuserprofile:firstname</role>
				</roles>
			</attribute>
			<attribute name="Surname" attributetype="Monotext" searcheable="true" >
				<validations>
					<required>true</required>
				</validations>
				<roles>
					<role>jpuserprofile:surname</role>
				</roles>
			</attribute>
			<attribute name="email" attributetype="Monotext" searcheable="true" >
				<validations>
					<required>true</required>
					<regexp><![CDATA[.+@.+.[a-z]+]]></regexp>
				</validations>
				<roles>
					<role>jpuserprofile:mail</role>
				</roles>
			</attribute>
			<attribute name="birthdate" attributetype="Date" required="true" searcheable="true"/>
			<attribute name="language" attributetype="Monotext" required="true"/>
			<attribute name="boolean1" attributetype="Boolean" searcheable="true"/>
			<attribute name="boolean2" attributetype="Boolean" searcheable="true"/>
		</attributes>
	</profiletype>
</profiletypes>');