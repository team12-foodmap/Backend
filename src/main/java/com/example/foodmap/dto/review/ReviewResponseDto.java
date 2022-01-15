package com.example.foodmap.dto.review;

import com.example.foodmap.model.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {
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
