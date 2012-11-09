DROP TABLE EXPLORER_PROPERTY_DEFINITION;

//
// Attributes
//
DROP TABLE ATTRIBUTES IF EXISTS;
CREATE CACHED TABLE ATTRIBUTES
(
   PROPERTY_CLASS VARCHAR(255) DEFAULT 'userAttributes' NOT NULL,
   CLASS_KEY VARCHAR(255) NOT NULL,
   ATTRIBUTE_NAME VARCHAR(255) NOT NULL,
   ATTRIBUTE_VALUE VARCHAR(),
   CONSTRAINT ATTRIBUTES_PK PRIMARY KEY (PROPERTY_CLASS,CLASS_KEY,ATTRIBUTE_NAME)
);
INSERT INTO ATTRIBUTES (CLASS_KEY, ATTRIBUTE_NAME, ATTRIBUTE_VALUE) SELECT CONCAT(USERNAME,'_1'), ATTRIBUTE_NAME,ATTRIBUTE_VALUE FROM USER_ATTRIBUTES;
DROP TABLE USER_ATTRIBUTES;

//
// Attribute definitions
//

DROP TABLE ATTRIBUTE_DEFINITION IF EXISTS;
CREATE CACHED TABLE ATTRIBUTE_DEFINITION
(
   NAME VARCHAR(255),
   PROPERTY_CLASS VARCHAR(255) DEFAULT 'userAttributes',
   VISIBILITY TINYINT NOT NULL,
   TYPE TINYINT NOT NULL,
   SORT_ORDER INTEGER NOT NULL,
   TEXT_LABEL VARCHAR(255) NOT NULL,
   TEXT_DESCRIPTION VARCHAR(65535) NOT NULL,
   TYPE_META VARCHAR(255) NOT NULL,
   CATEGORY INTEGER NOT NULL,
   CATEGORY_LABEL VARCHAR(255) NOT NULL,
   DEFAULT_VALUE VARCHAR(255) NOT NULL,
   HIDDEN TINYINT,
   VALIDATION_STRING VARCHAR(255) NOT NULL,
   CONSTRAINT ATTRIBUTE_DEFINITION_PK PRIMARY KEY (NAME,PROPERTY_CLASS)
);
INSERT INTO ATTRIBUTE_DEFINITION (NAME, VISIBILITY, TYPE, SORT_ORDER, TEXT_LABEL, TEXT_DESCRIPTION,
	TYPE_META, CATEGORY, CATEGORY_LABEL, DEFAULT_VALUE,
	HIDDEN,VALIDATION_STRING) SELECT NAME, VISIBILITY, TYPE, SORT_ORDER, TEXT_LABEL, TEXT_DESCRIPTION,
	TYPE_META, CATEGORY, CATEGORY_LABEL, DEFAULT_VALUE, 0, '' FROM USER_ATTRIBUTE_DEFINITION;
DROP TABLE USER_ATTRIBUTE_DEFINITION;

// TODO we need to copy in the old user attributes



CREATE CACHED TABLE EXPLORER_PROPERTIES_TMP
(
   KEY_1 VARCHAR(255) not null,
   KEY_2 VARCHAR(255) not null,
   KEY_3 VARCHAR(255) not null,
   KEY_4 VARCHAR(255) not null,
   KEY_5 VARCHAR(255) not null,
   VALUE VARCHAR(255) not null
);

CREATE UNIQUE INDEX SYS_IDX_PROPERTY_CONSTRAINT_2 ON EXPLORER_PROPERTIES_TMP
(
  KEY_1,
  KEY_2,
  KEY_3,
  KEY_4,
  KEY_5
);

INSERT INTO EXPLORER_PROPERTIES_TMP (KEY_1,KEY_2,KEY_3,KEY_4, KEY_5, VALUE) SELECT NAME,USERNAME,PROFILE_ID,'0','',VALUE FROM EXPLORER_PROPERTIES;

DROP TABLE EXPLORER_PROPERTIES;
ALTER TABLE EXPLORER_PROPERTIES_TMP RENAME TO EXPLORER_PROPERTIES;

ALTER TABLE PROPERTY_PROFILES ADD COLUMN REALM_ID INTEGER DEFAULT 1 ;

// Fix all known realm properties giving them realm 1

UPDATE EXPLORER_PROPERTIES SET KEY_4 = '1' WHERE KEY_1 = 'security.userDatabase';
UPDATE EXPLORER_PROPERTIES SET KEY_4 = '1' WHERE KEY_1 = 'security.administrators';
UPDATE EXPLORER_PROPERTIES SET KEY_4 = '1' WHERE KEY_1 = 'security.password.pattern';
UPDATE EXPLORER_PROPERTIES SET KEY_4 = '1' WHERE KEY_1 = 'security.password.pattern.description';
UPDATE EXPLORER_PROPERTIES SET KEY_4 = '1' WHERE KEY_1 = 'security.password.daysBeforeExpiryWarning';
UPDATE EXPLORER_PROPERTIES SET KEY_4 = '1' WHERE KEY_1 = 'security.password.daysBeforeExpiry';
UPDATE EXPLORER_PROPERTIES SET KEY_4 = '1' WHERE KEY_1 = 'security.multipleSessions';
UPDATE EXPLORER_PROPERTIES SET KEY_4 = '1' WHERE KEY_1 = 'security.maxLogonAttemptsBeforeLock';
UPDATE EXPLORER_PROPERTIES SET KEY_4 = '1' WHERE KEY_1 = 'security.maxLocksBeforeDisable';
UPDATE EXPLORER_PROPERTIES SET KEY_4 = '1' WHERE KEY_1 = 'security.lockDuration';
UPDATE EXPLORER_PROPERTIES SET KEY_4 = '1' WHERE KEY_1 LIKE 'activeDirectory.%';
UPDATE EXPLORER_PROPERTIES SET KEY_4 = '1' WHERE KEY_1 LIKE 'ldap%'; 
UPDATE EXPLORER_PROPERTIES SET KEY_4 = '1' WHERE KEY_1 LIKE 'nis.%';