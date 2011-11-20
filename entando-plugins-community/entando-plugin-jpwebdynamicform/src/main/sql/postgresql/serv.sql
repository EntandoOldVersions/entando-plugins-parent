CREATE TABLE jpwebdynamicform_messages
(
  messageid character varying(16) NOT NULL,
  username character varying(40),
  langcode character varying(2) NOT NULL,
  messagetype character varying(30) NOT NULL,
  creationdate timestamp without time zone NOT NULL,
  messagexml character varying NOT NULL,
  CONSTRAINT jpwebdynamicform_messages_pkey PRIMARY KEY (messageid)
)
WITH (OIDS=TRUE);


CREATE TABLE jpwebdynamicform_messagesearch
(
  messageid character varying(16) NOT NULL,
  attrname character varying(30) NOT NULL,
  textvalue character varying(255),
  datevalue date,
  numvalue integer,
  langcode character varying(2),
  CONSTRAINT jpwebdynamicform_messagesearch_messageid_fkey FOREIGN KEY (messageid)
      REFERENCES jpwebdynamicform_messages (messageid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS=TRUE);

CREATE TABLE jpwebdynamicform_messageanswers
(
  answerid character varying(16) NOT NULL,
  messageid character varying(16) NOT NULL,
  "operator" character varying(20),
  senddate timestamp without time zone NOT NULL,
  "text" character varying NOT NULL,
  CONSTRAINT jpwebdynamicform_messageanswers_pkey PRIMARY KEY (answerid),
  CONSTRAINT jpwebdynamicform_messageanswers_messageid_fkey FOREIGN KEY (messageid)
      REFERENCES jpwebdynamicform_messages (messageid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS=TRUE);


INSERT INTO authpermissions (permissionname, descr) VALUES ('jpwebdynamicform_manageForms', 'Gestione Web Dynamic Forms');
