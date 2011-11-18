CREATE TABLE jpactionlogger_records
(
  id integer NOT NULL,
  username character varying(20),
  actiondate timestamp without time zone,
  namespace character varying,
  actionname character varying(255),
  params character varying,
  CONSTRAINT jpactionlogger_records_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=true
);