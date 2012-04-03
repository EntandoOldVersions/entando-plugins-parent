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


INSERT INTO authroles (rolename, descr) VALUES ('contentType_ART', 'ART Content Type Management');
INSERT INTO authroles (rolename, descr) VALUES ('contentType_EVN', 'EVN Content Type Management');
INSERT INTO authroles (rolename, descr) VALUES ('contentType_RAH', 'RAH Content Type Management');

INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('contentType_ART', 'editContents');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('contentType_ART', 'enterBackend');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('contentType_EVN', 'editContents');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('contentType_EVN', 'enterBackend');

INSERT INTO authuserroles (username, rolename) VALUES ('editorCoach', 'contentType_RAH');
INSERT INTO authuserroles (username, rolename) VALUES ('editorCoach', 'contentType_ART');