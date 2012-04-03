

CREATE TABLE jpnewsletter_newsletterreport (
  id integer NOT NULL,
  date timestamp without time zone NOT NULL,
  subject character varying NOT NULL,
  CONSTRAINT jpnewsletter_newsletterreport_pkey PRIMARY KEY (id)
);

CREATE TABLE jpnewsletter_contentreport
(
  id integer NOT NULL,
  newsletterid integer NOT NULL,
  contentid character varying(16) NOT NULL,
  textbody character varying NOT NULL,
  htmlbody character varying,
  CONSTRAINT jpnewsletter_contentreport_pkey PRIMARY KEY (id),
  CONSTRAINT jpnewsletter_contentreport_refnewsletterreport_fkey FOREIGN KEY (newsletterid)
      REFERENCES jpnewsletter_newsletterreport (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE jpnewsletter_recipient
(
  contentreportid integer NOT NULL,
  username character varying(40) NOT NULL,
  mailaddress character varying(100) NOT NULL,
  CONSTRAINT jpnewsletterdest_pkey PRIMARY KEY (contentreportid, username),
  CONSTRAINT jpnewsletterdest_recipient_refcontentreport_fkey FOREIGN KEY (contentreportid)
      REFERENCES jpnewsletter_contentreport (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);


CREATE TABLE jpnewsletter_contentqueue
(
  contentid character varying(16) NOT NULL,
  CONSTRAINT jpnewsletter_contentqueue_pkey PRIMARY KEY (contentid)
);


CREATE TABLE jpnewsletter_subscribers
(
  mailaddress character varying(100) NOT NULL,
  subscription_date date,
  active smallint,
  CONSTRAINT jpnewsletter_subscribers_pkey PRIMARY KEY (mailaddress)
)
WITH (OIDS=FALSE);

CREATE TABLE jpnewsletter_subscribertokens
(
  mailaddress character varying(100) NOT NULL,
  token character varying NOT NULL,
  regtime timestamp without time zone NOT NULL,
  CONSTRAINT jpnewsletter_subscribertokens_pkey PRIMARY KEY (mailaddress)
)
WITH (OIDS=FALSE);

