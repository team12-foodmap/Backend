package com.example.foodmap.dto.mypage;

import com.example.foodmap.model.Location;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyReviewDetailResponseDto {
    private Long reviewId;
    private Long restaurantId;
    private Long userId;
    private Location location;
    private String content;
    private String image;
    private int reviewLikes;
    private String nickname;
    private String restaurantName;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


}

