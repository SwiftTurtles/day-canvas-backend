package com.daycanvas.dto.post;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostRequestDto {

    private Long id;
    private Long userId; // @Todo 추후 변경
    private String content;

}
