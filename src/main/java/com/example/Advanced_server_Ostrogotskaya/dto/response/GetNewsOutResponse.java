package com.example.Advanced_server_Ostrogotskaya.dto.response;

import com.example.Advanced_server_Ostrogotskaya.entities.TagEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetNewsOutResponse {
    private String description;

    private Long id;

    private String image;

    private String title;

    private UUID userId;

    private String username;

    private Set<TagEntity> tags;

    public GetNewsOutResponse(long l) {
    }
}
