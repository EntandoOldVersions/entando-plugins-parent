CREATE TABLE `jpstats_statistics` (
  `ip` varchar(19) DEFAULT NULL,
  `referer` longtext,
  `session_id` varchar(254) DEFAULT NULL,
  `role` varchar(254) DEFAULT NULL,
  `timestamp` varchar(254) DEFAULT NULL,
  `year` char(4) DEFAULT NULL,
  `month` char(2) DEFAULT NULL,
  `day` char(2) DEFAULT NULL,
  `hour` char(8) DEFAULT NULL,
  `pagecode` varchar(254) DEFAULT NULL,
  `langcode` char(2) DEFAULT NULL,
  `useragent` longtext,
  `browserlang` varchar(254) DEFAULT NULL,
  `content` varchar(16) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;