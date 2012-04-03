INSERT INTO authpermissions (permissionname, descr) VALUES ('jpuserprofile_profile_view', 'User Profile - View');
INSERT INTO authpermissions (permissionname, descr) VALUES ('jpuserprofile_profile_edit', 'User Profile - Edit');

CREATE TABLE `jpuserprofile_authuserprofiles` (
  `username` varchar(40) NOT NULL,
  `profiletype` varchar(30) NOT NULL,
  `profilexml` longtext NOT NULL,
  `publicprofile` smallint(6) NOT NULL DEFAULT '0',
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `jpuserprofile_profilesearch` (
  `username` varchar(40) NOT NULL,
  `attrname` varchar(30) NOT NULL,
  `textvalue` varchar(255) DEFAULT NULL,
  `datevalue` date DEFAULT NULL,
  `numvalue` int(11) DEFAULT NULL,
  `langcode` varchar(2) DEFAULT NULL,
  KEY `jpuserprofile_profilesearch_username_fkey` (`username`),
  CONSTRAINT `jpuserprofile_profilesearch_username_fkey` FOREIGN KEY (`username`) REFERENCES `jpuserprofile_authuserprofiles` (`username`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
