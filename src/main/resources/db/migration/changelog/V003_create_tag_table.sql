-- changeset maryana1233:V003_create_tag_table
CREATE TABLE if not exists schema.tags (
                             id SERIAL PRIMARY KEY,
                             title VARCHAR(255) UNIQUE NOT NULL
);