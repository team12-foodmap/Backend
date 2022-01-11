package com.example.foodmap.dto.Restaurant;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class reviewResponseDto {
    private String content;
    private String createdAt;
    private String modifiedAt;
}
