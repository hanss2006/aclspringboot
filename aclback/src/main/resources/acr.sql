CREATE TABLE acl_sid (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    principal BOOLEAN NOT NULL,
    sid VARCHAR(100) NOT NULL,
    UNIQUE(sid, principal)
);
CREATE TABLE acl_class (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    class VARCHAR(255) NOT NULL,
    UNIQUE(class)
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