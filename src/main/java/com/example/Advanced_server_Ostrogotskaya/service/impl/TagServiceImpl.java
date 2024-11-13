package com.example.Advanced_server_Ostrogotskaya.service.impl;

import com.example.Advanced_server_Ostrogotskaya.entities.TagEntity;
import com.example.Advanced_server_Ostrogotskaya.repositories.TagRepository;
import com.example.Advanced_server_Ostrogotskaya.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public Set<TagEntity> createTags(Set<String> tags) {
        Set<TagEntity> tagEntities = new HashSet<>();

        for (String title : tags) {
            TagEntity existingTag = tagRepository.findByTitle(title);
            if (existingTag == null) {
                TagEntity newTag = new TagEntity(title);
                newTag.setTitle(title);
                newTag = tagRepository.save(newTag);
                tagEntities.add(newTag);
            } else {
                tagEntities.add(existingTag);
            }
        }

        return tagEntities;
    }
}
