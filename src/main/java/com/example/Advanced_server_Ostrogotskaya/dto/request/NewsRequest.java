package com.example.Advanced_server_Ostrogotskaya.dto.request;

import com.example.Advanced_server_Ostrogotskaya.constants.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Validated
@NoArgsConstructor
public class NewsRequest {

    @Size(min = 3, max = 160, message = ValidationConstants.NEWS_DESCRIPTION_SIZE)
    @NotBlank(message = ValidationConstants.NEWS_DESCRIPTION_NOT_NULL)
    private String description;

    @Size(min = 3, max = 130, message = ValidationConstants.NEWS_IMAGE_HAS_TO_BE_PRESENT)
    private String image;

    private Set<@NotBlank(message = ValidationConstants.TAGS_NOT_VALID) String> tags;

    @Size(min = 3, max = 160, message = ValidationConstants.NEWS_DESCRIPTION_SIZE)
    private String title;

}
