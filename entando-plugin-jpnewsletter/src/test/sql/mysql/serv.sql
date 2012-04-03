

CREATE TABLE `jpnewsletter_newsletterreport` (
  `id` int(11) NOT NULL,
  `date` timestamp NOT NULL DEFAULT 0,
  `subject` longtext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `jpnewsletter_contentreport` (
  `id` int(11) NOT NULL,
  `newsletterid` int(11) NOT NULL,
  `contentid` varchar(16) NOT NULL,
  `textbody` longtext NOT NULL,
  `htmlbody` longtext,
  PRIMARY KEY (`id`),
  KEY `jpnewsletter_contentreport_refnewsletterreport_fkey` (`newsletterid`),
  CONSTRAINT `jpnewsletter_contentreport_refnewsletterreport_fkey` FOREIGN KEY (`newsletterid`) REFERENCES `jpnewsletter_newsletterreport` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `jpnewsletter_recipient` (
  `contentreportid` int(11) NOT NULL,
  `username` varchar(40) NOT NULL,
  `mailaddress` varchar(100) NOT NULL,
  PRIMARY KEY (`contentreportid`,`username`),
  KEY `jpnewsletterdest_recipient_refcontentreport_fkey` (`contentreportid`),
  CONSTRAINT `jpnewsletterdest_recipient_refcontentreport_fkey` FOREIGN KEY (`contentreportid`) REFERENCES `jpnewsletter_contentreport` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `jpnewsletter_contentqueue` (
  `contentid` varchar(16) NOT NULL,
  PRIMARY KEY (`contentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `jpnewsletter_subscribers` (
  `mailaddress` varchar(100) NOT NULL,
  `subscription_date` date,
  `active` tinyint(4) NOT NULL,
  PRIMARY KEY (`mailaddress`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `jpnewsletter_subscribertokens` (
  `mailaddress` varchar(100) NOT NULL,
  `token` longtext NOT NULL,
  `regtime` timestamp NOT NULL,
  PRIMARY KEY (`mailaddress`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

