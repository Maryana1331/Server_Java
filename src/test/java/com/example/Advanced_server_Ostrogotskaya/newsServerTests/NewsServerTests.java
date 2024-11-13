package com.example.Advanced_server_Ostrogotskaya.newsServerTests;

import com.example.Advanced_server_Ostrogotskaya.ConstantTest;
import com.example.Advanced_server_Ostrogotskaya.constants.ErrorCodes;
import com.example.Advanced_server_Ostrogotskaya.dto.request.NewsRequest;
import com.example.Advanced_server_Ostrogotskaya.dto.response.CreateNewsSuccessResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.GetNewsOutResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.PageableResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.common.BaseSuccessResponse;
import com.example.Advanced_server_Ostrogotskaya.entities.NewsEntity;
import com.example.Advanced_server_Ostrogotskaya.entities.TagEntity;
import com.example.Advanced_server_Ostrogotskaya.entities.UserEntity;
import com.example.Advanced_server_Ostrogotskaya.errors.CustomException;
import com.example.Advanced_server_Ostrogotskaya.mapper.NewsMapper;
import com.example.Advanced_server_Ostrogotskaya.repositories.NewsRepository;
import com.example.Advanced_server_Ostrogotskaya.repositories.UserRepository;
import com.example.Advanced_server_Ostrogotskaya.security.CustomUserDetails;
import com.example.Advanced_server_Ostrogotskaya.service.impl.NewsServiceImpl;
import com.example.Advanced_server_Ostrogotskaya.service.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class NewsServerTests extends ConstantTest {

    @InjectMocks
    private NewsServiceImpl newsService;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NewsMapper newsMapper;

    @Mock
    private TagServiceImpl tagService;

    private UserEntity user;

    @BeforeEach
    public void setup() {
        UUID authenticatedUserId = UUID.randomUUID();
        user = createUser(authenticatedUserId);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        Authentication mockAuthentication = mock(Authentication.class);
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        when(mockAuthentication.getPrincipal()).thenReturn(userDetails);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        SecurityContextHolder.setContext(mockSecurityContext);
    }

    @Test
    @DisplayName("Create new news successfully")
    void testNewsCreate_Success() {
        NewsRequest newsRequest = createNewsRequest();
        NewsEntity newsEntity = createNews();
        newsEntity.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(newsMapper.newsRequestToNewsEntity(newsRequest, user)).thenReturn(newsEntity);
        when(tagService.createTags(any())).thenReturn(Collections.emptySet());
        when(newsRepository.save(newsEntity)).thenReturn(newsEntity);

        CreateNewsSuccessResponse response = newsService.newsCreate(newsRequest);

        assertNotNull(response);
        assertEquals(newsEntity.getId(), response.getId());
        verify(userRepository).findById(user.getId());
        verify(newsMapper).newsRequestToNewsEntity(newsRequest, user);
        verify(tagService).createTags(any());
        verify(newsRepository).save(newsEntity);
    }

    @Test
    @DisplayName("Create news with tags")
    void testNewsCreate_WithTags() {
        NewsRequest newsRequest = new NewsRequest();
        Set<String> tags = new HashSet<>(Arrays.asList("tag1", "tag2"));
        newsRequest.setTags(tags);

        NewsEntity newsEntity = createNews();
        newsEntity.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(newsMapper.newsRequestToNewsEntity(newsRequest, user)).thenReturn(newsEntity);
        Set<TagEntity> tagEntities = new HashSet<>();
        for (String tag : tags) {
            TagEntity tagEntity = new TagEntity();
            tagEntity.setTitle(tag);
            tagEntities.add(tagEntity);
        }
        when(tagService.createTags(tags)).thenReturn(tagEntities);

        CreateNewsSuccessResponse response = newsService.newsCreate(newsRequest);

        assertNotNull(response);
        assertEquals(newsEntity.getId(), response.getId());
        verify(newsRepository).save(newsEntity);
        assertEquals(tagEntities, newsEntity.getTags());
    }

    @Test
    @DisplayName("Create news without tags")
    void testNewsCreate_WithoutTags() {
        NewsRequest newsRequest = new NewsRequest();
        newsRequest.setTags(null);

        NewsEntity newsEntity = createNews();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(newsMapper.newsRequestToNewsEntity(newsRequest, user)).thenReturn(newsEntity);
        when(tagService.createTags(new HashSet<>())).thenReturn(new HashSet<>());

        CreateNewsSuccessResponse response = newsService.newsCreate(newsRequest);

        assertNotNull(response);
        assertEquals(newsEntity.getId(), response.getId());
        verify(newsRepository).save(newsEntity);
        assertTrue(newsEntity.getTags().isEmpty());
    }

    @Test
    @DisplayName("Throw exception when user not found")
    void testNewsCreate_UserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        NewsRequest newsRequest = new NewsRequest();
        newsRequest.setTags(Collections.singleton("tag1"));

        CustomException exception = assertThrows(CustomException.class, () -> newsService.newsCreate(newsRequest));
        assertEquals(ErrorCodes.USER_NOT_FOUND, exception.getErrorCode());

        verify(userRepository).findById(user.getId());
        verify(newsMapper, never()).newsRequestToNewsEntity(any(), any());
        verify(tagService, never()).createTags(any());
        verify(newsRepository, never()).save(any());
    }

    @Test
    @DisplayName("Get all news successfully")
    void testGetAllNews_Success() {
        int page = 1;
        int perPage = 5;
        List<NewsEntity> allNews = Arrays.asList(createNews(), createNews());
        Page<NewsEntity> newsPage = new PageImpl<>(
                allNews, PageRequest.of(page - 1, perPage), allNews.size());

        when(newsRepository.findAll(any(PageRequest.class))).thenReturn(newsPage);
        List<GetNewsOutResponse> getNewsDtos = Arrays.asList(
                new GetNewsOutResponse(1L), new GetNewsOutResponse(2L));
        when(newsMapper.newsEntityToGetNewsDto(allNews)).thenReturn(getNewsDtos);

        PageableResponse<List<GetNewsOutResponse>> response = newsService.getAllNews(page, perPage);

        assertNotNull(response);
        assertNotNull(response.getContent());
        assertEquals(2, response.getContent().size());
        verify(newsRepository).findAll(PageRequest.of(0, perPage));
        verify(newsMapper).newsEntityToGetNewsDto(allNews);
    }

    @Test
    @DisplayName("Find news by username and keywords successfully")
    void testFindNewsByUser_Success() {
        UUID userId = UUID.randomUUID();
        List<NewsEntity> newsList = Arrays.asList(createNews(), createNews());
        Page<NewsEntity> newsEntityPage = new PageImpl<>(
                newsList, PageRequest.of(0, 5), newsList.size());

        when(newsRepository.findNewsEntityByUsernameOrKeywordsOrTags(
                any(), any(), any(), any())).thenReturn(newsEntityPage);
        List<GetNewsOutResponse> getNewsDtos = Arrays.asList(
                new GetNewsOutResponse(1L), new GetNewsOutResponse(2L));
        when(newsMapper.newsEntityToGetNewsDto(newsList)).thenReturn(getNewsDtos);

        PageableResponse<List<GetNewsOutResponse>> response = newsService
                .findNews("username", "", 1, 5, Collections.emptyList());

        assertNotNull(response);
        assertNotNull(response.getContent());
        assertEquals(2, response.getContent().size());
        verify(newsRepository).findNewsEntityByUsernameOrKeywordsOrTags(any(), any(), any(), any());
        verify(newsMapper).newsEntityToGetNewsDto(newsList);
    }

    @Test
    @DisplayName("Get news by user ID - user not found")
    void testGetNewsByUserId_UserNotFound() {
        UUID userId = UUID.randomUUID();
        when(newsRepository.findByUserEntityId(userId)).thenReturn(Collections.emptyList());

        CustomException exception = assertThrows(
                CustomException.class, () -> newsService.getNewsByUserId(userId, 1, 5));
        assertEquals(ErrorCodes.USER_NOT_FOUND, exception.getErrorCode());

        verify(newsRepository).findByUserEntityId(userId);
        verify(newsMapper, never()).newsEntityToGetNewsDto(any());
    }

    @Test
    @DisplayName("Delete news successfully")
    void testDeleteNewsByUserSuccess() {
        NewsEntity newsEntity = createNews();
        newsEntity.setId(1L);
        newsEntity.setUserEntity(user);

        when(newsRepository.findById(1L)).thenReturn(Optional.of(newsEntity));

        BaseSuccessResponse response = newsService.deleteNews(1L);

        assertNotNull(response);
        verify(newsRepository).delete(newsEntity);
    }

    @Test
    @DisplayName("Fail to delete news due to user mismatch")
    void testDeleteNews_UserMismatch() {
        NewsEntity newsEntity = createNews();
        newsEntity.setId(1L);
        newsEntity.setUserEntity(createUser(UUID.randomUUID()));

        when(newsRepository.findById(1L)).thenReturn(Optional.of(newsEntity));

        CustomException exception = assertThrows(CustomException.class, () -> newsService.deleteNews(1L));
        assertEquals(ErrorCodes.NEWS_NOT_FOUND, exception.getErrorCode());

        verify(newsRepository).findById(1L);
        verify(newsRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Delete news - unauthorized")
    void testDeleteNewsByUserUnauthorized() {
        SecurityContextHolder.clearContext();
        assertThrows(CustomException.class, () -> newsService.deleteNews(1L));
    }

    @Test
    @DisplayName("Update news successfully")
    void testUpdateNews_Success() {
        Long newsId = 1L;
        NewsRequest newsRequest = createNewsRequest();
        NewsEntity existingNewsEntity = createNews();
        existingNewsEntity.setId(newsId);
        existingNewsEntity.setUserEntity(user);

        when(newsRepository.findById(newsId)).thenReturn(Optional.of(existingNewsEntity));
        NewsEntity updatedEntity = createNews();
        when(newsMapper.updateNewsEntity(newsRequest)).thenReturn(updatedEntity);

        BaseSuccessResponse response = newsService.updateNews(newsRequest, newsId);

        assertNotNull(response);
        verify(newsRepository).save(updatedEntity);
    }

    @Test
    @DisplayName("Fail to update news due to user mismatch")
    void testUpdateNews_UserMismatch() {
        Long newsId = 1L;
        NewsRequest newsRequest = createNewsRequest();
        UserEntity newsOwner = createUser(UUID.randomUUID());
        NewsEntity existingNewsEntity = createNews();
        existingNewsEntity.setId(newsId);
        existingNewsEntity.setUserEntity(newsOwner);
        when(newsRepository.findById(newsId)).thenReturn(Optional.of(existingNewsEntity));
                CustomException exception = assertThrows(
                        CustomException.class, () -> newsService.updateNews(newsRequest, newsId));
        assertEquals(ErrorCodes.UNAUTHORISED, exception.getErrorCode());
        verify(newsRepository).findById(newsId);
        verify(newsRepository, never()).save(any());
    }

    private UserEntity createUser(UUID id) {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setName("user" + id);
        return user;
    }

    private NewsEntity createNews() {
        NewsEntity news = new NewsEntity();
        news.setTitle("Sample Title");
        news.setDescription("Sample Content");
        return news;
    }

    private NewsRequest createNewsRequest() {
        NewsRequest request = new NewsRequest();
        request.setTitle("Sample Title");
        request.setDescription("Sample Content");
        return request;
    }
}
