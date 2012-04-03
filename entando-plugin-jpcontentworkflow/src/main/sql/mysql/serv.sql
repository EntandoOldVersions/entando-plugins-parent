CREATE TABLE `jpcontentworkflow_events` (
  `id` int(11) NOT NULL,
  `data` timestamp NOT NULL DEFAULT 0,
  `contentid` varchar(16) NOT NULL,
  `contenttype` varchar(30) NOT NULL,
  `descr` varchar(1000) NOT NULL,
  `maingroup` varchar(20) NOT NULL,
  `status` varchar(12) NOT NULL,
  `notified` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
