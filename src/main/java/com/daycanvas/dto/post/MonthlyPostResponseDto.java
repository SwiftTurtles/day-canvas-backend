package com.daycanvas.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class MonthlyPostResponseDto {

    private List<String> imagePaths;
}
