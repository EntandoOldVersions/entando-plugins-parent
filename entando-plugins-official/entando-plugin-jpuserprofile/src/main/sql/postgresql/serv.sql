INSERT INTO authpermissions (permissionname, descr) VALUES ('jpuserprofile_profile_view', 'User Profile - View');
INSERT INTO authpermissions (permissionname, descr) VALUES ('jpuserprofile_profile_edit', 'User Profile - Edit');

CREATE TABLE jpuserprofile_authuserprofiles
(
  username character varying(40) NOT NULL,
  profiletype character varying(30) NOT NULL,
  "xml" character varying NOT NULL,
  publicprofile smallint NOT NULL,
  CONSTRAINT jpuserprofile_autuserprofiles_pkey PRIMARY KEY (username)
);

CREATE TABLE jpuserprofile_profilesearch
(
  username character varying(40) NOT NULL,
  attrname character varying(30) NOT NULL,
  textvalue character varying(255),
  datevalue date,
  numvalue integer,
  langcode character varying(2),
  CONSTRAINT jpuserprofile_profilesearch_username_fkey FOREIGN KEY (username)
      REFERENCES jpuserprofile_authuserprofiles (username) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
