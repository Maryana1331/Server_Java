package com.example.Advanced_server_Ostrogotskaya.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PageableResponse<T> {
    private T content;

    private Long numberOfElements;

    public PageableResponse(T content) {
        this.content = content;
    }


}
