CREATE database IF NOT EXISTS reach_out;

CREATE USER IF NOT EXISTS 'reach'@'localhost' IDENTIFIED BY 'reach_pass';

GRANT ALL PRIVILEGES ON reach_out.* TO 'reach'@'localhost';

DROP TABLE IF EXISTS reach_out.USERS;
DROP TABLE IF EXISTS reach_out.PASSWORDS;
DROP TABLE IF EXISTS reach_out.LISTINGS;

CREATE TABLE reach_out.USERS (
  `USERS_ID` int(11) NOT NULL,
  `USERS_USERNAME` varchar(100) NOT NULL,
  `USERS_EMAIL` varchar(100) NOT NULL,
  PRIMARY KEY (`USERS_ID`),
  UNIQUE KEY `USERS_USERNAME_UNIQUE` (`USERS_USERNAME`)
);

CREATE TABLE reach_out.PASSWORDS(
PWD_ID int(11) NOT NULL,
PWD_USER_ID int(11) NOT NULL,
PWD_CREATE_DATE BigInt(35) NOT NULL,
PWD_PASSWORD varchar(128) NOT NULL,
PRIMARY KEY(PWD_ID)
);

CREATE TABLE reach_out.LISTINGS(
LST_ID int(11) NOT NULL,
LST_USER_ID int(11) NOT NULL,
LST_TITLE varchar(128) NOT NULL,
LST_DESCRIPTION varchar(2048) NOT NULL,
LST_COUNTY varchar(128) NOT NULL,
LST_CITY varchar(128) NOT NULL,
LST_STATUS int(3) NOT NULL,
LST_TYPE int(3) NOT NULL,
LST_CREATE_DATE BigInt(35) NOT NULL,
PRIMARY KEY(LST_ID)
);