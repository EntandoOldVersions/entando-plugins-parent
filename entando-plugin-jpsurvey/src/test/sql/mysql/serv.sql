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


INSERT INTO jpsurvey ( id, description, maingroup, startdate, enddate, active, publicpartialresult,
	publicresult, questionnaire, gatheruserinfo, title, restrictedaccess, checkcookie,
	checkipaddress, imageid, imagedescr ) VALUES
	( 1, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Description-1</property>
<property key="it">Descrizione-1</property>
</properties>', 'ignored', '2009-03-16', NULL, 1, 0, 1, 1, 1, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Title-1</property>
<property key="it">Titolo-1</property>
</properties>', 0, 0, 0, 'IMG001', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Barrali by night</property>
<property key="it">Barrali di notte</property>
</properties>' ),
	( 2, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Descrizione-2</property>
	<property key="en">Description-2</property>
</properties>', 'ignoredToo', '2008-02-06', NULL, 0, 0, 0, 0, 0, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Titolo-2</property>
	<property key="en">Title-2</property>
</properties>', 1, 0, 0, NULL, NULL );


INSERT INTO jpsurvey_questions ( id, surveyid, question, pos, singlechoice, minresponsenumber, maxresponsenumber ) VALUES
	( 1, 1, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Domanda 1-1</property>
	<property key="en">Question 1-1</property>
</properties>', 2, 1, 1, 1 ),
	( 2, 1, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Domanda 1-2</property>
	<property key="en">Question 1-2</property>
</properties>', 1, 0, 1, 2 );


INSERT INTO jpsurvey_choices ( id, questionid, choice, pos, freetext ) VALUES
	( 1, 1, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Opzione 1-1-1</property>
	<property key="en">Option 1-1-1</property>
</properties>', 1, 0 ),
	( 2, 1, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Opzione 1-1-2</property>
	<property key="en">Option 1-1-2</property>
</properties>', 3, 0 ),
	( 3, 1, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Opzione 1-1-3</property>
	<property key="en">Option 1-1-3</property>
</properties>', 2, 0 ),
	( 4, 2, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Opzione 1-2-2</property>
	<property key="en">Option 1-2-2</property>
</properties>', 2, 0 ),
	( 5, 2, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Opzione 1-2-1</property>
	<property key="en">Option 1-2-1</property>
</properties>', 1, 0 ),
	( 6, 2, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Opzione TESTO LIBERO</property>
	<property key="en">Option FREE TEXT</property>
</properties>', 4, 1 ),
	( 7, 2, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Opzione 1-2-3</property>
	<property key="en">Option 1-2-3</property>
</properties>', 3, 0 );


INSERT INTO jpsurvey_voters ( id, age, country, sex, votedate, surveyid, username, ipaddress ) VALUES
	( 1, 99, 'ir', 'M', '2008-04-07', 2, 'guest', '192.168.10.1' );


INSERT INTO jpsurvey_responses ( voterid, questionid, choiceid, freetext ) VALUES
	( 1, 2, 6, 'lorem ipsum dolor' );


