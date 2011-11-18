
UPDATE sysconfig SET config = '<?xml version="1.0" encoding="UTF-8"?>
<Params>
	<Param name="urlStyle">classic</Param>
	<Param name="hypertextEditor">fckeditor</Param>
	<SpecialPages>
		<Param name="notFoundPageCode">notfound</Param>
		<Param name="homePageCode">homepage</Param>
		<Param name="errorPageCode">errorpage</Param>
		<Param name="loginPageCode">login</Param>
	</SpecialPages>
	<ExtendendPrivacyModule>
		<Param name="extendedPrivacyModuleEnabled">false</Param>
		<Param name="maxMonthsSinceLastAccess">6</Param>
		<Param name="maxMonthsSinceLastPasswordChange">3</Param>
	</ExtendendPrivacyModule>
	<Versioning>
		<Param name="jpversioning_deleteMidVersions">true</Param>
	</Versioning>
</Params>' WHERE item = 'params';




CREATE TABLE `jpversioning_versionedcontents` (
  `id` int(11) NOT NULL,
  `contentid` varchar(16) NOT NULL,
  `contenttype` varchar(30) NOT NULL,
  `descr` varchar(100) NOT NULL,
  `status` varchar(12) NOT NULL,
  `xml` longtext NOT NULL,
  `versiondate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `version` varchar(7) NOT NULL,
  `onlineversion` int(11) NOT NULL,
  `approved` smallint(6) NOT NULL,
  `username` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `contentid` (`contentid`,`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




CREATE TABLE `jpversioning_trashedresources` (
  `resid` varchar(16) NOT NULL,
  `restype` varchar(30) NOT NULL,
  `descr` varchar(100) NOT NULL,
  `maingroup` varchar(20) NOT NULL,
  `resxml` longtext NOT NULL,
  PRIMARY KEY (`resid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO jpversioning_trashedresources (resid, restype, descr, maingroup, resxml) VALUES ('66', 'Attach', 'configurazione', 'free', '<?xml version="1.0" encoding="UTF-8"?>
<resource typecode="Attach" id="66">
<descr>configurazione</descr>
<groups mainGroup="free" />
<categories />
<instance><size>0</size><filename>configurazione.txt</filename><mimetype>application/msword</mimetype><weight>55 Kb</weight></instance>
</resource>');
INSERT INTO jpversioning_trashedresources (resid, restype, descr, maingroup, resxml) VALUES ('67', 'Image', '219', 'free', '<?xml version="1.0" encoding="UTF-8"?>
<resource typecode="Image" id="67"><descr>219</descr><groups mainGroup="free" /><categories /><instance><size>3</size><filename>DSCN0219_d3.JPG</filename><mimetype>image/jpeg</mimetype><weight>4 Kb</weight></instance><instance><size>2</size><filename>DSCN0219_d2.JPG</filename><mimetype>image/jpeg</mimetype><weight>4 Kb</weight></instance><instance><size>1</size><filename>DSCN0219_d1.JPG</filename><mimetype>image/jpeg</mimetype><weight>2 Kb</weight></instance><instance><size>0</size><filename>DSCN0219_d0.JPG</filename><mimetype>image/jpeg</mimetype><weight>1 Kb</weight></instance></resource>');
INSERT INTO jpversioning_trashedresources (resid, restype, descr, maingroup, resxml) VALUES ('68', 'Image', '220', 'free', '<?xml version="1.0" encoding="UTF-8"?>
<resource typecode="Image" id="68"><descr>220</descr><groups mainGroup="free" /><categories /><instance><size>3</size><filename>DSCN0220_d3.JPG</filename><mimetype>image/jpeg</mimetype><weight>3 Kb</weight></instance><instance><size>2</size><filename>DSCN0220_d2.JPG</filename><mimetype>image/jpeg</mimetype><weight>3 Kb</weight></instance><instance><size>1</size><filename>DSCN0220_d1.JPG</filename><mimetype>image/jpeg</mimetype><weight>2 Kb</weight></instance><instance><size>0</size><filename>DSCN0220_d0.JPG</filename><mimetype>image/jpeg</mimetype><weight>1 Kb</weight></instance></resource>');
INSERT INTO jpversioning_trashedresources (resid, restype, descr, maingroup, resxml) VALUES ('69', 'Image', 'qqq', 'customers', '<?xml version="1.0" encoding="UTF-8"?>
<resource typecode="Image" id="69"><descr>qqq</descr><groups mainGroup="free" /><categories /><instance><size>3</size><filename>DSCN0219_d3.JPG</filename><mimetype>image/jpeg</mimetype><weight>4 Kb</weight></instance><instance><size>2</size><filename>DSCN0219_d2.JPG</filename><mimetype>image/jpeg</mimetype><weight>4 Kb</weight></instance><instance><size>1</size><filename>DSCN0219_d1.JPG</filename><mimetype>image/jpeg</mimetype><weight>2 Kb</weight></instance><instance><size>0</size><filename>DSCN0219_d0.JPG</filename><mimetype>image/jpeg</mimetype><weight>1 Kb</weight></instance></resource>');
INSERT INTO jpversioning_trashedresources (resid, restype, descr, maingroup, resxml) VALUES ('70', 'Image', 'tux', 'free', '<?xml version="1.0" encoding="UTF-8"?>
<resource typecode="Image" id="70"><descr>tux</descr><groups mainGroup="free" /><categories /><instance><size>3</size><filename>tux_d3.png</filename><mimetype>image/png</mimetype><weight>5 Kb</weight></instance><instance><size>2</size><filename>tux_d2.png</filename><mimetype>image/png</mimetype><weight>5 Kb</weight></instance><instance><size>1</size><filename>tux_d1.png</filename><mimetype>image/png</mimetype><weight>5 Kb</weight></instance><instance><size>0</size><filename>tux_d0.png</filename><mimetype>image/png</mimetype><weight>0 Kb</weight></instance></resource>');
INSERT INTO jpversioning_trashedresources (resid, restype, descr, maingroup, resxml) VALUES ('71', 'Image', 'Logo jAPS', 'free', '<?xml version="1.0" encoding="UTF-8"?>
<resource typecode="Image" id="398"><descr>Logo jAPS</descr><groups mainGroup="free" /><categories><category id="resCat1" /></categories><instance><size>3</size><filename>jAPS_logo_d3.jpg</filename><mimetype>image/jpeg</mimetype><weight>2 Kb</weight></instance><instance><size>2</size><filename>jAPS_logo_d2.jpg</filename><mimetype>image/jpeg</mimetype><weight>2 Kb</weight></instance><instance><size>1</size><filename>jAPS_logo_d1.jpg</filename><mimetype>image/jpeg</mimetype><weight>1 Kb</weight></instance><instance><size>0</size><filename>jAPS_logo_d0.jpg</filename><mimetype>image/jpeg</mimetype><weight>0 Kb</weight></instance></resource>
');




