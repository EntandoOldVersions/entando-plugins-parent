CREATE TABLE `jptokenapi_usertokens` (
  `username` varchar(40) DEFAULT NULL,
  `token` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;