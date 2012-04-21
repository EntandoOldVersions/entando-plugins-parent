CREATE TABLE jprssaggregator_aggregatoritems
(
  code integer NOT NULL,
  contenttype character varying(30) NOT NULL,
  descr character varying(255) NOT NULL,
  link character varying(255) NOT NULL,
  delay bigint NOT NULL,
  lastupdate timestamp without time zone NOT NULL,
  categories character varying,
  CONSTRAINT jprssaggregator_aggregatoritems_pkey PRIMARY KEY (code)
);
