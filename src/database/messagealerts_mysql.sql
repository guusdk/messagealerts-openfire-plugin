CREATE TABLE `ofmsgalerts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `toJID` varchar(255) NOT NULL,
  `body` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO ofVersion (name, version) VALUES ('messagealerts', 0);
