CREATE TABLE `jpcontentfeedback_comments` (
  `id` int(11) NOT NULL,
  `contentid` varchar(16) NOT NULL,
  `creationdate` timestamp NOT NULL DEFAULT 0,
  `usercomment` longtext NOT NULL,
  `status` int(11) NOT NULL,
  `username` varchar(40) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




CREATE TABLE `jpcontentfeedback_rating` (
  `id` int(11) NOT NULL,
  `commentid` int(11) DEFAULT NULL,
  `contentid` varchar(16) DEFAULT NULL,
  `voters` int(11) DEFAULT NULL,
  `sumvote` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `jpcontentfeedback_rating_refcommentid_fkey` (`commentid`),
  CONSTRAINT `jpcontentfeedback_rating_refcommentid_fkey` FOREIGN KEY (`commentid`) REFERENCES `jpcontentfeedback_comments` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO showletcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('content_feedback_viewer', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Content feedback</property>
<property key="it">Feedback contenuti</property>
</properties>', '<config>
	<parameter name="contentId">
		Content ID
	</parameter>
	<parameter name="modelId">
		Content Model ID
	</parameter>
	<parameter name="usedContentRating">
		Enable content rating (true|false)
	</parameter>
<parameter name="usedComment">
		Enable user comments (true|false)
	</parameter>
<parameter name="usedCommentWithRating">
		Enable rating on comments (true|false)
	</parameter>
<parameter name="commentValidation">
		Enable administrator moderation of comments (true|false)
	</parameter>
	<action name="viewerContentFeedbackConfig"/>
</config>', 'jpcontentfeedback', NULL, NULL, 1);

INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'jpcontentfeedback_config', 'Content Feedback global configuration', '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<contentFeedbackConfig>
		<anonymousComment>true</anonymousComment>
		<comment>true</comment>
		<rateComment>true</rateComment>
		<rateContent>true</rateContent>
</contentFeedbackConfig>');