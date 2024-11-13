-- changeset maryana1233:V005_create_log_table
CREATE TABLE if not exists schema.log_entity (
                                   timestamp TIMESTAMP PRIMARY KEY,
                                   method VARCHAR(50),
                                   uri VARCHAR(255),
                                   status VARCHAR(20),
                                   user_name VARCHAR(50),
                                   error_message VARCHAR(255)
);