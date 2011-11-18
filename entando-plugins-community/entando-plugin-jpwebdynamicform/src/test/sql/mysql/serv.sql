CREATE TABLE `jpwebdynamicform_messages` (
  `messageid` varchar(16) NOT NULL,
  `username` varchar(40) DEFAULT NULL,
  `langcode` varchar(2) NOT NULL,
  `messagetype` varchar(30) NOT NULL,
  `creationdate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `xml` longtext NOT NULL,
  PRIMARY KEY (`messageid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `jpwebdynamicform_messagesearch` (
  `messageid` varchar(16) NOT NULL,
  `attrname` varchar(30) NOT NULL,
  `textvalue` varchar(255) DEFAULT NULL,
  `datevalue` date DEFAULT NULL,
  `numvalue` int(11) DEFAULT NULL,
  `langcode` varchar(2) DEFAULT NULL,
  KEY `jpwebdynamicform_messagesearch_messageid_fkey` (`messageid`),
  CONSTRAINT `jpwebdynamicform_messagesearch_messageid_fkey` FOREIGN KEY (`messageid`) REFERENCES `jpwebdynamicform_messages` (`messageid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `jpwebdynamicform_messageanswers` (
  `answerid` varchar(16) NOT NULL,
  `messageid` varchar(16) NOT NULL,
  `operator` varchar(20) DEFAULT NULL,
  `senddate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `text` longtext NOT NULL,
  PRIMARY KEY (`answerid`),
  KEY `jpwebdynamicform_messageanswers_messageid_fkey` (`messageid`),
  CONSTRAINT `jpwebdynamicform_messageanswers_messageid_fkey` FOREIGN KEY (`messageid`) REFERENCES `jpwebdynamicform_messages` (`messageid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

