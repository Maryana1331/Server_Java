-- changeset maryana1233:V001_create_user_entity_table
CREATE TABLE IF NOT EXISTS schema.user_entity (
                                    id UUID PRIMARY KEY ,
                                    avatar VARCHAR(255),
                                    name VARCHAR(255) NOT NULL,
                                    email VARCHAR(255) NOT NULL UNIQUE,
                                    password VARCHAR(255) NOT NULL,
                                    role VARCHAR(50)
);