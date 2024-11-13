package com.example.Advanced_server_Ostrogotskaya.controller;

import com.example.Advanced_server_Ostrogotskaya.constants.ValidationConstants;
import com.example.Advanced_server_Ostrogotskaya.dto.request.NewsRequest;
import com.example.Advanced_server_Ostrogotskaya.dto.response.CreateNewsSuccessResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.GetNewsOutResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.PageableResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.common.BaseSuccessResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.common.CustomSuccessResponse;
import com.example.Advanced_server_Ostrogotskaya.service.NewsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @PostMapping
    public ResponseEntity<CreateNewsSuccessResponse> createNews(@Valid @RequestBody NewsRequest newsRequest) {
        return ResponseEntity.ok(newsService.newsCreate(newsRequest));
    }

    @GetMapping
    public ResponseEntity<CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>>> getAllNews(
            @Positive(message = ValidationConstants.TASKS_PAGE_GREATER_OR_EQUAL_1)
            @RequestParam int page, @Positive(message = ValidationConstants.TASKS_PER_PAGE_GREATER_OR_EQUAL_1)
            @Max(value = 100, message = ValidationConstants.TASKS_PER_PAGE_LESS_OR_EQUAL_100)
            @RequestParam int perPage) {
        return ResponseEntity.ok(new CustomSuccessResponse(newsService.getAllNews(page, perPage)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>>> getNewsByUserId(
            @PathVariable UUID id, @RequestBody
            @Positive(message = ValidationConstants.TASKS_PAGE_GREATER_OR_EQUAL_1) int page,
            @Positive(message = ValidationConstants.TASKS_PER_PAGE_GREATER_OR_EQUAL_1)
            @Max(value = 100, message = ValidationConstants.TASKS_PER_PAGE_LESS_OR_EQUAL_100)
            @RequestBody int perPage) {
        return ResponseEntity.ok(new CustomSuccessResponse<>(newsService.getNewsByUserId(id, page, perPage)));
    }

    @GetMapping("/find")
    public ResponseEntity<CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>>> findNews(
            @RequestParam(required = false) String author, @RequestParam(required = false) String keywords,
            @RequestParam int page, @RequestParam int perPage,
            @RequestParam(required = false) List<@NotBlank String> tags) {
        return ResponseEntity.ok(new CustomSuccessResponse<>(newsService.findNews(author, keywords, page, perPage, tags)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseSuccessResponse> deleteNews(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(newsService.deleteNews(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseSuccessResponse> updateNews(@Valid @PathVariable Long id,
                                                          @Valid @RequestBody NewsRequest newsRequest) {
        return ResponseEntity.ok(newsService.updateNews(newsRequest, id));
    }
}
