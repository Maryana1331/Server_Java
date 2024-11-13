package com.example.Advanced_server_Ostrogotskaya.mapper;

import com.example.Advanced_server_Ostrogotskaya.dto.request.NewsRequest;
import com.example.Advanced_server_Ostrogotskaya.dto.response.GetNewsOutResponse;
import com.example.Advanced_server_Ostrogotskaya.entities.NewsEntity;
import com.example.Advanced_server_Ostrogotskaya.entities.TagEntity;
import com.example.Advanced_server_Ostrogotskaya.entities.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface NewsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "username", source = "userEntity.name")
    NewsEntity newsRequestToNewsEntity(NewsRequest newsRequest, UserEntity userEntity);

    @InheritInverseConfiguration
    List<GetNewsOutResponse> newsEntityToGetNewsDto(List<NewsEntity> newsEntity);

    @Mapping(target = "tags", source = "title")
    NewsEntity updateNewsEntity(NewsRequest newsRequest);

    default Set<TagEntity> map(String title) {
        TagEntity tagEntity = new TagEntity(title);
        return Set.of(tagEntity);
    }

}
