package com.example.foodmap.dto.review;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewUpdateRequestDto {
    private String content;
    private String spicy;
    private String restaurantTags;

}
