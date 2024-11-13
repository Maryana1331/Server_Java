package com.example.Advanced_server_Ostrogotskaya.service;

import com.example.Advanced_server_Ostrogotskaya.dto.request.NewsRequest;
import com.example.Advanced_server_Ostrogotskaya.dto.response.CreateNewsSuccessResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.GetNewsOutResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.PageableResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.common.BaseSuccessResponse;
import java.util.List;
import java.util.UUID;

public interface NewsService {
    CreateNewsSuccessResponse newsCreate(NewsRequest newsRequest);

    PageableResponse<List<GetNewsOutResponse>> getAllNews(int page, int parPage);

    PageableResponse<List<GetNewsOutResponse>> getNewsByUserId(UUID id, int page, int perPage);

    PageableResponse<List<GetNewsOutResponse>> findNews(String author, String keywords, int page, int perPage, List<String> tags);

    BaseSuccessResponse deleteNews(Long id);

    BaseSuccessResponse updateNews(NewsRequest newsRequest, Long id);
}
