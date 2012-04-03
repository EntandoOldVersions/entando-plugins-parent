CREATE TABLE jpactionlogger_records
(
  id integer NOT NULL,
  username character varying(20),
  actiondate timestamp DEFAULT 0,
  namespace longtext,
  actionname character varying(255),
  params longtext,
  CONSTRAINT jpactionlogger_records_pkey PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
