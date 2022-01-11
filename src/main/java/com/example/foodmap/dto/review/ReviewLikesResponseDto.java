package com.example.foodmap.dto.review;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewLikesResponseDto {
    private Long reviewId;
    private String content;
    private int spicy;
    private int restaurantTags;
    private MultipartFile image;
    private int reviewLikes;
}
