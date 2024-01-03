package com.daycanvas.dto.post;

import lombok.Getter;

@Getter
public class PostRequestDto {

    private Long id;
    private Long user_id; // @Todo 추후 변경
    private String content;

}
