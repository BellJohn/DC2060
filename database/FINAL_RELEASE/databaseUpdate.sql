-- UPDATE FOR FINAL_RELEASE AFTER THE MVP_RELEASE 
DROP TABLE IF EXISTS reach_out.PASSWORD_RESET;

CREATE TABLE reach_out.PASSWORD_RESET(
PR_ID int(11) NOT NULL,
USERS_ID int(65) NOT NULL,
PR_CODE VARCHAR(100) NOT NULL,
PR_CREATE_DATE BigInt(35) NOT NULL,
PRIMARY KEY(PR_ID)
);
