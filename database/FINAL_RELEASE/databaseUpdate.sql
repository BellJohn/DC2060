-- UPDATES TO THE DATABASE FOR FINAL_RELEASE AFTER THE MVP_RELEASE
DROP TABLE IF EXISTS reach_out.SYSTEM_PROPERTIES;

CREATE TABLE reach_out.SYSTEM_PROPERTIES(
SP_ID int(11) NOT NULL AUTO_INCREMENT,
SP_NAME varchar(200) UNIQUE NOT NULL,
SP_VALUE varchar(200) NOT NULL,
PRIMARY KEY(SP_ID)
);

INSERT INTO reach_out.SYSTEM_PROPERTIES (SP_NAME, SP_VALUE) VALUES ("IMAGE_DIR", "C:\xampp\images");


DROP TABLE IF EXISTS reach_out.PASSWORD_RESET;

CREATE TABLE reach_out.PASSWORD_RESET(
PR_ID int(11) NOT NULL,
USERS_ID int(65) NOT NULL,
PR_CODE VARCHAR(100) NOT NULL,
PR_CREATE_DATE BigInt(35) NOT NULL,
PRIMARY KEY(PR_ID)
);
