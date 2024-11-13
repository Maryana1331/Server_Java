-- changeset maryana1233:V004_create_news_tags_table

CREATE TABLE if not exists schema.news_tags (
                                  news_id BIGINT REFERENCES schema.news(id) ON DELETE CASCADE,
                                  tags_id UUID REFERENCES schema.tag(id) ON DELETE CASCADE,
                                  PRIMARY KEY (news_id, tags_id)
);