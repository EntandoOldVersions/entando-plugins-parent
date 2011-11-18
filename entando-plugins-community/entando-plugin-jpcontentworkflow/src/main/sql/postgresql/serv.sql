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