package com.example.foodmap.dto.mypage;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyReviewDetailResponseDto {
    private Long reviewId;
    private Long restaurantId;
    private Long userId;
    private String content;
    private int spicy;
    private int restaurantTags;
    private String image;
    private int reviewLikes;
}

