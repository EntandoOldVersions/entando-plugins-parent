CREATE TABLE jptokenapi_usertokens (
  username character varying(40) NOT NULL,
  token character varying(100),
  CONSTRAINT jptokenapi_usertokens_pkey PRIMARY KEY (username)
);