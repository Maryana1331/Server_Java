package com.example.Advanced_server_Ostrogotskaya.service;

import com.example.Advanced_server_Ostrogotskaya.entities.TagEntity;
import java.util.Set;

public interface TagService {
    Set<TagEntity> createTags(Set<String> tag);

}
