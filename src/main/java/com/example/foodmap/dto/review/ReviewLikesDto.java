package com.example.foodmap.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReviewLikesDto {
    private Long userId;

    public ReviewLikesDto(Long userId) {
        this.userId = userId;
    }
}
