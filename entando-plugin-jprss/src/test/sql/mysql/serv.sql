CREATE TABLE `jprss_channel` (
  `id` int(11) NOT NULL,
  `title` varchar(100) NOT NULL,
  `description` varchar(100) NOT NULL,
  `active` varchar(5) NOT NULL,
  `contenttype` varchar(30) NOT NULL,
  `filters` longtext,
  `feedtype` varchar(10) NOT NULL,
  `category` varchar(30) DEFAULT NULL,
  `maxcontentsize` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
