CREATE TABLE `jpstats_statistics` (
  `ip` varchar(19) DEFAULT NULL,
  `referer` longtext,
  `session_id` varchar(254) DEFAULT NULL,
  `role` varchar(254) DEFAULT NULL,
  `timestamp` varchar(254) DEFAULT NULL,
  `year_value` char(4) DEFAULT NULL,
  `month_value` char(2) DEFAULT NULL,
  `day_value` char(2) DEFAULT NULL,
  `hour_value` char(8) DEFAULT NULL,
  `pagecode` varchar(254) DEFAULT NULL,
  `langcode` char(2) DEFAULT NULL,
  `useragent` longtext,
  `browserlang` varchar(254) DEFAULT NULL,
  `content` varchar(16) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;