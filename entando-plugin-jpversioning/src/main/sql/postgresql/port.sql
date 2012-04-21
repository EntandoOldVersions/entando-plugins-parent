CREATE TABLE jpversioning_versionedcontents
(
  id integer NOT NULL,
  contentid character varying(16) NOT NULL,
  contenttype character varying(30) NOT NULL,
  descr character varying(100) NOT NULL,
  status character varying(12) NOT NULL,
  contentxml character varying NOT NULL,
  versiondate timestamp without time zone NOT NULL,
  versioncode character varying(7) NOT NULL,
  onlineversion integer NOT NULL,
  approved smallint NOT NULL,
  username character varying(40),
  CONSTRAINT jpversioning_versionedcontents_pkey PRIMARY KEY (id),
  CONSTRAINT jpversioning_versionedcontents_contentid_key UNIQUE (contentid, versioncode)
);

CREATE TABLE jpversioning_trashedresources
(
  resid character varying(16) NOT NULL,
  restype character varying(30) NOT NULL,
  descr character varying(100) NOT NULL,
  maingroup character varying(20) NOT NULL,
  resxml character varying NOT NULL,
  CONSTRAINT jpversioning_trashedresources_pkey PRIMARY KEY (resid)
);
