INSERT INTO showletcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpuserprofile_editCurrentUser', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Edit Current User</property>
<property key="it">Edita Utente Corrente</property>
</properties>', NULL, 'jpuserprofile', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpuserprofile/Front/CurrentUser/edit.action</property>
</properties>', 1);
INSERT INTO showletcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpuserprofile_editCurrentUser_password', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Edit Current User Password</property>
<property key="it">Edita Password Utente Corrente</property>
</properties>', NULL, 'jpuserprofile', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpuserprofile/Front/CurrentUser/editPassword.action</property>
</properties>', 1);
INSERT INTO showletcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpuserprofile_editCurrentUser_profile', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Edit Current User Profile</property>
<property key="it">Edita Profilo Utente Corrente</property>
</properties>', NULL, 'jpuserprofile', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpuserprofile/Front/CurrentUser/Profile/edit.action</property>
</properties>', 1);

insert into sysconfig (version, item, descr, config) values ('production', 'jpuserprofileProfileType', 'Definizione del profilo utente', '<?xml version="1.0" encoding="UTF-8"?>
<profiletypes>
	<profiletype typecode="PFL" typedescr="Profilo utente/cittadino tipo">
		<attributes>
			<attribute name="Name" attributetype="Monotext" searcheable="true">
				<validations>
					<required>true</required>
				</validations>
				<roles>
					<role>jpuserprofile:firstname</role>
				</roles>
			</attribute>
			<attribute name="Surname" attributetype="Monotext" searcheable="true">
				<validations>
					<required>true</required>
				</validations>
				<roles>
					<role>jpuserprofile:surname</role>
				</roles>
			</attribute>
			<attribute name="email" attributetype="Monotext" searcheable="true">
				<validations>
					<required>true</required>
					<regexp><![CDATA[.+@.+.[a-z]+]]></regexp>
				</validations>
				<roles>
					<role>jpuserprofile:mail</role>
				</roles>
			</attribute>
			<attribute name="language" attributetype="Monotext">
				<validations>
					<required>true</required>
				</validations>
			</attribute>
			<attribute name="boolean1" attributetype="Boolean" searcheable="true" />
			<attribute name="boolean2" attributetype="Boolean" searcheable="true" />
		</attributes>
	</profiletype>
</profiletypes>');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_CONFIRM_NEWPASS','it','Conferma nuova password');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_CONFIRM_NEWPASS','en','Confirm new password');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_EDITPASSWORD','it','Modifica Password');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_EDITPASSWORD','en','Edit Password');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_EDITPASSWORD_TITLE','it','Modifica Password');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_EDITPASSWORD_TITLE','en','Edit Password');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_EDITPROFILE_TITLE','it','Modifica profilo');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_EDITPROFILE_TITLE','en','Edit Profile');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_ITEM_MOVEUP','it','Sposta su');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_ITEM_MOVEUP','en','Move up');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_ITEM_MOVEUP_IN','it','Sposta su in posizione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_ITEM_MOVEUP_IN','en','Move at position');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_ITEM_MOVEDOWN','it','Sposta giu');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_ITEM_MOVEDOWN','en','Move down');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_ITEM_MOVEDOWN_IN','it','Sposta giu in posizione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_ITEM_MOVEDOWN_IN','en','Move down at position');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_ITEM_REMOVE','it','Rimuovi dalla lista'); 
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_ITEM_REMOVE','en','Remove from list'); 

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_MESSAGE_TITLE_FIELDERRORS','it','Attenzione, si sono verificati i seguenti errori nella compilazione del modulo');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_MESSAGE_TITLE_FIELDERRORS','en','Warning, please check the module');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_NEWPASS','it','Nuova password');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_NEWPASS','en','New password');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_OLDPASSWORD','it','Vecchia password');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_OLDPASSWORD','en','Old password');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_PASSWORD_UPDATED','it','La password è stata aggiornata correttamente.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_PASSWORD_UPDATED','en','Your password updated successfully.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_PLEASE_LOGIN','it','E'' necessario effettuare l''accesso');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_PLEASE_LOGIN','en','Please login');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_PLEASE_LOGIN_AGAIN','it','E'' necessario riloggarsi.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_PLEASE_LOGIN_AGAIN','en','Please logout and login again.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_PLEASE_LOGIN_TO_EDIT_PASSWORD','it','E'' necessario effettuare l''accesso per cambiare la password');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_PLEASE_LOGIN_TO_EDIT_PASSWORD','en','Please login in order to change your password');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_PROFILE_UPDATED','it','Profilo aggiornato correttamente.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_PROFILE_UPDATED','en','Your profile is now updated.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_SAVE_PASSWORD','it','Salva password');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_SAVE_PASSWORD','en','Save password');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_SAVE_PROFILE','it','Salva il profilo');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_SAVE_PROFILE','en','Save profile');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_ADDITEM_LIST','it','Aggiungi nuovo elemento alla lista');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_ADDITEM_LIST','en','Add an element to the list');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_PUBLICCHECK','it','Il mio profilo è pubblico');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_PUBLICCHECK','en','My profile is public');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_YES','it','Si');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_YES','en','Yes');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_NO','it','No');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_NO','en','No');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_BOTH_YES_AND_NO','it','Indifferente');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_BOTH_YES_AND_NO','en','Both');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_ENTITY_ATTRIBUTE_MANDATORY_SHORT', 'it', '*');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_ENTITY_ATTRIBUTE_MANDATORY_SHORT', 'en', '*');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_ENTITY_ATTRIBUTE_MANDATORY_FULL', 'it', 'Obbligatorio');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_ENTITY_ATTRIBUTE_MANDATORY_FULL', 'en', 'Mandatory');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_ENTITY_ATTRIBUTE_MINLENGTH_SHORT', 'it', 'Min');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_ENTITY_ATTRIBUTE_MINLENGTH_SHORT', 'en', 'Min');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_ENTITY_ATTRIBUTE_MINLENGTH_FULL', 'it', 'Lunghezza Minima');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_ENTITY_ATTRIBUTE_MINLENGTH_FULL', 'en', 'Minimum length');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_ENTITY_ATTRIBUTE_MAXLENGTH_SHORT', 'it', 'Max');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_ENTITY_ATTRIBUTE_MAXLENGTH_SHORT', 'en', 'Max');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_ENTITY_ATTRIBUTE_MAXLENGTH_FULL', 'it', 'Lunghezza Massima');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpuserprofile_ENTITY_ATTRIBUTE_MAXLENGTH_FULL', 'en', 'Maximum length');
