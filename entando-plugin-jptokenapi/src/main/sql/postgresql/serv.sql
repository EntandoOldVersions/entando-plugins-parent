CREATE TABLE jptokenapi_usertokens (
  username character varying(40) NOT NULL,
  token character varying(100),
  CONSTRAINT jptokenapi_usertokens_pkey PRIMARY KEY (username)
);

INSERT INTO jptokenapi_usertokens(username, token) VALUES ('admin', '671c5cf66e2e2327d5f43eefe51f0f91');

