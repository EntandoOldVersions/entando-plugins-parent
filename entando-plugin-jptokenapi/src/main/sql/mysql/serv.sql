CREATE TABLE `jptokenapi_usertokens` (
  `username` varchar(40) DEFAULT NULL,
  `token` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO jptokenapi_usertokens(username, token) VALUES ('admin', '571c5cc66e2e2783d5f43edfe74f0f91');