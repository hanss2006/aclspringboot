CREATE TABLE dict_org_unit (
  id integer NOT NULL,
  full_name varchar(1000),
  PRIMARY KEY (id)
);
CREATE TABLE dict_rate (
  id integer GENERATED BY DEFAULT AS IDENTITY,
  code varchar(255) NOT NULL,
  full_name varchar(255),
  PRIMARY KEY (id)
);
CREATE TABLE dict_rate_asfp (
  id integer GENERATED BY DEFAULT AS IDENTITY,
  code varchar(6),
  full_name varchar(200),
  name varchar(100),
  PRIMARY KEY (id)
);
CREATE TABLE form_skim_it (
  id integer GENERATED BY DEFAULT AS IDENTITY,
  dict_rate_id integer,
  dict_org_unit_id integer,
  PRIMARY KEY (id)
);
CREATE TABLE form_asfp (
  id integer GENERATED BY DEFAULT AS IDENTITY,
  dict_rate_asfp_id integer,
  dict_org_unit_id integer,
  PRIMARY KEY (id)
);
ALTER TABLE
  dict_rate_asfp
DROP
  CONSTRAINT IF EXISTS UK_7wbo04qxbpgu890tbdfkdto9b;
ALTER TABLE
  dict_rate_asfp
ADD
  CONSTRAINT UK_7wbo04qxbpgu890tbdfkdto9b UNIQUE (code);
ALTER TABLE
  form_skim_it
DROP
  CONSTRAINT IF EXISTS skim_it_unique_columns;
ALTER TABLE
  form_skim_it
ADD
  CONSTRAINT skim_it_unique_columns UNIQUE (dict_rate_id);
ALTER TABLE
  form_skim_it
ADD
  CONSTRAINT FKtqc7m7d3klt6rbkacmyljd81s FOREIGN KEY (dict_rate_id) REFERENCES dict_rate;
ALTER TABLE
  form_skim_it
ADD
  CONSTRAINT FKtk9lypvkvc0c7q95yw4tdrub4 FOREIGN KEY (dict_org_unit_id) REFERENCES dict_org_unit;
ALTER TABLE
  form_asfp
ADD
  CONSTRAINT FK27w9evd8ju3d2qr5pp9s5vgng FOREIGN KEY (dict_rate_asfp_id) REFERENCES dict_rate_asfp;
ALTER TABLE
  form_asfp
ADD
  CONSTRAINT FKs549phg7xxrt0gfpxrgfehvig FOREIGN KEY (dict_org_unit_id) REFERENCES dict_org_unit;
CREATE TABLE acl_sid (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  principal BOOLEAN NOT NULL,
  sid VARCHAR(100) NOT NULL,
  UNIQUE(sid, principal)
);
CREATE TABLE acl_class (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  CLASS VARCHAR(255) NOT NULL,
  UNIQUE(CLASS)
);
CREATE TABLE acl_object_identity (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  object_id_class BIGINT NOT NULL,
  object_id_identity BIGINT NOT NULL,
  parent_object BIGINT,
  owner_sid BIGINT,
  entries_inheriting BOOLEAN NOT NULL,
  FOREIGN KEY (object_id_class) REFERENCES acl_class(id),
  FOREIGN KEY (parent_object) REFERENCES acl_object_identity(id),
  FOREIGN KEY (owner_sid) REFERENCES acl_sid(id)
);
CREATE TABLE acl_entry (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  acl_object_identity BIGINT NOT NULL,
  ace_order INT NOT NULL,
  sid BIGINT NOT NULL,
  mask INTEGER NOT NULL,
  granting BOOLEAN NOT NULL,
  audit_success BOOLEAN NOT NULL,
  audit_failure BOOLEAN NOT NULL,
  FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity(id),
  FOREIGN KEY (sid) REFERENCES acl_sid(id)
);
INSERT INTO acl_class (id, CLASS)
VALUES
  (
    1, 'com.ocrv.skimrv.backend.dictionaries.entities.simple.OrgUnitDictionary'
  );
INSERT INTO acl_class (id, CLASS)
VALUES
  (
    2, 'com.ocrv.skimrv.backend.dictionaries.entities.skim.DictRate'
  );
