create database if not exists reach_out;

CREATE USER 'reach'@'localhost' IDENTIFIED BY 'reach_pass';
CREATE USER 'reach'@'%' IDENTIFIED BY 'reach_pass';
GRANT ALL PRIVILEGES on reach_out.* TO 'reach'@'%';

DROP TABLE IF EXISTS reach_out.users;

CREATE TABLE reach_out.users (
  `USERS_ID` int(11) NOT NULL,
  `USERS_USERNAME` varchar(100) NOT NULL,
  `USERS_EMAIL` varchar(100) NOT NULL,
  `USERS_PASSWORD` varchar(100) NOT NULL,
  PRIMARY KEY (`USERS_ID`),
  UNIQUE KEY `USERS_USERNAME_UNIQUE` (`USERS_USERNAME`)
);
