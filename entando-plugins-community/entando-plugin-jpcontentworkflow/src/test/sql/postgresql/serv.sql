CREATE TABLE jpcontentworkflow_events
(
  id integer NOT NULL,
  data timestamp without time zone,
  contentid character varying(16) NOT NULL,
  contenttype character varying(30) NOT NULL,
  descr character varying(1000) NOT NULL,
  maingroup character varying(20) NOT NULL,
  status character varying(12) NOT NULL,
  notified smallint,
  CONSTRAINT jpcontentworkflow_events_pkey PRIMARY KEY (id)
);


INSERT INTO authroles (rolename, descr) VALUES ('contentType_ART', 'ART Content Type Management');
INSERT INTO authroles (rolename, descr) VALUES ('contentType_EVN', 'EVN Content Type Management');
INSERT INTO authroles (rolename, descr) VALUES ('contentType_RAH', 'RAH Content Type Management');

INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('contentType_ART', 'editContents');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('contentType_ART', 'enterBackend');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('contentType_EVN', 'editContents');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('contentType_EVN', 'enterBackend');

INSERT INTO authuserroles (username, rolename) VALUES ('editorCoach', 'contentType_RAH');
INSERT INTO authuserroles (username, rolename) VALUES ('editorCoach', 'contentType_ART');