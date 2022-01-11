package com.example.foodmap.dto.Restaurant;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RestaurantReviewResponseDto {
    private Long reviewId;
    private String image;

    @Builder
    public RestaurantReviewResponseDto(Long reviewId, String image) {
        this.reviewId = reviewId;
        this.image = image;
    }
}