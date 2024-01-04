package com.daycanvas.dto.post;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class PostResponseDto {

    private LocalDateTime writtenDate;
    private String content;
    private String imagePath;
}
