-- Table: groups
CREATE TABLE groups
(
    id          serial      NOT NULL,
    name        varchar(30) NOT NULL,
    description varchar(50) NOT NULL,
    CONSTRAINT groups_pk PRIMARY KEY (id)
);

-- Table: users
CREATE TABLE users
(
    id       serial      NOT NULL,
    login    varchar(30) NOT NULL,
    password varchar(30) NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (id)
);

-- Table: users_groups
CREATE TABLE users_groups
(
    user_id  int NOT NULL,
    group_id int NOT NULL,
    CONSTRAINT users_groups_pk PRIMARY KEY (user_id, group_id)
);

-- foreign keys
-- Reference: group_user_group (table: users_groups)
ALTER TABLE users_groups
    ADD CONSTRAINT group_user_group
        FOREIGN KEY (group_id)
            REFERENCES groups (id)
                NOT DEFERRABLE
                    INITIALLY IMMEDIATE
;

-- Reference: group_user_user (table: users_groups)
ALTER TABLE users_groups
    ADD CONSTRAINT group_user_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
                NOT DEFERRABLE
                    INITIALLY IMMEDIATE
;

-- End of file.

