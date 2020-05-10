-- UPDATES TO THE DATABASE FOR MVP_RELEASE AFTER THE HACK RELEASE
DROP TABLE IF EXISTS reach_out.INTERNAL_MESSAGES;

CREATE TABLE reach_out.INTERNAL_MESSAGES(
IM_ID int(11) NOT NULL,
IM_ORIG_ID int(11) NOT NULL,
IM_TARGET_ID int(11) NOT NULL,
IM_MESSAGE varchar(2048) NOT NULL,
IM_CREATE_DATE BigInt(35) NOT NULL,
IM_VIEWED BOOLEAN,
PRIMARY KEY(IM_ID)
);