CREATE TABLE `jpsurvey` (
  `id` int(11) NOT NULL,
  `description` longtext NOT NULL,
  `maingroup` varchar(20) NOT NULL,
  `startdate` date NOT NULL,
  `enddate` date DEFAULT NULL,
  `active` smallint(6) NOT NULL,
  `publicpartialresult` smallint(6) NOT NULL,
  `publicresult` smallint(6) NOT NULL,
  `questionnaire` smallint(6) NOT NULL,
  `gatheruserinfo` smallint(6) NOT NULL,
  `title` longtext NOT NULL,
  `restrictedaccess` smallint(6) NOT NULL,
  `checkcookie` smallint(6) NOT NULL,
  `checkipaddress` smallint(6) NOT NULL,
  `imageid` varchar(16) DEFAULT NULL,
  `imagedescr` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `jpsurvey_questions` (
  `id` int(11) NOT NULL,
  `surveyid` int(11) NOT NULL,
  `question` longtext NOT NULL,
  `pos` smallint(6) NOT NULL,
  `singlechoice` smallint(6) NOT NULL,
  `minresponsenumber` smallint(6) DEFAULT NULL,
  `maxresponsenumber` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `jpsurvey_questions_surveyid_fkey` (`surveyid`),
  CONSTRAINT `jpsurvey_questions_surveyid_fkey` FOREIGN KEY (`surveyid`) REFERENCES `jpsurvey` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `jpsurvey_choices` (
  `id` int(11) NOT NULL,
  `questionid` int(11) NOT NULL,
  `choice` longtext NOT NULL,
  `pos` smallint(6) NOT NULL,
  `freetext` smallint(6) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `jpsurvey_answeres_questionid_fkey` (`questionid`),
  CONSTRAINT `jpsurvey_answeres_questionid_fkey` FOREIGN KEY (`questionid`) REFERENCES `jpsurvey_questions` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `jpsurvey_voters` (
  `id` int(11) NOT NULL,
  `age` smallint(6) DEFAULT NULL,
  `country` varchar(2) DEFAULT NULL,
  `sex` char(1) DEFAULT NULL,
  `votedate` date NOT NULL,
  `surveyid` int(11) NOT NULL,
  `username` varchar(30) NOT NULL,
  `ipaddress` varchar(15) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `surveyid` (`surveyid`),
  CONSTRAINT `surveyid` FOREIGN KEY (`surveyid`) REFERENCES `jpsurvey` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `jpsurvey_responses` (
  `voterid` int(11) NOT NULL,
  `questionid` int(11) NOT NULL,
  `choiceid` int(11) NOT NULL,
  `freetext` varchar(30) DEFAULT NULL,
  KEY `choiceid` (`choiceid`),
  KEY `jpsurvey_responses_voterid_fkey` (`voterid`),
  KEY `questionid` (`questionid`),
  CONSTRAINT `choiceid` FOREIGN KEY (`choiceid`) REFERENCES `jpsurvey_choices` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `jpsurvey_responses_voterid_fkey` FOREIGN KEY (`voterid`) REFERENCES `jpsurvey_voters` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `questionid` FOREIGN KEY (`questionid`) REFERENCES `jpsurvey_questions` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
