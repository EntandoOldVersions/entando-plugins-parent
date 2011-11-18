CREATE TABLE `jprssaggregator_aggregatoritems` (
  `code` int(11) NOT NULL,
  `contenttype` varchar(30) NOT NULL,
  `descr` varchar(255) NOT NULL,
  `link` varchar(255) NOT NULL,
  `delay` bigint(20) NOT NULL,
  `lastupdate` datetime NOT NULL,
  `categories` longtext,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;