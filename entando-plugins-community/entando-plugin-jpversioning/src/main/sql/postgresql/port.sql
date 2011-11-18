
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
</Params>
' WHERE item = 'params';




CREATE TABLE jpversioning_versionedcontents
(
  id integer NOT NULL,
  contentid character varying(16) NOT NULL,
  contenttype character varying(30) NOT NULL,
  descr character varying(100) NOT NULL,
  status character varying(12) NOT NULL,
  "xml" character varying NOT NULL,
  versiondate timestamp without time zone NOT NULL,
  "version" character varying(7) NOT NULL,
  onlineversion integer NOT NULL,
  approved smallint NOT NULL,
  username character varying(40),
  CONSTRAINT jpversioning_versionedcontents_pkey PRIMARY KEY (id),
  CONSTRAINT jpversioning_versionedcontents_contentid_key UNIQUE (contentid, version)
)
WITH (OIDS=FALSE);




CREATE TABLE jpversioning_trashedresources
(
  resid character varying(16) NOT NULL,
  restype character varying(30) NOT NULL,
  descr character varying(100) NOT NULL,
  maingroup character varying(20) NOT NULL,
  resxml character varying NOT NULL,
  CONSTRAINT jpversioning_trashedresources_pkey PRIMARY KEY (resid)
)
WITH (OIDS=FALSE);
