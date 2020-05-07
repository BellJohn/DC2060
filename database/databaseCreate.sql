create database if not exists reach_out;

CREATE USER IF NOT EXISTS 'reach'@'localhost' IDENTIFIED BY 'reach_pass';

GRANT ALL PRIVILEGES on reach_out.* TO 'reach'@'localhost';

DROP TABLE IF EXISTS reach_out.USERS;
DROP TABLE IF EXISTS reach_out.PASSWORDS;
DROP TABLE IF EXISTS reach_out.LISTINGS;
DROP TABLE IF EXISTS reach_out.ASSIGNED_LISTINGS;
DROP TABLE IF EXISTS reach_out.USERPROFILE;
DROP TABLE IF EXISTS reach_out.HEALTHSTATUS;


CREATE TABLE reach_out.USERS (
  `USERS_ID` int(11) NOT NULL AUTO_INCREMENT,
  `USERS_USERNAME` varchar(100) NOT NULL,
  `USERS_EMAIL` varchar(100) NOT NULL,
  `USERS_FIRSTNAME` varchar(100) NOT NULL,
  `USERS_LASTNAME` varchar(100) NOT NULL,
  `USERS_DOB` varchar(100) NOT NULL,
  PRIMARY KEY (`USERS_ID`),
  UNIQUE KEY `USERS_USERNAME_UNIQUE` (`USERS_USERNAME`)
);

CREATE TABLE reach_out.PASSWORDS(
PWD_ID int(11) NOT NULL AUTO_INCREMENT,
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

CREATE TABLE reach_out.ASSIGNED_LISTINGS(
AS_ID int(11) NOT NULL AUTO_INCREMENT,
AS_LISTING_ID int(11) NOT NULL,
AS_USER_ID int(11) NOT NULL,
PRIMARY KEY (AS_ID),
UNIQUE KEY AS_UNIQ_COMBO (AS_LISTING_ID, AS_USER_ID)
);

CREATE TABLE reach_out.USERPROFILE (
  `USER_ID` int(11) NOT NULL AUTO_INCREMENT,
  `PROFILE_PIC` varchar(300) NOT NULL,
  `BIO` varchar(1000) NOT NULL,
  `HEALTH_STATUS` varchar(100) NOT NULL,
  PRIMARY KEY (`USER_ID`)
);

CREATE TABLE reach_out.HEALTHSTATUS (
  	HEALTHSTATUS_ID int(11) NOT NULL AUTO_INCREMENT,
	STATUS VARCHAR(100) NOT NULL,
    PRIMARY KEY (`HEALTHSTATUS_ID`)
  );

INSERT INTO reach_out.HEALTHSTATUS (`status`) VALUES ("Healthy");
INSERT INTO reach_out.HEALTHSTATUS (`status`) VALUES ("Healthy, but mobility restricted");
INSERT INTO reach_out.HEALTHSTATUS (`status`) VALUES ("Self-Isolating, potential COVID-19");
INSERT INTO reach_out.HEALTHSTATUS (`status`) VALUES ("Self-Isolating, due to COVID-19");
