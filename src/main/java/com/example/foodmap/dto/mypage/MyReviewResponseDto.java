package com.example.foodmap.dto.mypage;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MyReviewResponseDto {
    private Long reviewId;
    private Long userId;
    private Long restaurantId;
    private String image;

}
