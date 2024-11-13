package com.example.Advanced_server_Ostrogotskaya.tagServiceTests;

import com.example.Advanced_server_Ostrogotskaya.entities.TagEntity;
import com.example.Advanced_server_Ostrogotskaya.repositories.TagRepository;
import com.example.Advanced_server_Ostrogotskaya.service.impl.TagServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {
    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagRepository tagRepository;

    @Test
    @DisplayName("Create new tags")
    public void createNewTags() {
        Set<String> tags = new HashSet<>();
        tags.add("Tag1");
        tags.add("Tag2");
        when(tagRepository.findByTitle("Tag1")).thenReturn(null);
        when(tagRepository.findByTitle("Tag2")).thenReturn(null);
        when(tagRepository.save(any(TagEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Set<TagEntity> result = tagService.createTags(tags);
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.stream().anyMatch(tag -> "Tag1".equals(tag.getTitle())));
        Assertions.assertTrue(result.stream().anyMatch(tag -> "Tag2".equals(tag.getTitle())));
        verify(tagRepository, times(2)).save(any(TagEntity.class));
    }

    @Test
    @DisplayName("test Create Tags Existing Tags")
    void testCreateTags_ExistingTags() {
        Set<String> tags = new HashSet<>();
        tags.add("Tag1");
        tags.add("Tag2");
        TagEntity existingTag = new TagEntity("Tag1");
        when(tagRepository.findByTitle("Tag1")).thenReturn(existingTag);
        when(tagRepository.findByTitle("Tag2")).thenReturn(null);
        when(tagRepository.save(any(TagEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Set<TagEntity> result = tagService.createTags(tags);
        assertEquals(2, result.size());
        assertTrue(result.contains(existingTag));
        assertTrue(result.stream().anyMatch(tag -> tag.getTitle().equals("Tag2")));
        verify(tagRepository, times(1)).save(any(TagEntity.class));
    }
}
