create database if not exists reach_out;

CREATE USER 'reach'@'localhost' IDENTIFIED BY 'reach_pass';

GRANT ALL PRIVILEGES on reach_out.* TO 'reach'@'localhost';

DROP TABLE IF EXISTS reach_out.USERS;
DROP TABLE IF EXISTS reach_out.PASSWORDS;
DROP TABLE IF EXISTS reach_out.USERPROFILE;

CREATE TABLE reach_out.USERS (
  `USERS_ID` int(11) NOT NULL,
  `USERS_USERNAME` varchar(100) NOT NULL,
  `USERS_EMAIL` varchar(100) NOT NULL,
  `USERS_FIRSTNAME` varchar(100) NOT NULL,
  `USERS_LASTNAME` varchar(100) NOT NULL,
<<<<<<< HEAD
  `USERS_DOB` date NOT NULL,
  PRIMARY KEY (USERS_ID),
=======
  `USERS_DOB` varchar(100) NOT NULL
  PRIMARY KEY (`USERS_ID`),
>>>>>>> d56275fd2c58fbd0f5a1680fcdc2205f2f7be7a0
  UNIQUE KEY `USERS_USERNAME_UNIQUE` (`USERS_USERNAME`)
);

CREATE TABLE reach_out.PASSWORDS(
PWD_ID int(11) NOT NULL,
PWD_USER_ID int(11) NOT NULL,
PWD_CREATE_DATE BigInt(35) NOT NULL,
PWD_PASSWORD varchar(128) NOT NULL,
PRIMARY KEY(PWD_ID)
<<<<<<< HEAD
=======

>>>>>>> d56275fd2c58fbd0f5a1680fcdc2205f2f7be7a0
);

CREATE TABLE reach_out.USERPROFILE (
  `USER_ID` int(11) NOT NULL,
  `PROFILE_PIC` varchar(300) NOT NULL,
  `BIO` varchar(1000) NOT NULL,
  `HEALTH_STATUS` varchar(100) NOT NULL,
  PRIMARY KEY (`USER_ID`)
);
<<<<<<< HEAD

CREATE TABLE reach_out.HEALTHSTATUS (
  	HEALTHSTATUS_ID int(11) NOT NULL,
	STATUS VARCHAR(100) NOT NULL
  );
  
INSERT INTO reach_out.HEALTHSTATUS (`status`) VALUES ("Self-Isolating - due to suspected COVID-19");
INSERT INTO reach_out.HEALTHSTATUS (`status`) VALUES ("In quarantine due to exposure to COVID-19");
INSERT INTO reach_out.HEALTHSTATUS (`status`) VALUES( "Self-Isolating - due to health issues");
INSERT INTO reach_out.HEALTHSTATUS (`status`) VALUES ("Recovered from COVID-19");
INSERT INTO reach_out.HEALTHSTATUS (`status`) VALUES ("Healthy");
 
=======
 



>>>>>>> d56275fd2c58fbd0f5a1680fcdc2205f2f7be7a0
