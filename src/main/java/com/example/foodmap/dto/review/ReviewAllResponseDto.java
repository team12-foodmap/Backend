package com.example.foodmap.dto.review;

import com.example.foodmap.model.Location;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewAllResponseDto {
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
    private List<ReviewLikesDto> reviewLikesDtoList;

}
