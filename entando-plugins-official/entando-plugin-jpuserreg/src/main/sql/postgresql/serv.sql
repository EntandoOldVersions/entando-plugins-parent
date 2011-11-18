CREATE TABLE jpuserreg_activationtokens
(
  username character varying(40) NOT NULL,
  token character varying NOT NULL,
  regtime timestamp without time zone NOT NULL,
  tokentype character varying(25) NOT NULL,
  CONSTRAINT jpuserreg_activationtokens_pkey PRIMARY KEY (username)
);