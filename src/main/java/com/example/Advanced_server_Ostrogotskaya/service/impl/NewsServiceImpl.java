package com.example.Advanced_server_Ostrogotskaya.service.impl;

import com.example.Advanced_server_Ostrogotskaya.constants.ErrorCodes;
import com.example.Advanced_server_Ostrogotskaya.dto.request.NewsRequest;
import com.example.Advanced_server_Ostrogotskaya.dto.response.CreateNewsSuccessResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.GetNewsOutResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.PageableResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.common.BaseSuccessResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.common.CustomSuccessResponse;
import com.example.Advanced_server_Ostrogotskaya.entities.NewsEntity;
import com.example.Advanced_server_Ostrogotskaya.entities.UserEntity;
import com.example.Advanced_server_Ostrogotskaya.errors.CustomException;
import com.example.Advanced_server_Ostrogotskaya.mapper.NewsMapper;
import com.example.Advanced_server_Ostrogotskaya.repositories.NewsRepository;
import com.example.Advanced_server_Ostrogotskaya.repositories.UserRepository;
import com.example.Advanced_server_Ostrogotskaya.security.CustomUserDetails;
import com.example.Advanced_server_Ostrogotskaya.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    private final UserRepository userRepository;

    private final NewsMapper newsMapper;

    private final TagServiceImpl tagService;

    @Override
    @Transactional
    public CreateNewsSuccessResponse newsCreate(NewsRequest newsRequest) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        UserEntity contextUser = userRepository.findById(UUID.fromString(userDetails.getUsername()))
                .orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));
        NewsEntity newsEntity = newsMapper.newsRequestToNewsEntity(newsRequest, contextUser);
        Set<String> tags = newsRequest.getTags() != null ? newsRequest.getTags() : new HashSet<>();
        newsEntity.setTags(tagService.createTags(tags));
        newsEntity.setUserEntity(userDetails.getUserEntity());
        newsRepository.save(newsEntity);
        return new CreateNewsSuccessResponse(newsEntity.getId());
    }

    @Override
    public PageableResponse<List<GetNewsOutResponse>> getAllNews(int page, int perPage) {
        List<NewsEntity> allNews = newsRepository.findAll(PageRequest.of(page - 1, perPage)).toList();
        List<GetNewsOutResponse> getNews = newsMapper.newsEntityToGetNewsDto(allNews);
        PageableResponse<List<GetNewsOutResponse>> pageableResponse = new PageableResponse<>(
                getNews);
        pageableResponse.setNumberOfElements((long) allNews.size());
        return pageableResponse;
    }

    @Override
    public PageableResponse<List<GetNewsOutResponse>> getNewsByUserId(
            UUID id, int page, int perPage) {
        List<NewsEntity> newsEntityList = newsRepository.findByUserEntityId(id);
        if (!newsEntityList.isEmpty()) {
            List<GetNewsOutResponse> getNews = newsMapper.newsEntityToGetNewsDto(newsEntityList);
            PageableResponse<List<GetNewsOutResponse>> pageableResponse = new PageableResponse<>(getNews);
            pageableResponse.setNumberOfElements((long) newsEntityList.size());
            return pageableResponse;
        } else {
            throw new CustomException(ErrorCodes.USER_NOT_FOUND);
        }
    }

    @Override
    public PageableResponse<List<GetNewsOutResponse>> findNews(
            String username,
            String keywords,
            int page,
            int perPage,
            List<String> tags) {

        Pageable pageable = PageRequest.of(page, perPage);
        Page<NewsEntity> newsEntityPage;
        newsEntityPage = newsRepository.findNewsEntityByUsernameOrKeywordsOrTags(username, keywords, tags, pageable);
        List<GetNewsOutResponse> getNews = newsMapper.newsEntityToGetNewsDto(newsEntityPage.getContent());
        PageableResponse<List<GetNewsOutResponse>> pageableResponse = new PageableResponse<>(getNews,
                (long) newsEntityPage.getNumberOfElements());
        return pageableResponse;
    }

    @Override
    @Transactional
    public BaseSuccessResponse deleteNews(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomException(ErrorCodes.UNAUTHORISED);
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UUID authenticatedUserId = UUID.fromString(userDetails.getUsername());
        NewsEntity newsEntity = newsRepository.findById(id).orElseThrow(() -> new CustomException(
                ErrorCodes.NEWS_NOT_FOUND));
        if (!newsEntity.getUserEntity().getId().equals(authenticatedUserId)) {
            throw new CustomException(ErrorCodes.NEWS_NOT_FOUND);
        }
        newsRepository.delete(newsEntity);
        return new BaseSuccessResponse();
    }

    @Override
    public BaseSuccessResponse updateNews(NewsRequest newsRequest, Long id) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        UUID authenticatedUserId = UUID.fromString(userDetails.getUsername());

        NewsEntity newsEntity = newsRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCodes.NEWS_NOT_FOUND));

        if (!authenticatedUserId.equals(newsEntity.getUserEntity().getId())) {
            throw new CustomException(ErrorCodes.UNAUTHORISED);
        }
        newsEntity = newsMapper.updateNewsEntity(newsRequest);
        newsRepository.save(newsEntity);
        return new BaseSuccessResponse();
    }
}
