CREATE TABLE `jpuserreg_activationtokens` (
  `username` varchar(40) NOT NULL,
  `token` longtext NOT NULL,
  `regtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `tokentype` varchar(25) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
