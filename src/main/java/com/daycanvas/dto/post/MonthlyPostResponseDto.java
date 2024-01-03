package com.daycanvas.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MonthlyPostResponseDto {

    private List<String> imagePaths;
}
