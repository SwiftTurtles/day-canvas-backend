package com.daycanvas.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MonthlyPostResponseDto {

    private List<DayImageMappingDto> imagePaths;
}