INSERT INTO acl_class (id, CLASS)
VALUES
  (
    3, 'com.ocrv.skimrv.backend.dictionaries.entities.asfp.DictRateAsfp'
  );
INSERT INTO ACL_SID (PRINCIPAL, SID)
VALUES
  (FALSE, 'ROLE_ADMIN');
INSERT INTO ACL_SID (PRINCIPAL, SID)
VALUES
  (FALSE, 'ROLE_USER');
INSERT INTO ACL_SID (PRINCIPAL, SID)
VALUES
  (FALSE, 'ROLE_MODERATOR');
INSERT INTO acl_object_identity (
  id, object_id_class, object_id_identity,
  parent_object, owner_sid, entries_inheriting
)
VALUES
  (1, 1, 1, NULL, 1, 0),
  (2, 1, 2, NULL, 1, 0),
  (3, 1, 3, NULL, 1, 0),
  (4, 1, 4, NULL, 1, 0),
  (5, 1, 5, NULL, 1, 0),
  (6, 2, 1, NULL, 1, 0),
  (7, 2, 2, NULL, 1, 0),
  (8, 2, 3, NULL, 1, 0),
  (9, 2, 4, NULL, 1, 0),
  (10, 2, 5, NULL, 1, 0),
  (11, 3, 1, NULL, 1, 0),
  (12, 3, 2, NULL, 1, 0),
  (13, 3, 3, NULL, 1, 0),
  (14, 3, 4, NULL, 1, 0),
  (15, 3, 5, NULL, 1, 0);
INSERT INTO acl_entry (
  id, acl_object_identity, ace_order,
  sid, mask, granting, audit_success,
  audit_failure
)
VALUES
  (1, 1, 1, 1, 1, 1, 1, 1),
  (2, 1, 2, 3, 1, 1, 1, 1),
  (3, 1, 3, 3, 2, 1, 1, 1);
INSERT INTO acl_entry (
  id, acl_object_identity, ace_order,
  sid, mask, granting, audit_success,
  audit_failure
)
VALUES
  (4, 2, 1, 2, 1, 1, 1, 1),
  (5, 2, 2, 3, 1, 1, 1, 1),
  (6, 2, 3, 3, 2, 1, 1, 1);
INSERT INTO acl_entry (
  id, acl_object_identity, ace_order,
  sid, mask, granting, audit_success,
  audit_failure
)
VALUES
  (7, 3, 1, 3, 1, 1, 1, 1),
  (8, 3, 2, 3, 2, 1, 1, 1);
INSERT INTO DICT_ORG_UNIT (ID, FULL_NAME)
VALUES
  (1, 'Unit 1'),
  (2, 'Unit 2'),
  (3, 'Unit 3'),
  (4, 'Unit 4'),
  (5, 'Unit 5');
INSERT INTO DICT_RATE (ID, CODE, FULL_NAME)
VALUES
  (1, '1', 'RATE 1'),
  (2, '2', 'RATE 2'),
  (3, '3', 'RATE 3'),
  (4, '4', 'RATE 4'),
  (5, '5', 'RATE 5');
INSERT INTO DICT_RATE_ASFP (ID, CODE, NAME, FULL_NAME)
VALUES
  (1, '1', 'RATE 1', 'RATE 1'),
  (2, '2', 'RATE 2', 'RATE 2'),
  (3, '3', 'RATE 3', 'RATE 3'),
  (4, '4', 'RATE 4', 'RATE 4'),
  (5, '5', 'RATE 5', 'RATE 5');

INSERT INTO FORM_ASFP (ID, dict_org_unit_id, dict_rate_asfp_id)
VALUES
  (1, 1, 1),
  (2, 2, 2),
  (3, 3, 3),
  (4, 4, 4),
  (5, 5, 5);

INSERT INTO FORM_SKIM_IT (ID, dict_rate_id, dict_org_unit_id)
VALUES
  (1, 1, 1),
  (2, 2, 2),
  (3, 3, 3),
  (4, 4, 4),
  (5, 5, 5);
