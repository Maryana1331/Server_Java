-- changeset maryana1233:V002_create_news_table
CREATE TABLE if not exists schema.news (
                             id SERIAL PRIMARY KEY,
                             user_id UUID REFERENCES schema.user_entity(id) ON DELETE CASCADE,
                             username VARCHAR(255),
                             description TEXT,
                             image VARCHAR(255),
                             title VARCHAR(255)
);