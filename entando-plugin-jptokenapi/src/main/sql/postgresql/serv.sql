CREATE TABLE jptokenapi_usertokens (
  username character varying(40) NOT NULL,
  token character varying(100),
  CONSTRAINT jptokenapi_usertokens_pkey PRIMARY KEY (username)
);

INSERT INTO jptokenapi_usertokens(username, token) VALUES ('admin', '571c5cc66e2e2783d5f43edfe74f0f91');

