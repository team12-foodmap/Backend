package com.example.foodmap.dto.review;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewAllResponseDto {
    private Long reviewId;
    private Long restaurantId;
    private Long userId;
    private String content;
    private int spicy;
    private int restaurantTags;
    private String image;
    private int reviewLikes;
}
