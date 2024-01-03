package com.daycanvas.dto.post;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {

    private LocalDateTime writtenDate;
    private String content;
    private String imagePath;
}
